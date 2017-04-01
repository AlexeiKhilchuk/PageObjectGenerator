package by.bsuir.pogen.models;
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
    private String partialLinkTextLocator;
    private String xpathLocator;
    private String cssLocator;
    private String idLocator;

    public WebElement(Element elem){
        this.element = elem;
        this.elementName = generateElementName();
    }

    public String getElementName() {
        return elementName;
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

    public String getPartialLinkTextLocator() {
        return partialLinkTextLocator;
    }

    public void setPartialLinkTextLocator(String partialLinkTextLocator) {
        this.partialLinkTextLocator = partialLinkTextLocator;
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

    private String generateElementName(){
        String resultName = this.element.tagName();

        if (!this.element.attr("name").equals("")) {
            resultName += ("_" + this.element.attr("name"));
        }
        else if (!this.element.attr("href").equals("")){
            resultName += ("_" + this.element.attr("href").replace("#",""));
        }
        else if (!this.element.attr("id").equals("")){
            resultName += ("_" + this.element.attr("id"));
        }
        else if (!this.element.attr("class").equals("")){
            resultName += ("_" + this.element.attr("class"));
        }

        resultName = resultName.replace(" ", "_");
        resultName = resultName.replace("-", "_");

        return resultName;
    }

    @Override
    public String toString() {
        return elementName;
   }
}
