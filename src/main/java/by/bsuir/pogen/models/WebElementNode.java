package by.bsuir.pogen.models;

import org.jsoup.nodes.Element;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by alexei.khilchuk on 01/04/2017.
 */
public class WebElementNode<T> extends DefaultMutableTreeNode{
    public WebElementNode(WebElement rootData){
        super(rootData);
        if (rootData.getElement().children().size() > 0){
            for (Element element : rootData.getElement().children()){
                super.add(new WebElementNode<T>(new WebElement(element)));
            }
        }
    }
}
