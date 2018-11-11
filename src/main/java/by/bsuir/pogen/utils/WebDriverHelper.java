package by.bsuir.pogen.utils;

import by.bsuir.pogen.constants.Constants;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static by.bsuir.pogen.constants.Constants.SCRIPT_GET_ELEMENT_BORDER;
import static by.bsuir.pogen.constants.Constants.SCRIPT_UNHIGHLIGHT_ELEMENT;


/**
 * Created by Alexei Khilchuk on 13.07.2018.
 */
public class WebDriverHelper {
    private static final String LOCALHOST = "127.0.0.1";
    static Logger LOG = Logger.getLogger(WebDriverHelper.class.getName());
    private static WebElement lastElem = null;
    private static String lastBorder = null;
    private static String androidHost = System.getProperty("androidHost", LOCALHOST);
    private static String androidPort = System.getProperty("androidPort", "6789");
    private static String androidEmuSid = System.getProperty("androidSerial", "emulator-5554");
    private static String androidBrowserName = System.getProperty("androidBrowserName", "android");

    public static RemoteWebDriver getWebDriver(final Constants.BrowserType type, Proxy proxy) {
        DesiredCapabilities capabilitiesProxy = new DesiredCapabilities();
        if (proxy != null) {
            capabilitiesProxy.setCapability(CapabilityType.PROXY, proxy);
        }
        switch (type) {
            case CHROME:
                return getChromeDriver(proxy);
            case FIREFOX:
                return getFirefoxDriver(capabilitiesProxy);
            case ANDROID:
                return getAppiumAndroidDriver();
            default:
                return getChromeDriver(proxy);
        }
    }

    private static RemoteWebDriver getFirefoxDriver(DesiredCapabilities capabilities) {
        DesiredCapabilities caps = (capabilities != null) ? capabilities : new DesiredCapabilities();
        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setPreference("browser.link.open_newwindow.restriction", 1);

        return new FirefoxDriver();
    }

    private static RemoteWebDriver getChromeDriver(Proxy proxy) {
        String platform = System.getProperty("os.name").toLowerCase();
        File myFile = null;

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-popup-blocking");

        try {
            if (platform.contains("win")) {
                myFile = new File("chromedriver.exe");
            } else if (platform.contains("mac")) {
                myFile = new File("chromedriver_mac");
            } else if (platform.contains("linux")) {
                myFile = new File("chromedriver_linux");
            } else {
                LOG.info(String.format("Unsupported platform: %1$s for chrome browser %n", platform));
            }
        } catch (Exception e1) {
            LOG.error(WebDriverHelper.class.getName() + ".getChromeDriver", e1);
        }
        System.setProperty("webdriver.chrome.driver", myFile.getAbsolutePath());

        System.setProperty("webdriver.chrome.logfile", "/tmp/chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        RemoteWebDriver driver = new ChromeDriver(options);
        //driver.manage().window().maximize();
        return driver;
    }

    private static RemoteWebDriver getAppiumAndroidDriver(){

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, System.getProperty("deviceName", "Nexus 5X API 24"));
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, System.getProperty("androidVersion", "7.0"));
        caps.setCapability(MobileCapabilityType.APP, System.getProperty("app"));
        caps.setCapability("appPackage", System.getProperty("appPackage"));
        caps.setCapability("appActivity", System.getProperty("appActivity"));
        caps.setCapability("clearSystemFiles", true);
        caps.setCapability("unicodeKeyboard", true);
        caps.setCapability("resetKeyboard", true);
        caps.setCapability("newCommandTimeout", 0);

        //Start the server with the builder
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .withIPAddress(LOCALHOST)
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                //.withArgument(GeneralServerFlag.LOG_LEVEL,"")
                .withStartUpTimeOut(30, TimeUnit.SECONDS)
        );
        service.start();

        try {
            return new AndroidDriver(new URL(String.format("http://%1$s:4723/wd/hub", LOCALHOST)), caps);
        } catch (MalformedURLException e) {
            LOG.error(e);
            return null;
        }

    }

    public static void highlightElement(WebElement elem, RemoteWebDriver webDriver) {
        if (!(webDriver instanceof AndroidDriver))
        {
            unhighlightLast(webDriver);
            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            // remember the new element
            lastElem = elem;
            lastBorder = (String) (js.executeScript(SCRIPT_GET_ELEMENT_BORDER, elem));
            js.executeScript("arguments[0].scrollIntoView(true);", elem);
        }
    }

    private static void unhighlightLast(RemoteWebDriver webDriver) {
        if (lastElem != null) {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            try {
                // if there already is a highlighted element, unhighlight it
                js.executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, lastElem, lastBorder);
            } catch (StaleElementReferenceException ignored) {
                // the page got reloaded, the element isn't there
            } finally {
                // element either restored or wasn't valid, nullify in both cases
                lastElem = null;
            }
        }
    }
}
