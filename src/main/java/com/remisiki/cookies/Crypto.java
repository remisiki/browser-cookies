package com.remisiki.cookies;

import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.freedesktop.secret.simple.SimpleCollection;

public class Crypto {

	public static char[] getSecret() throws Exception {
		try (SimpleCollection collection = new SimpleCollection()) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("application", "chrome");
			m.put("xdg:schema", "chrome_libsecret_os_crypt_password_v2");
			List<String> items = collection.getItems(m);
			if (items.size() > 0) {
				String path = items.get(0);
				return collection.getSecret(path);
			} else {
				throw new Exception("Cannot find secret");
			}
		} catch (Exception err) {
			throw err;
		}
	}

	public static byte[] generateKey(char[] secret) throws Exception {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec keySpec = new PBEKeySpec(secret, "saltysalt".getBytes(), 1, 128);
		SecretKey key = keyFactory.generateSecret(keySpec);
		return key.getEncoded();
	}

	public static byte[] decrypt(byte[] cipherText, byte[] key) throws Exception {
		byte[] iv = new byte[16];
		Arrays.fill(iv, (byte) 32);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
		return cipher.doFinal(cipherText);
	}

}