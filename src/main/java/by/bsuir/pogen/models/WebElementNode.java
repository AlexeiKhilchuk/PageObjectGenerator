package by.bsuir.pogen.models;

import org.jsoup.nodes.Element;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

/**
 * Created by alexei.khilchuk on 01/04/2017.
 */
public class WebElementNode<T> extends DefaultMutableTreeNode implements Serializable {

    private static final long serialVersionUID = -4298474751201349156L;

    public WebElementNode(WebElement rootData){
        super(rootData);
        if (rootData.getElement().children().size() > 0){
            for (Element element : rootData.getElement().children()){
                super.add(new WebElementNode<T>(new WebElement(element)));
            }
        }
    }


}
