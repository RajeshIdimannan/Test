package controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestXpath {

	public static void main(String[] args) {
		
		FirefoxDriver driver = null;
		try {
			/*
			 * FirefoxProfile fp = new FirefoxProfile();
			 * fp.setEnableNativeEvents(true);
			 * fp.setPreference("network.proxy.type",
			 * "http://browserconfig.target.com/proxy-Global.pa");
			 */
			org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
			proxy.setProxyAutoconfigUrl("http://browserconfig.target.com/proxy-Global.pac");
			DesiredCapabilities dc = DesiredCapabilities.firefox();
			dc.setCapability(CapabilityType.PROXY, proxy);
			driver = new FirefoxDriver(dc);
			driver.manage().window().maximize();
		} catch (WebDriverException we) {
		} catch (Throwable th) {
		}
		String s = (String) ((JavascriptExecutor) driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		System.out.println(s);
		driver.get("http://xcc-qa02.target.com");
		String pageSource=driver.getPageSource();
		
		//WebElement ele=driver.;
		
		//Source code to copy page source to a notepad.
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(pageSource);
		clipboard.setContents(strSel, null);
		driver.quit();
		//System.out.println(driver.getPageSource().contains("patio furniture"));
	}
}
