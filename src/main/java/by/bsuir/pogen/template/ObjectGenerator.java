package by.bsuir.pogen.template;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.models.WebElement;

/**
 * Created by Alexei Khilchuk on 15.08.2018.
 */
public class ObjectGenerator {
    Constants.ProgrammingLanguage currentLanguage;

    ObjectGenerator(Constants.ProgrammingLanguage language){
        currentLanguage = language;
    }

    String getObjectDeclaration(WebElement element, boolean isAndroid){
        String objectDeclaration = generateObjectDeclaration(element);

        if (element.getMultipleElements()){
            switch (currentLanguage){
                case JAVA:{
                    objectDeclaration =  objectDeclaration.replace("WebElement", "List<WebElement>");
                    objectDeclaration =  objectDeclaration.replace("findElement", "findElements");
                    break;
                }
                case C_SHARP:{
                    objectDeclaration =  objectDeclaration.replace("RemoteWebElement", "List<RemoteWebElement>");
                    objectDeclaration =  objectDeclaration.replace("FindElement", "FindElements");
                    break;
                }
            }
        }
        if (isAndroid)
        {
            objectDeclaration = objectDeclaration.replace("WebElement", "MobileElement");
        }
        return objectDeclaration;
    }

    private String getFindElementBy(WebElement element){
        return getFindElementBy(element, element.getPreferredLocatorType());
    }

    private String getFindElementBy(WebElement element, Constants.LocatorType locatorType){
        String findBy = null;
        switch (currentLanguage){
            case JAVA:{
                switch (locatorType){
                    case XPATH:{
                        findBy = "findElement(By.xpath(\"%s\"))";
                        break;
                    }
                    case CSS:{
                        findBy = "findElement(By.cssSelector(\"%s\"))";
                        break;
                    }
                    case TAG_NAME:{
                        findBy = "findElement(By.tagName(\"%s\"))";
                        break;
                    }
                    case LINK_TEXT:{
                        findBy = "findElement(By.linkText(\"%s\"))";
                        break;
                    }
                    case CLASS_NAME:{
                        findBy = "findElement(By.className(\"%s\"))";
                        break;
                    }
                    case NAME:{
                        findBy = "findElement(By.name(\"%s\"))";
                        break;
                    }
                    case ID:{
                        findBy = "findElement(By.id(\"%s\"))";
                        break;
                    }
                }
                break;
            }
            case C_SHARP:{
                switch (locatorType){
                    case XPATH:{
                        findBy = "FindElement(By.XPath(\"%s\"))";
                        break;
                    }
                    case CSS:{
                        findBy = "FindElement(By.CssSelector(\"%s\"))";
                        break;
                    }
                    case TAG_NAME:{
                        findBy = "FindElement(By.TagName(\"%s\"))";
                        break;
                    }
                    case LINK_TEXT:{
                        findBy = "FindElement(By.LinkText(\"%s\"))";
                        break;
                    }
                    case CLASS_NAME:{
                        findBy = "FindElement(By.ClassName(\"%s\"))";
                        break;
                    }
                    case NAME:{
                        findBy = "FindElement(By.Name(\"%s\"))";
                        break;
                    }
                    case ID:{
                        findBy = "FindElement(By.Id(\"%s\"))";
                        break;
                    }
                }
                break;
            }
        }
        findBy = String.format(findBy, element.getLocatorValueByType(locatorType));
        return findBy;
    }

    private String generateObjectDeclaration(WebElement element){
        String objectDeclaration = null;
        String elementName = element.getElementName();
        switch (currentLanguage){
            case JAVA:{
                objectDeclaration =  String.format("    WebElement %1s = driver.%2s;", elementName, getFindElementBy(element));
                break;
            }
            case C_SHARP:{
                objectDeclaration =  String.format("    RemoteWebElement %1s = driver.%2s;", elementName, getFindElementBy(element));
                break;
            }
        }
        return objectDeclaration;
    }
}
