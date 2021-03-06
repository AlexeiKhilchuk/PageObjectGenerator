package by.bsuir.pogen.template;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.models.WebElement;
import by.bsuir.pogen.utils.LocatorBuilder;

/**
 * Created by Alexei Khilchuk on 15.08.2018.
 */
public class MethodsGenerator {
    Constants.ProgrammingLanguage currentLanguage;

    MethodsGenerator(Constants.ProgrammingLanguage language){
        currentLanguage = language;
    }

    String generateMethodsForElement(WebElement element){
        StringBuilder sb = new StringBuilder("");
        String tag = element.getTagNameLocator() == null ? new LocatorBuilder().getTagNameLocator(element) : element.getTagNameLocator();
        if (tag.equals("button") || tag.toLowerCase().contains("button")){
            sb.append(getClickMethod(element)).append('\n');
            sb.append(getGetTextMethod(element)).append('\n');
        }
        else if (tag.equals("label") || tag.toLowerCase().contains("textview")){
            sb.append(getClickMethod(element)).append('\n');
            sb.append(getGetTextMethod(element)).append('\n');
        }
        else if (tag.equals("input") || tag.toLowerCase().contains("edittext")){
            sb.append(getClearMethod(element)).append('\n');
            sb.append(getGetTextMethod(element)).append('\n');
            sb.append(getSetValueMethod(element)).append('\n');

        }
        else if (tag.equals("ul") || tag.equals("ol")){
            sb.append(getLiUlValuesMethod(element)).append('\n');
        }
        else if (tag.equals("select")){
            sb.append(getSelectOptionMethod(element)).append('\n');
        }
        if (!element.getElement().attr("href").equals(null) && !element.getElement().attr("href").equals("")){
            sb.append(getClickMethod(element)).append('\n');
        }

        return sb.toString();
    }

    private String getClickMethod(WebElement element){
        StringBuilder sb = new StringBuilder("");
        switch (currentLanguage){
            case JAVA:{
                sb.append(String.format("    public void click_%s(){ \n", element.getElementName()));
                sb.append(String.format("        %s.click(); \n    }", element.getElementName()));
                break;
            }
            case C_SHARP:{
                sb.append(String.format("    public void Click_%s(){ \n", element.getElementName()));
                sb.append(String.format("            %s.Click(); \n        }", element.getElementName()));
                break;
            }
        }
        return sb.toString();
    }

    private String getSetValueMethod(WebElement element){
        StringBuilder sb = new StringBuilder("");
        switch (currentLanguage){
            case JAVA:{
                sb.append(String.format("    public void setValue_%s(String value){ \n", element.getElementName()));
                sb.append(String.format("        %s.sendKeys(value); \n    }", element.getElementName()));
                break;
            }
            case C_SHARP:{
                sb.append(String.format("    public void SetValue_%s(String value){ \n", element.getElementName()));
                sb.append(String.format("            %s.SendKeys(value); \n        }", element.getElementName()));
                break;
            }
        }
        return sb.toString();
    }

    private String getClearMethod(WebElement element){
        StringBuilder sb = new StringBuilder("");
        switch (currentLanguage){
            case JAVA:{
                sb.append(String.format("    public void clear_%s(){ \n", element.getElementName()));
                sb.append(String.format("        %s.clear(); \n    }", element.getElementName()));
                break;
            }
            case C_SHARP:{
                sb.append(String.format("    public void Clear_%s(){ \n", element.getElementName()));
                sb.append(String.format("            %s.Clear(); \n        }", element.getElementName()));
                break;
            }
        }
        return sb.toString();
    }

    private String getGetTextMethod(WebElement element){
        StringBuilder sb = new StringBuilder("");
        switch (currentLanguage){
            case JAVA:{
                sb.append(String.format("    public String getText_%s(){ \n", element.getElementName()));
                sb.append(String.format("            return %s.getText(); \n        }", element.getElementName()));
                break;
            }
            case C_SHARP:{
                sb.append(String.format("    public string GetText_%s(){ \n", element.getElementName()));
                sb.append(String.format("            return %s.Text; \n        }", element.getElementName()));
                break;
            }
        }
        return sb.toString();
    }

    private String getLiUlValuesMethod(WebElement element){
        StringBuilder sb = new StringBuilder("");
        switch (currentLanguage){
            case JAVA:{
                sb.append(String.format("    public List<String> getListValues_%s(){ \n", element.getElementName()));
                sb.append("        List<String> values = new ArrayList<String>(); ");
                sb.append(String.format("        for(WebElement element: %s.findElements(By.xpath(\"/li\"))){ \n", element.getElementName()));
                sb.append("             values.add(element.getText()); \n");
                sb.append("        }");
                sb.append("        return values \n    }");
                break;
            }
            case C_SHARP:{
                sb.append(String.format("    public List<String> GetListValues_%s(){ \n", element.getElementName()));
                sb.append("            List<string> values = new List<string>(); ");
                sb.append(String.format("            foreach (var element in %s.FindElements(By.XPath(\"/li\"))){ \n", element.getElementName()));
                sb.append("                 values.Add(element.text); \n");
                sb.append("            }");
                sb.append("            return values \n        }");
                break;
            }
        }
        return sb.toString();
    }

    private String getSelectOptionMethod(WebElement element){
        StringBuilder sb = new StringBuilder("");
        switch (currentLanguage){
            case JAVA:{
                sb.append(String.format("    public List<String> selectOption_%s(int option){ \n", element.getElementName()));
                sb.append(String.format("        List<WebElement> elements = %s.findElements(By.xpath(\"/option\")); \n", element.getElementName()));
                sb.append("        elements.get(option).setValue(true); \n    }");
                break;
            }
            case C_SHARP:{
                sb.append(String.format("    public List<String> SelectOption_%s(int option){ \n", element.getElementName()));
                sb.append(String.format("            List<RemoteWebElement> elements = %s.FindElements(By.XPath(\"/option\")); \n", element.getElementName()));
                sb.append("            elements.Get(option).SetValue(true); \n        }");
                break;
            }
        }
        return sb.toString();
    }
}
