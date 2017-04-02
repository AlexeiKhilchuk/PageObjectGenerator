package by.bsuir.pogen.models;
import by.bsuir.pogen.constants.Constants;
import org.jsoup.nodes.Element;

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

    public WebElement(Element elem){
        this.element = elem;
        this.elementName = generateElementName();
        this.preferredLocatorType = Constants.LocatorType.XPATH;
        this.isForGeneration = false;
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

    private String generateElementName(){
        String resultName = this.element.tagName();

        if (!this.element.attr("name").equals("")) {
            resultName += ("_" + this.element.attr("name"));
        }
        else if (!this.element.attr("id").equals("")){
            resultName += ("_" + this.element.attr("id"));
        }
        else if (!this.element.attr("class").equals("")){
            resultName += ("_" + this.element.attr("class"));
        }
        else if (!this.element.attr("href").equals("")){
            resultName += ("_" + this.element.attr("href"));
        }


        resultName = resultName.replaceAll(" ", "_");
        resultName = resultName.replaceAll("-", "_");
        resultName = resultName.replaceAll("\\.", "");
        resultName = resultName.replaceAll("http://", "");
        resultName = resultName.replaceAll("https://", "");
        resultName = resultName.replaceAll("/", "");
        resultName = resultName.replaceAll("#","");

        return resultName;
    }

    @Override
    public String toString() {
        return isForGeneration ? "[r] " + elementName : elementName;
   }
}
