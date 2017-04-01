package by.bsuir.pogen.utils;

import by.bsuir.pogen.models.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by alexei.khilchuk on 31/03/2017.
 */
public class LocatorBuilder {
    public static String getCssLocator(WebElement element){
        return element.getElement().cssSelector();
    }

    public static String getXpathLocator(WebElement element, RemoteWebDriver webDriver){

        /*elementDescription = elementDescription.substring( elementDescription.indexOf("xpath: ")+7,elementDescription.length()-1);
        return elementDescription;*/

        return (String)((JavascriptExecutor)webDriver).executeScript(
                "gPt=function(c){if(c.id!=='')" +
                        "{return'id(\"'+c.id+'\")'}" +
                        "if(c===document.body){return c.tagName}" +
                        "var a=0;" +
                        "var e=c.parentNode.childNodes;" +
                        "for(var b=0;b<e.length;b++)" +
                        "{var d=e[b];if(d===c)" +
                        "{return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}" +
                        "if(d.nodeType===1&&d.tagName===c.tagName){a++}}};" +
                        "return gPt(arguments[0]).toLowerCase();",
                webDriver.findElement(By.cssSelector(element.getElement().cssSelector())).toString());
    }

    public static String getNameLocator(WebElement element){
        return element.getElement().attr("name");
    }

    public static String getTagNameLocator(WebElement element){
        return element.getElement().tagName();
    }

    public static String getLinkTextLocator(WebElement element){
        return element.getElement().attr("href");
    }

    public static String getPartialLinkTextLocator(WebElement element){
        return element.getElement().attr("href");
    }

    public static String getIdLocator(WebElement element){
        return element.getElement().attr("id");
    }
}
