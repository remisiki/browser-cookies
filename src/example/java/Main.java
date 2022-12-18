import com.remisiki.cookies.Chrome;

public class Main {

	public static void main(String[] args) throws Exception {
		Chrome chrome = new Chrome();
		chrome.connect();
		System.out.println(chrome.getCookies().size());
		System.out.println(chrome.getCookies("github.com"));
		chrome.close();
	}

}