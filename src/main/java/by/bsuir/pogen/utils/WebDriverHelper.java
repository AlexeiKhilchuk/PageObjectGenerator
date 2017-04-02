package by.bsuir.pogen.utils;

import by.bsuir.pogen.constants.Constants;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import static by.bsuir.pogen.constants.Constants.SCRIPT_GET_ELEMENT_BORDER;
import static by.bsuir.pogen.constants.Constants.SCRIPT_UNHIGHLIGHT_ELEMENT;


/**
 * Created by Alexei Khilchuk on 04.03.2017.
 */
public class WebDriverHelper {
    static Logger LOG = LoggerFactory.getLogger(WebDriverHelper.class.getName());
    private static WebElement lastElem = null;
    private static String lastBorder = null;

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
            default:
                return getChromeDriver(proxy);
        }
    }

    private static RemoteWebDriver getFirefoxDriver(DesiredCapabilities capabilities) {
        DesiredCapabilities caps =  (capabilities != null) ? capabilities : new DesiredCapabilities();
        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setPreference("browser.link.open_newwindow.restriction", 1);

        return new FirefoxDriver(new FirefoxBinary(), ffProfile, caps);
    }

    private static RemoteWebDriver getChromeDriver(Proxy proxy) {
        String platform = System.getProperty("os.name").toLowerCase();
        URL myTestURL = null;
        File myFile = null;
        if (platform.contains("win")) {
            myTestURL = ClassLoader.getSystemResource("chromedriver.exe");
        } else if (platform.contains("mac")) {
            myTestURL = ClassLoader.getSystemResource("chromedriver_mac");
        } else if (platform.contains("linux")) {
            myTestURL = ClassLoader.getSystemResource("chromedriver_linux");
        } else {
            LOG.info(String.format("Unsupported platform: %1$s for chrome browser %n", platform));
        }
        ChromeOptions options = null;
        DesiredCapabilities cp1 = DesiredCapabilities.chrome();
        cp1.setCapability("chrome.switches", Arrays.asList("--disable-popup-blocking"));
        try {
            myFile = new File(myTestURL.toURI());
        } catch (URISyntaxException e1) {
            LOG.error( WebDriverHelper.class.getName() + ".getChromeDriver", e1);
        }
        System.setProperty("webdriver.chrome.driver", myFile.getAbsolutePath());

        if (options != null) {
            cp1.setCapability(ChromeOptions.CAPABILITY, options);
        }
        if (proxy != null) {
            cp1.setCapability(CapabilityType.PROXY, proxy);
        }
        RemoteWebDriver driver = new ChromeDriver(cp1);
        //driver.manage().window().maximize();
        return driver;
    }


    public static void highlightElement(WebElement elem, RemoteWebDriver webDriver) {
        unhighlightLast(webDriver);
        JavascriptExecutor js = (JavascriptExecutor)webDriver;

        // remember the new element
        lastElem = elem;
        lastBorder = (String)(js.executeScript(SCRIPT_GET_ELEMENT_BORDER, elem));
        js.executeScript("arguments[0].scrollIntoView(true);", elem);

    }

    private static void unhighlightLast(RemoteWebDriver webDriver) {
        if (lastElem != null) {
            JavascriptExecutor js = (JavascriptExecutor)webDriver;
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
