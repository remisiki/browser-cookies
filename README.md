# browser-cookies

Decrypt cookies from browsers.

Inspired from [browser-cookie3](https://github.com/borisbabic/browser_cookie3), but written in Java.

## Requirements

Tested on Linux with GNOME, Chrome 108.

## Install

maven
```xml
<dependency>
	<groupId>com.remisiki</groupId>
	<artifactId>browser-cookies</artifactId>
	<version>0.1.0</version>
</dependency>
```

sbt
```scala
libraryDependencies += "com.remisiki" % "browser-cookies" % "0.1.0"
```

## Usage

Get a list of cookies using `Chrome::getCookies`. Fetch all cookies when domain is not specified.

```java
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
```

## License

BSD 3-Clause