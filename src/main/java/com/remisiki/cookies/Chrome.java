package com.remisiki.cookies;

import java.io.File;
import java.net.HttpCookie;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import java.time.Instant;

public class Chrome {

	protected char[] secret;
	protected byte[] key;
	protected File cookieFile;
	protected Sqlite db;

	public Chrome() {}

	public void connect() {
		try {
			this.secret = Crypto.getSecret();
			this.key = Crypto.generateKey(this.secret);
			this.createTmpCookieFile();
			this.db = new Sqlite(this.cookieFile);
			this.db.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createTmpCookieFile() throws Exception {
		Path cookieFilePath = Paths.get(System.getProperty("user.home"), ".config/google-chrome/Default/Cookies");
		Path tmpFilePath = Paths.get(System.getProperty("java.io.tmpdir"), ".com.remisiki", "cookies");
		Files.createDirectories(tmpFilePath);
		tmpFilePath = Paths.get(tmpFilePath.toString(), ".chrome.cookies.sqlite");
		Files.copy(cookieFilePath, tmpFilePath, StandardCopyOption.REPLACE_EXISTING);
		this.cookieFile = new File(tmpFilePath.toString());
	}

	protected byte[] decryptCookieValue(byte[] rawValue) {
		if (rawValue.length <= 3) {
			return new byte[]{};
		} else {
			try {
				byte[] cipherText = Arrays.copyOfRange(rawValue, 3, rawValue.length);
				byte[] plainText = Crypto.decrypt(cipherText, this.key);
				return plainText;
			} catch (Exception e) {
				return new byte[]{};
			}
		}
	}

	public List<HttpCookie> getCookies() {
		return this.getCookies("");
	}

	public List<HttpCookie> getCookies(String domain) {
		List<HttpCookie> li = new ArrayList<HttpCookie>();
		try {
			String queryString = "SELECT host_key, name, encrypted_value, is_httponly, expires_utc, path, is_secure FROM cookies";
			if (domain != "") {
				queryString += String.format(" WHERE host_key LIKE \"%%%s%%\"", domain);
			}
			byte[][][] rs = this.db.query(queryString);
			for (byte[][] row: rs) {
				byte[] plainText = this.decryptCookieValue(row[2]);
				HttpCookie cookie = new HttpCookie(new String(row[1]), new String(plainText));
				cookie.setDomain(new String(row[0]));
				cookie.setHttpOnly((new String(row[3]) == "true"));
				cookie.setMaxAge(Long.parseLong(new String(row[4])) / 1000000L - 11644473600L - Instant.now().getEpochSecond());
				cookie.setPath(new String(row[5]));
				cookie.setSecure((new String(row[6]) == "true"));
				li.add(cookie);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return li;
		}
	}

}