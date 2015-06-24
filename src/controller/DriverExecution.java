package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.iphone.IPhoneDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.opera.core.systems.OperaDriver;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


@SuppressWarnings("deprecation")
public class DriverExecution {

	public String initDriver(String browserName, String iOSProxy) {
		String browserPath = new String("");
		int index = browserName.indexOf(" ");
		if (index != -1) {
			browserPath = (String) browserName.subSequence(index + 1,
					browserName.length());
		}
		if (browserName.toLowerCase().contains("firefox"))
			return initFirefoxDriver(browserPath);
		if (browserName.toLowerCase().contains("chrome"))
			return initChromeDriver(browserPath);
		if (browserName.toLowerCase().contains("iexplore"))
			return initIeDriver(browserPath);
		if (browserName.toLowerCase().contains("opera"))
			return initOperaDriver(browserPath);
		if (browserName.toLowerCase().contains("safari"))
			return initSafariDriver(browserPath);
		if (browserName.toLowerCase().contains("android"))
			return initAndroidDriver(browserPath);
		if (browserName.toLowerCase().contains("iphone")) 
			return initIPhoneDriver(browserPath,iOSProxy);
		return "UnRecognised Browser";
	}

	private WebDriver driver = null;

	public String initFirefoxDriver(String binaryPath) {
		if (!binaryPath.equals("")) {
			System.setProperty("webdriver.firefox.bin", binaryPath);
		}
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
			this.driver = new FirefoxDriver(dc);
			this.driver.manage().deleteAllCookies();
			this.driver.manage().window().maximize();
		} catch (WebDriverException we) {
		} catch (Throwable th) {
		}
		String s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		System.out.println(s);
		return getBrowserVersion("Firefox", s);
	}

	public String initIeDriver(String binaryPath) {

		System.setProperty("webdriver.ie.driver", "lib\\IEDriverServer.exe");
		String s;
		try {		
			DesiredCapabilities iecapabilities = DesiredCapabilities
					.internetExplorer();
			iecapabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);			
			this.driver = new InternetExplorerDriver(iecapabilities);
			this.driver.manage().deleteAllCookies();
			this.driver.manage().window().maximize();
			/*this.driver.findElement(By.tagName("body")).sendKeys(Keys.F12);			
			String selectAll = Keys.chord(Keys.ALT, "8");
			this.driver.findElement(By.tagName("body")).sendKeys(selectAll);	
			String pinIt = Keys.chord(Keys.CONTROL, "p");
			this.driver.findElement(By.tagName("body")).sendKeys(pinIt);*/
		} catch (WebDriverException we) {
			System.out.println(we.getMessage());
			return "";
		} catch (Throwable th) {
			System.out.println(th.getMessage());
			return "";
		}
		s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		return getBrowserVersion("InternetExplorer", s);
	}

	public String initChromeDriver(String binaryPath) {
		File dir = new File(".");
		String path = null;
		String OS = System.getProperty("os.name").toLowerCase();
		try {
			if (OS.indexOf("win") >= 0)
				path = dir.getCanonicalPath() + "\\lib\\chromedriver.exe";
			else if (OS.indexOf("mac") >= 0)
				path = "/usr/bin/chromedriver";
			if (!binaryPath.equals("")) {
				System.setProperty("chrome.binary", binaryPath);
			}
			System.setProperty("webdriver.chrome.driver", path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return "";
		}
		try {
			this.driver = new ChromeDriver();
			this.driver.manage().deleteAllCookies();
			this.driver.manage().window().maximize();
		} catch (WebDriverException we) {
			we.printStackTrace();
			return "";
		} catch (Throwable th) {
			th.printStackTrace();
			return "";
		}
		String s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		return getBrowserVersion("GoogleChrome", s);
	}

	public String initOperaDriver(String binaryPath) {
		if (!binaryPath.equals(""))
			System.setProperty("opera.binary", binaryPath);
		try {
			this.driver = new OperaDriver();
			this.driver.manage().window().maximize();
		} catch (WebDriverException we) {
			we.printStackTrace();
			return "";
		} catch (Throwable th) {
			th.printStackTrace();
			return "";
		}
		String s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		return getBrowserVersion("Opera", s);
	}

	// Initiate Safari Driver...
	public String initSafariDriver(String binaryPath) {
		if (!binaryPath.equals("")) {
			System.setProperty("webdriver.safari.bin", binaryPath);
		}
		try {
			this.driver = new SafariDriver();
			this.driver.manage().deleteAllCookies();
			this.driver.manage().window().maximize();
		} catch (WebDriverException we) {
			we.printStackTrace();
		} catch (Throwable th) {
			th.printStackTrace();
		}
		String s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		return getBrowserVersion("Safari", s);
	}

	public String initAndroidDriver(String binaryPath) {
		if (!binaryPath.equals("")) {
			System.setProperty("webdriver.android.bin", binaryPath);
		}
		try {
			this.driver = new AndroidDriver();
			this.driver.manage().deleteAllCookies();
			this.driver.manage().window().maximize();
		} catch (WebDriverException we) {
			we.printStackTrace();
		} catch (Throwable th) {
			th.printStackTrace();
		}
		String s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		System.out.println(s);
		return getBrowserVersion("Android", s);
	}


	public String initIPhoneDriver(String binaryPath, String iOSProxy) {
		if (!binaryPath.equals("")) {
			System.setProperty("webdriver.firefox.bin", binaryPath);
		}
		try {
			DesiredCapabilities dc = DesiredCapabilities.iphone();
			dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			this.driver = new IPhoneDriver("http://"+iOSProxy+"/wd/hub/");
			this.driver.manage().deleteAllCookies();
		} catch (WebDriverException we) {
			we.printStackTrace();
		} catch (Throwable th) {
			th.printStackTrace();
		}
		String s = (String) ((JavascriptExecutor) this.driver).executeScript(
				"return navigator.userAgent;", new Object[0]);
		System.out.println(s);
		return getBrowserVersion("IPhone", s);
	}


	// To Get Version of Browser we are executing...
	public String getBrowserVersion(String browserName,
			String JavaScriptUserAgent) {
		String browser_version = "";
		if (browserName.contains("InternetExplorer")) {
			browser_version = StringUtils.substringBetween(JavaScriptUserAgent,
					"MSIE", ";");
			browserName = "InternetExplorer " + browser_version;
		} else if (browserName.contains("Firefox")) {
			browser_version = StringUtils.substringAfterLast(
					JavaScriptUserAgent, "Firefox/");
			browserName = "Firefox " + browser_version;
		} else if (browserName.contains("GoogleChrome")) {
			browser_version = StringUtils.substringBetween(JavaScriptUserAgent,
					"Chrome/", " ");
			browserName = "GoogleChrome " + browser_version;
		} else if (browserName.contains("Opera")) {
			browser_version = StringUtils.substringBetween(JavaScriptUserAgent,
					"Opera/", " ");
			browserName = "Opera " + browser_version;
		} else if (browserName.contains("Safari")) {
			browser_version = StringUtils.substringBetween(JavaScriptUserAgent,
					"Version/", " ");
			browserName = "Safari " + browser_version;
		} else if (browserName.contains("Android")) {
			// browser_version = Build.VERSION.RELEASE;
			browserName = "Android " + browser_version;
		} else if (browserName.contains("IPhone")) {
			// browser_version = Build.VERSION.RELEASE;
			browserName = "IPhone " + browser_version;
		}
		return browserName;
	}

	// Capture Screenshot of Application alone
	public void captureScreen(String filename) {
		try {
			if (this.driver != null) {
				TakesScreenshot ts = (TakesScreenshot) this.driver;
				try {
					File scrFile = (File) ts.getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(scrFile, new File(filename));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * try { BufferedImage screencapture = new Robot()
		 * .createScreenCapture(new Rectangle(Toolkit
		 * .getDefaultToolkit().getScreenSize())); ImageIO.write(screencapture,
		 * "png", new File(filename)); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */

	}

	public void ieModeControl(String browserVersion){
		String[] ieVersion = browserVersion.split(" ");
		if (ieVersion[2].equalsIgnoreCase("9.0")) {			
			String browserMode = Keys.chord(Keys.ALT, "f");
			this.driver.findElement(By.tagName("body")).sendKeys(browserMode);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
							
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_DOWN);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_DOWN);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);
			String docMode = Keys.chord(Keys.ALT, "9");
			this.driver.findElement(By.tagName("body")).sendKeys(docMode);
			String close= Keys.chord(Keys.ALT,Keys.F4);
			this.driver.findElement(By.tagName("body")).sendKeys(close);
			try {
				Thread.sleep(4000L);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			/*String minimize = Keys.chord(Keys.ALT,Keys.ENTER);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_DOWN);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_DOWN);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_DOWN);
			this.driver.findElement(By.tagName("body")).sendKeys(Keys.ENTER);*/
			//String pinIt = Keys.chord(Keys.CONTROL, "p");
			//this.driver.findElement(By.tagName("body")).sendKeys(pinIt);
		}
	}
	
	public ArrayList<SeleneseData> runserver(String timeOut,
			ArrayList<SeleneseData> hd, String outputPath, String browserVersion, String screenFlag) {
		URL url = null;
		String OS = System.getProperty("os.name").toLowerCase();
		// URL Parsing or URL Encoding
		String URLstr = "http://" + ((SeleneseData) hd.get(1)).getBaseurl();
		try {
			url = new URL(URLstr);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(),
					url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());
			url = uri.toURL();
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			this.driver.get(url.toString());
			/*((JavascriptExecutor) driver).executeScript(
	                  "function pageloadingtime()"+
	                              "{"+
	                              "return 'Page has completely loaded'"+
	                              "}"+
	                  "return (window.onload=pageloadingtime());");*/
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return document.readyState").equals("complete");
				}
			};
			Wait<WebDriver> wait = new WebDriverWait(driver,30);				
		      try {
		              wait.until(expectation);
		      } catch(Throwable error) {
		             error.getStackTrace();
		      }		      
		} catch (Exception e) {
			System.out
					.println("Page could not be loaded within specified time: "
							+ e.getMessage());
		}
		//WebElement mouseOverElement = null;
		File screenshotDirectory = null;
		if (OS.indexOf("win") >= 0)
			screenshotDirectory = new File(outputPath + "\\" + browserVersion);
		else if (OS.indexOf("mac") >= 0)
			screenshotDirectory = new File(outputPath + "/" + browserVersion);
		if (!screenshotDirectory.exists()) {
			screenshotDirectory.mkdirs();
		}
		for (int i = 0; i < hd.size(); i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			sdf.setTimeZone(TimeZone.getTimeZone("IST"));
			Date date = new Date();
			String screenShotPath = null;
			try {
				if (OS.indexOf("win") >= 0)
					screenShotPath = screenshotDirectory.getCanonicalPath()
							.toString() + "\\";
				else if (OS.indexOf("mac") >= 0)
					screenShotPath = screenshotDirectory.getCanonicalPath()
							.toString() + "/";
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			screenShotPath = screenShotPath + sdf.format(date) + ".png";
			SeleneseData seleneseData = (SeleneseData) hd.get(i);
			seleneseData.setExstatus(false);
			ArrayList<String> targetList = seleneseData.getTargetList();
			boolean executeStatus = true;
			WebElement element = null;
			int count = 0;
			if (executeStatus) {
				while (count < targetList.size()) {
					seleneseData.setTarget((String) targetList.get(count));
					try {
						element = getElementFromLoc((String) targetList
								.get(count));
					} catch (Exception e) {
						System.out.println("Element: "
								+ (String) targetList.get(count) + "  --  "
								+ e.getMessage());
						seleneseData.setMessage(e.getMessage());
					}
					count++;
				}
				try {
					if ((!seleneseData.isExstatus()))						
							seleneseData = executestep(element,	seleneseData, screenShotPath,browserVersion,screenFlag);
				} catch (NullPointerException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage("Element Not Found");
					e.printStackTrace();
				} catch (ElementNotVisibleException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage("Element not visible");
					e.printStackTrace();
				} catch (MoveTargetOutOfBoundsException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData
							.setMessage("Element Cannot be scrolled into view");
					e.printStackTrace();
				} catch (ElementNotFoundException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage("Element Not Found");
					e.printStackTrace();
				} catch (SeleniumException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage(e.getMessage());
					e.printStackTrace();
				} catch (WebDriverException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage("Element Not Found");
					e.printStackTrace();
				} catch (AWTException e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage("Key could not be pressed");
					e.printStackTrace();
				} catch (Exception e) {
					captureScreen(screenShotPath);
					seleneseData.setExstatus(false);
					seleneseData.setMessage(e.getMessage());
					e.printStackTrace();
				}
				hd.remove(i);
				hd.add(i, seleneseData);
			}
		}
		try {
			this.driver.quit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return hd;
	}

	public WebElement getElementFromLoc(String target) throws Exception {
		WebElement webelement = null;
		StringBuffer element = new StringBuffer(target);
		try {
			if (target.startsWith("//")) {
				try {
					webelement = this.driver.findElement(By.xpath(element
							.toString()));
				} catch (ElementNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			} else if (target.contains("xpath=")) {
				try {
					element.delete(0, 6);
					webelement = this.driver.findElement(By.xpath(element
							.toString()));
				} catch (ElementNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			} else if (target.contains("id=")) {
				try {
					element.delete(0, 3);
					webelement = this.driver.findElement(By.id(element
							.toString()));
				} catch (ElementNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			} else if (target.contains("name=")) {
				try {
					element.delete(0, 5);
					webelement = this.driver.findElement(By.name(element
							.toString()));
				} catch (ElementNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			} else if (target.contains("css=")) {
				try {
					element.delete(0, 4);
					webelement = this.driver.findElement(By.cssSelector(element
							.toString()));
				} catch (NoSuchElementException e) {
					e.printStackTrace();
					return null;
				}
			} else if (target.contains("link=")) {
				try {
					element.delete(0, 5);
					webelement = this.driver.findElement(By.linkText(element
							.toString()));
				} catch (NoSuchElementException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				webelement = null;
			}
			return webelement;
		} catch (WebDriverException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public SeleneseData executestep(WebElement element,
			 SeleneseData hd, String outputPath,
			String browserVersion,String screenFlag) throws NullPointerException,
			AWTException, MoveTargetOutOfBoundsException,
			ElementNotVisibleException, ElementNotFoundException,
			WebDriverException, SeleniumException, Exception {
		SeleneseData hh = hd;
		System.out.println(hh.getTarget());
		String str, value, labelValue, text;
		StringBuffer label;
		int timeout;
		Select select;
		Robot robot;
		Process process;
		switch (hd.getCommand()) {
		case "webAuthPopup":
			File dir = new File(".");
			String[] credentials=hd.getValue().split(",");
			process = new ProcessBuilder(dir.getCanonicalPath() + "\\lib\\WebAuthPopup.exe",credentials[0],credentials[1],credentials[2]).start();
			process.waitFor();
			break;
		case "sync":			
			try {
				/*((JavascriptExecutor) driver).executeScript(
		                  "function pageloadingtime()"+
		                              "{"+
		                              "return 'Page has completely loaded'"+
		                              "}"+
		                  "return (window.onload=pageloadingtime());");
				
				ExpectedConditions<Boolean> expectation = new ExpectedConditions<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return ((JavascriptExecutor) driver).executeScript(
								"return document.readyState").equals("complete");
					}
				};*/
				//ExpectedConditions.presenceOfElementLocated(By.xpath(element.toString()));
				Wait<WebDriver> wait = new WebDriverWait(driver, Integer.parseInt(hd.getValue()));
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(hd.getTarget())));
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}				
			} catch (Exception e) {
				e.printStackTrace()	;
				hh.setExstatus(false);
				if(screenFlag == "Fail" || screenFlag == "Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setMessage("Unable to load webpage within " + hd.getValue() + "Seconds");
			}
			break;
		case "syncPageLoad":			
			try {
				/*((JavascriptExecutor) driver).executeScript(
		                  "function pageloadingtime()"+
		                              "{"+
		                              "return 'Page has completely loaded'"+
		                              "}"+
		                  "return (window.onload=pageloadingtime());");
				*/
				ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return ((JavascriptExecutor) driver).executeScript(
								"return document.readyState").equals("complete");
					}
				};
				Wait<WebDriver> wait = new WebDriverWait(driver,30);				
			      try {
			              wait.until(expectation);
			      } catch(Throwable error) {
			             error.getStackTrace();
			      }
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}				
			} catch (Exception e) {
				e.printStackTrace()	;
				hh.setExstatus(false);
				if(screenFlag == "Fail" || screenFlag == "Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setMessage("Unable to load webpage within 30 seconds");				
			}
			break;
		case "verifyElementNotPresent":
			if (element == null) {
					if(screenFlag=="Both"){
						captureScreen(outputPath);
						hh.setScreenshotPath(outputPath);
					}						
				hh.setExstatus(true);				
			} else {
				highlightElement(element);
					if(screenFlag == "Fail" || screenFlag == "Both")
						captureScreen(outputPath);
				unhighlightElement(element);
				hh = setElementData(hh, element);
				hh.setExstatus(false);
				hh.setMessage("Element is Present");
				hh.setScreenshotPath(outputPath);
			}
			break;
		case "open":
			if (!hd.getTarget().equalsIgnoreCase("/")) {
				// URL Parsing or URL Encoding
				String URLstr = "http://" + hh.getBaseurl();
				try {
					URL url = new URL(URLstr);
					URI uri = new URI(url.getProtocol(), url.getUserInfo(),
							url.getHost(), url.getPort(), url.getPath(),
							url.getQuery(), url.getRef());
					url = uri.toURL();
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				// this.driver.get(URLstr);
			}
			hh.setExstatus(true);
			break;
		case "close":
			if(screenFlag=="Both"){
				captureScreen(outputPath);
				hh.setScreenshotPath(outputPath);
			}
			this.driver.close();
			hh.setExstatus(true);
			break;
		case "type":
			if(element!= null){
				Thread.sleep(1000L);
				element.clear();
				element.sendKeys(new CharSequence[] { hd.getValue().trim() });
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					unhighlightElement(element);
					hh.setScreenshotPath(outputPath);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			}else{							
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}						
				hh.setExstatus(false);			
			}
			break;
		case "selectWindow":
			Object[] list = this.driver.getWindowHandles().toArray();
			try {
				int index = Integer.parseInt(hd.getTarget());
				for (int i = 0; i < list.length; i++) {
					if (index == i) {
						hh.setExstatus(true);
						String windowHandle = (String) list[i];
						this.driver.switchTo().window(windowHandle);
						if(screenFlag=="Both"){
							captureScreen(outputPath);
							hh.setScreenshotPath(outputPath);
						}
					}
				}
			} catch (NumberFormatException nfe) {
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setMessage("Index not in numeric");				
			} catch (Exception e) {
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setMessage("Could not switch to window");
			}
			break;
		case "storeTitle":
			str = this.driver.getTitle();
			if (str != "") {
				hd.setValue(str);
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			} else {
				hh.setExstatus(false);
				hh.setMessage("Title not found");
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		case "assertTitle":
			str = this.driver.getTitle();
			if (str.equals(hd.getTarget())) {
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			} else {
				hh.setExstatus(false);
				hh.setMessage("Title doesn't match: current title set to - "
						+ str);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		case "storeText":
			try {
				captureScreen(outputPath);
			} catch (Exception localException1) {
			}
			value = element.getAttribute("value");
			hh.setValue(value);
			hh = setElementData(hh, element);
			hh.setScreenshotPath(outputPath);
			break;
		case "storeValue":
			try {
				captureScreen(outputPath);
			} catch (Exception localException1) {
			}
			value = element.getAttribute("value");
			hh.setValue(value);
			hh = setElementData(hh, element);
			hh.setScreenshotPath(outputPath);
			break;
		case "verifyValue":
			try {
				Thread.sleep(2000L);
				captureScreen(outputPath);
			} catch (Exception localException2) {
			}
			value = element.getAttribute("value");
			if (value.equals(hd.getValue())) {
				hh = setElementData(hh, element);
				hh.setScreenshotPath(outputPath);
			} else {
				hh.setExstatus(false);
				hh.setMessage("Value is Different");
				hh.setScreenshotPath(outputPath);
			}
			break;
		case "isVisible":
			if (element.isDisplayed() && element != null) {
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					unhighlightElement(element);
					hh.setScreenshotPath(outputPath);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			} else {
				hh.setExstatus(false);
				hh.setMessage("Element is not Visible.");
				if(screenFlag=="Fail" || screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					unhighlightElement(element);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		case "isNotVisible":
			if (!element.isDisplayed() && element != null) {
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					unhighlightElement(element);
					hh.setScreenshotPath(outputPath);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			} else {
				hh.setExstatus(false);
				hh.setMessage("Element is  Visible.");
				if(screenFlag=="Fail" || screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					unhighlightElement(element);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		/*case "isEditable":
			//highlightElement(element);
			try {
				Thread.sleep(2000L);
				captureScreen(outputPath);
				//unhighlightElement(element);
			} catch (Exception localException3) {
			}
			if (wbs.isEditable(hd.getTarget())) {
				hh = setElementData(hh, element, outputPath);
				hh.setScreenshotPath(outputPath);
			} else {
				hh.setExstatus(false);
				hh.setMessage("The Element cannot be Edited");
				hh.setScreenshotPath(outputPath);
			}
			break;*/
		case "isSelectedForDropDown":
			//highlightElement(element);
			try {
				Thread.sleep(2000L);
				captureScreen(outputPath);
				//unhighlightElement(element);
			} catch (Exception localException4) {
			}
			labelValue = new String();
			if (hd.getValue().startsWith("label="))
				labelValue = hd.getValue().substring(6, hd.getValue().length());
			else {
				labelValue = hd.getValue();
			}
			select = new Select(element);
			String selectedLabel = select.getFirstSelectedOption().getText();
			if (selectedLabel.trim().equals(labelValue)) {
				hh = setElementData(hh, element);
				hh.setScreenshotPath(outputPath);
			} else {
				hh.setExstatus(false);
				hh.setMessage("Expected value " + hd.getValue()
						+ " does not match with " + labelValue);
				hh.setScreenshotPath(outputPath);
			}
			break;
		case "removeSelection":
			//highlightElement(element);
			labelValue = new String();
			if (hd.getValue().startsWith("label="))
				labelValue = hd.getValue().substring(6, hd.getValue().length());
			else {
				labelValue = hd.getValue();
			}
			select = new Select(element);
			select.deselectByVisibleText(labelValue);
			hh = setElementData(hh, element);
			try {
				captureScreen(outputPath);
				//unhighlightElement(element);
			} catch (Exception localException5) {
				System.out.println(localException5.getMessage());
			}
			hh.setScreenshotPath(outputPath);
			break;
		case "verifyTextPresent":
			text = element.getText().trim() == null ? "" : element.getText()
					.trim();
			value = element.getAttribute("value") == null ? "" : element
					.getAttribute("value");
			if (this.driver.getPageSource().contains(hd.getTarget())) {
				hh = setElementData(hh, element);
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			} else {
				hh = setElementData(hh, element);
				hh.setExstatus(false);
				if (text.isEmpty())
					hh.setMessage("Text mismatch: " + value);
				else {
					hh.setMessage("Text mismatch: " + text);
				}
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
			/////////////////////////////////////////////Done////////////////////////
		case "scrollUp":
			((JavascriptExecutor) driver).executeScript(
					"window.scrollTo(document.body.scrollHeight,0)", "");
			hh.setExstatus(true);
			if(screenFlag=="Both"){
				captureScreen(outputPath);
				hh.setScreenshotPath(outputPath);
			}
			break;
		case "scrollDown":
			((JavascriptExecutor) driver).executeScript(
					"window.scrollTo(0,document.body.scrollHeight)", "");
			hh.setExstatus(true);
			if(screenFlag=="Both"){
				captureScreen(outputPath);
				hh.setScreenshotPath(outputPath);
			}
			break;
		case "verifyText":
			text = element.getText().trim() == null ? "" : element.getText()
					.trim();
			value = element.getAttribute("value") == null ? "" : element
					.getAttribute("value");
			if ((text.equals(hd.getValue().trim()))
					|| (value.equals(hd.getValue().trim()))) {
				hh = setElementData(hh, element);
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
			} else {
				hh.setExstatus(false);
				if (text.isEmpty())
					hh.setMessage("Text mismatch: " + value);
				else
					hh.setMessage("Text mismatch: " + text);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
			}
			break;
		case "verifyNotText":
			text = element.getText().trim() == null ? "" : element.getText()
					.trim();
			value = element.getAttribute("value") == null ? "" : element
					.getAttribute("value");
			if (!((text.equals(hd.getValue().trim())) || (value.equals(hd
					.getValue().trim())))) {
				hh = setElementData(hh, element);
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
			} else {
				hh.setExstatus(false);
				if (text.isEmpty())
					hh.setMessage("Text matches: " + value);
				else
					hh.setMessage("Text matches: " + text);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
			}
			break;
		case "check":
			if(element != null){
				if (!element.isSelected())
					element.click();
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			}else{
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setExstatus(false);
			}			
			break;
		case "unCheck":
			if(element != null){
				if (element.isSelected())
					element.click();
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			}else{
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setExstatus(false);
			}
			break;
		case "getAllWindowIds":
			java.util.Set<String> set = this.driver.getWindowHandles();
			String windowIds = new String();
			for (String string : set) {
				windowIds = windowIds + string + ", ";
			}
			windowIds.substring(0, windowIds.length() - 2);
			hh.setValue(windowIds);
			hh.setExstatus(true);
			hh.setScreenshotPath(outputPath);
			captureScreen(outputPath);
			break;
		case "getHtmlText":
			str = element.getText();
			hh.setExstatus(true);
			hh.setMessage(str);
			hh.setScreenshotPath(outputPath);
			captureScreen(outputPath);
			break;
		case "assertText":
			hd.getTarget().equalsIgnoreCase(element.getText());
			//wbs.getText(hd.getTarget());
			hh.setExstatus(true);
			captureScreen(outputPath);
			hh.setScreenshotPath(outputPath);
			break;
		case "alertEquals":
			Alert alert=this.driver.switchTo().alert();
			if(alert.equals(hd.getValue().trim())){			
				hh.setExstatus(true);
				hh.setScreenshotPath(outputPath);
			} else {
				hh.setExstatus(false);
				hh.setScreenshotPath(outputPath);
			}
			captureScreen(outputPath);
			break;
		/*case "assertAlert":
			if (hd.getTarget() == wbs.getAlert()) {
				hh.setExstatus(true);
				hh.setScreenshotPath(outputPath);
			} else {
				hh.setExstatus(false);
				hh.setScreenshotPath(outputPath);
			}
			captureScreen(outputPath);
			break;*/
		case "assertConfirmation":
			if (this.driver.switchTo().alert().getText()
					.contains(hd.getTarget())) {
				hh.setExstatus(true);
				hh.setScreenshotPath(outputPath);
			} else {
				hh.setExstatus(false);
				hh.setScreenshotPath(outputPath);
			}
			captureScreen(outputPath);
			break;
		case "highlight":
			highlightElement(element);
			try {
				Thread.sleep(2000L);
				captureScreen(outputPath);
				hh.setScreenshotPath(outputPath);
				unhighlightElement(element);
			} catch (Exception e) {
				e.printStackTrace();
			}
			hh = setElementData(hh, element);
			break;
		case "pause":
			try {
				Thread.sleep(Long.parseLong(hd.getValue()));
				hh.setExstatus(true);
				if(screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			} catch (InterruptedException e) {
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
			//////Migration screenFlag not done for below////
		case "waitForElementPresent":
			timeout = 0;
			try {
				timeout = Integer.parseInt(hd.getValue()) / 1000;
				for (int k = 0; k <= timeout; k += 1000)
					try {
						highlightElement(element);
						try {
							Thread.sleep(2000L);
							unhighlightElement(element);
						} catch (Exception localException9) {
						}
						hh = setElementData(hh, element);
						k = timeout + 1;
					} catch (Exception e) {
						Thread.sleep(1000L);
					}
			} catch (Exception e) {
				hh.setExstatus(false);
				hh.setScreenshotPath(outputPath);
			}
			captureScreen(outputPath);
			break;
		/*case "waitForTextPresent":
			timeout = 0;
			try {
				timeout = (int) (Long.parseLong(hd.getValue()) / 1000L);
				for (int k = 1; k <= timeout; k++)
					if (wbs.isTextPresent(hd.getTarget())) {
						hh.setExstatus(true);
						k = timeout + 1;
					} else {
						if (k == timeout) {
							hh.setExstatus(false);
							hh.setMessage("Text not found");
						}
						Thread.sleep(1000L);
					}
			} catch (Exception e) {
				hh.setExstatus(false);
				hh.setMessage("incorrect format of parameter value");
				hh.setScreenshotPath(outputPath);
				e.printStackTrace();
			}
			captureScreen(outputPath);
			hh.setScreenshotPath(outputPath);
			break;*/
		case "verifyElementPresent":
			if( element != null){
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);					
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			}else{
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}			
			break;
		case "select":
			if(element != null){
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				label = new StringBuffer(hd.getValue());
				new Select(element).selectByVisibleText(label.toString());				
				hh = setElementData(hh, element);
				hh.setExstatus(true);
			}else{
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
				hh.setMessage("Element Not Found");
				hh.setExstatus(false);
			}
			break;
		case "click":
			if(element != null){
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
				JavascriptExecutor js = (JavascriptExecutor) this.driver;
				js.executeScript("arguments[0].click();",
						new Object[] { element });				
			}else{
				hh.setMessage("Element Not Found");
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
			/////Migration not done below
		case "clickAt":
			//highlightElement(element);
			try {
				Thread.sleep(2000L);
				captureScreen(outputPath);
				//unhighlightElement(element);
			} catch (Exception e) {
				e.printStackTrace();
			}
			hh = setElementData(hh, element);
			//if (mouseOverElement != null) {
				Actions action = new Actions(this.driver);
				String[] xandy = hd.getValue().split(",");
				action.moveToElement(element,
						Integer.parseInt(xandy[0]), Integer.parseInt(xandy[1]))
						.click().build().perform();

			//}
			hh.setScreenshotPath(outputPath);
			break;
		case "clickAndWait":
			if(element != null){
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				hh = setElementData(hh, element);
				hh.setExstatus(true);
				JavascriptExecutor js = (JavascriptExecutor) this.driver;
				js.executeScript("arguments[0].click();",
						new Object[] { element });				
				try{
					Thread.sleep(Long.parseLong(hd.getValue()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				hh.setMessage("Element Not Found");
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		case "PressTab":
			robot = new Robot();
			robot.keyPress(9);
			robot.keyRelease(9);
		
			hh.setExstatus(true);
			hh.setScreenshotPath(outputPath);
			captureScreen(outputPath);
			break;
		case "Escape":
			robot = new Robot();
			robot.keyPress(27);
			robot.keyRelease(27);
			hh.setExstatus(true);
			hh.setScreenshotPath(outputPath);
			captureScreen(outputPath);
			break;
		case "PressEnter":
			robot = new Robot();
			robot.keyPress(10);
			robot.keyRelease(10);
			hh.setExstatus(true);
			hh.setScreenshotPath(outputPath);
			captureScreen(outputPath);
			break;
		case "selectFrame":
			if (hd.getTarget().equals("defaultContent")) {
				this.driver.switchTo().defaultContent();
			} else {
				this.driver.switchTo().defaultContent();
				this.driver.switchTo().frame(element);
			}
			captureScreen(outputPath);
			hh.setExstatus(true);
			hh.setScreenshotPath(outputPath);
			break;
		case "mouseOver":
			if(element != null){
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				hh.setExstatus(true);
				hh = setElementData(hh, element);
				mouseOverOnElement(element);				
			}else{
				hh.setMessage("Element Not Found");
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		case "mouseOverAndWait":
			if(element != null){
				if(screenFlag=="Both"){
					highlightElement(element);
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
					unhighlightElement(element);
				}
				hh.setExstatus(true);
				hh = setElementData(hh, element);
				mouseOverOnElement(element);
				try{
					Thread.sleep(Long.parseLong(hd.getValue()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				hh.setMessage("Element Not Found");
				hh.setExstatus(false);
				if(screenFlag=="Fail" || screenFlag=="Both"){
					captureScreen(outputPath);
					hh.setScreenshotPath(outputPath);
				}
			}
			break;
		case "windowMaximize":
			hh.setExstatus(true);
			this.driver.manage().window().maximize();
			captureScreen(outputPath);
			hh.setScreenshotPath(outputPath);
			break;
		default:
			hh.setExstatus(false);
			hh.setScreenshotPath(outputPath);
			hh.setMessage("Command type not recognised");
			captureScreen(outputPath);
			break;
		}
		return hh;
	}

	public boolean highlightElement(WebElement elem) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) this.driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					new Object[] { elem, "border: 1px solid green;" });
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public boolean unhighlightElement(WebElement elem) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) this.driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					new Object[] { elem, "border: hidden;" });
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public void mouseOverOnElement(WebElement mouseOverElement) {
		try {
			if ((this.driver instanceof FirefoxDriver)) {
				Locatable button = (Locatable) mouseOverElement;
				Mouse mouse = ((HasInputDevices) this.driver).getMouse();
				mouse.mouseDown(button.getCoordinates());
			} else {
				Actions action = new Actions(this.driver);				
				action.moveToElement(mouseOverElement).build().perform();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void focus(WebElement elem) {
		JavascriptExecutor js = (JavascriptExecutor) this.driver;
		try {
			js.executeScript("arguments[0].focus();", new Object[] { elem });
		} catch (Exception localException) {
		}
	}

	public SeleneseData setElementData(SeleneseData h, WebElement we) {
		h.setX(we.getLocation().getX());
		h.setY(we.getLocation().getY());		
		h.setHeight(we.getSize().getHeight());
		h.setWidth(we.getSize().getWidth());
		h.setColor(we.getCssValue("color"));
		h.setFontFamily(we.getCssValue("font-family"));
		h.setFontSize(we.getCssValue("font-size"));
		h.setFontStyle(we.getCssValue("font-style"));
		h.setFontWeight(we.getCssValue("font-weight"));
		return h;
	}

}
