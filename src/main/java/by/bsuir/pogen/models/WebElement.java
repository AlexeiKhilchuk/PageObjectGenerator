package by.bsuir.pogen.models;
import by.bsuir.pogen.constants.Constants;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static by.bsuir.pogen.constants.Constants.LocatorType.XPATH;

/**
 * Created by alexei.khilchuk on 31/03/2017.
 */
public class WebElement {
    private Element element;

    private String elementName;

    private String nameLocator;
    private String tagNameLocator;
    private String linkTextLocator;
    private String xpathLocator;
    private String cssLocator;
    private String idLocator;
    private String classNameLocator;

    private Constants.LocatorType preferredLocatorType;
    private Boolean isForGeneration;

    private Boolean isMultipleElements;

    public WebElement(Element elem){
        this.element = elem;
        this.elementName = generateElementName();
        this.preferredLocatorType = XPATH;
        this.isForGeneration = false;
        this.isMultipleElements = false;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Element getElement() {
        return element;
    }

    public String getNameLocator() {
        return nameLocator;
    }

    public void setNameLocator(String nameLocator) {
        this.nameLocator = nameLocator;
    }

    public String getTagNameLocator() {
        return tagNameLocator;
    }

    public void setTagNameLocator(String tagNameLocator) {
        this.tagNameLocator = tagNameLocator;
    }

    public String getLinkTextLocator() {
        return linkTextLocator;
    }

    public void setLinkTextLocator(String linkTextLocator) {
        this.linkTextLocator = linkTextLocator;
    }

    public String getXpathLocator() {
        return xpathLocator;
    }

    public void setXpathLocator(String xpathLocator) {
        this.xpathLocator = xpathLocator;
    }

    public String getCssLocator() {
        return cssLocator;
    }

    public void setCssLocator(String cssLocator) {
        this.cssLocator = cssLocator;
    }

    public String getIdLocator() {
        return idLocator;
    }

    public void setIdLocator(String idLocator) {
        this.idLocator = idLocator;
    }

    public String getClassNameLocator() {
        return classNameLocator;
    }

    public void setClassNameLocator(String classNameLocator) {
        this.classNameLocator = classNameLocator;
    }

    public Constants.LocatorType getPreferredLocatorType() {
        return preferredLocatorType;
    }

    public void setPreferredLocatorType(Constants.LocatorType preferredLocatorType) {
        this.preferredLocatorType = preferredLocatorType;
    }

    public Boolean isForGeneration() {
        return isForGeneration;
    }

    public void setForGeneration(Boolean forGeneration) {
        this.isForGeneration = forGeneration;
    }

    public Boolean getMultipleElements() {
        return isMultipleElements;
    }

    public void setMultipleElements(Boolean multipleElements) {
        isMultipleElements = multipleElements;
    }

    public String getLocatorValueByType(Constants.LocatorType type){
        String locatorValue;
        switch (type){
            case XPATH:{
                locatorValue = getXpathLocator();
                break;
            }
            case CSS:{
                locatorValue = getCssLocator();
                break;
            }
            case TAG_NAME:{
                locatorValue = getTagNameLocator();
                break;
            }
            case LINK_TEXT:{
                locatorValue = getLinkTextLocator();
                break;
            }
            case CLASS_NAME:{
                locatorValue = getClassNameLocator();
                break;
            }
            case NAME:{
                locatorValue = getNameLocator();
                break;
            }
            case ID:{
                locatorValue = getIdLocator();
                break;
            }
            default:{
                locatorValue = getXpathLocator();
                break;
            }
        }

        return locatorValue;
    }

    public String getPreferredLocatorValue(){
        return getLocatorValueByType(getPreferredLocatorType());
    }

    private String generateElementName(){
        //String resultName = this.element.tagName();
        String resultName = "";

        if (!this.element.attr("name").equals("")) {
            resultName += (this.element.attr("name"));
        }
        else if (!this.element.attr("id").equals("")){
            resultName += (this.element.attr("id"));
        }
        else if (!this.element.attr("resource-id").equals("")){
            resultName += (this.element.attr("resource-id"));
        }
        else if (!this.element.tagName().equals(""))
        {
            resultName += this.element.tagName();
        }
        else if (!this.element.attr("class").equals("")){
            resultName += (this.element.attr("class"));
        }
        else if (!this.element.attr("href").equals("")){
            resultName += (this.element.attr("href"));
        }

        resultName = resultName.replaceAll(" ", "_");
        resultName = resultName.replaceAll("-", "_");
      //  resultName = resultName.replaceAll("\\.", "");
       resultName = resultName.replaceAll("http://", "");
        resultName = resultName.replaceAll("https://", "");
       // resultName = resultName.replaceAll("/", "");
        resultName = resultName.replaceAll("#","");

        return resultName;
    }

    @Override
    public String toString() {
        return isForGeneration ? "[r] " + elementName : elementName;
   }
}
