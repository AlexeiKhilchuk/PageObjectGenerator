package by.bsuir.pogen.models;

import org.jsoup.nodes.Element;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

/**
 * Created by Alexei Khilchuk on 17.07.2018.
 */
public class WebElementNode<T> extends DefaultMutableTreeNode implements Serializable {

    private static final long serialVersionUID = -4298474751201349156L;

    public WebElementNode(WebElement rootData) {
        super(rootData);
        if (rootData.getElement().children().size() > 0) {
            for (Element element : rootData.getElement().children()) {
                super.add(new WebElementNode<T>(new WebElement(element)));
            }
        }
    }


}
