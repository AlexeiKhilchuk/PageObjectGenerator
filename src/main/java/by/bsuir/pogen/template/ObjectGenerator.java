package by.bsuir.pogen.template;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.models.WebElement;

/**
 * Created by alexei.khilchuk on 08/04/2017.
 */
public class ObjectGenerator {
    Constants.ProgrammingLanguage currentLanguage;

    public ObjectGenerator(Constants.ProgrammingLanguage language){
        currentLanguage = language;
    }

    public String getObjectDeclaration(WebElement element){
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
        return objectDeclaration;
    }

    public String getFindElementBy(WebElement element){
        return getFindElementBy(element, element.getPreferredLocatorType());
    }

    public String getFindElementBy(WebElement element, Constants.LocatorType locatorType){
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
        switch (currentLanguage){
            case JAVA:{
                objectDeclaration =  String.format("    WebElement %1s = driver.%2s;", element.getElementName(), getFindElementBy(element));
                break;
            }
            case C_SHARP:{
                objectDeclaration =  String.format("    RemoteWebElement %1s = driver.%2s;", element.getElementName(), getFindElementBy(element));
                break;
            }
        }
        return objectDeclaration;
    }
}
