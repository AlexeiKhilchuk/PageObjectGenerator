package by.bsuir.pogen.models;

import java.io.Serializable;
import java.util.List;

public class ExportModel implements Serializable {
    public WebElementNode getWebElementNode() {
        return webElementNode;
    }

    public void setWebElementNode(WebElementNode webElementNode) {
        this.webElementNode = webElementNode;
    }

    public List<WebElement> getUserObjects() {
        return userObjects;
    }

    public void setUserObjects(List<WebElement> userObjects) {
        this.userObjects = userObjects;
    }

    public WebElementNode webElementNode;
    public List<WebElement> userObjects;

    public ExportModel(WebElementNode webElementNode, List<WebElement> userObjects) {
        this.webElementNode = webElementNode;
        this.userObjects = userObjects;
    }
}
