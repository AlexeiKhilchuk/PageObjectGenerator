package by.bsuir.pogen.models;

import org.jsoup.nodes.Element;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by alexei.khilchuk on 01/04/2017.
 */
public class WebElementNode<T> extends DefaultMutableTreeNode /*implements Iterable<WebElementNode<T>> */{
    /*WebElement data;
    WebElementNode<T> parent;
    List<WebElementNode<T>> children;*/

    public WebElementNode(WebElement rootData){
        super(rootData);

//        this.data = rootData;
//        this.children = new LinkedList<WebElementNode<T>>();

        if (rootData.getElement().children().size() > 0){
            for (Element element : rootData.getElement().children()){
                super.add(new WebElementNode<T>(new WebElement(element)));
               // this.addChild(new WebElement(element));
            }
        }
    }

   /* public WebElementNode<T> addChild(WebElement child) {
        WebElementNode<T> childNode = new WebElementNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }*/

//    @Override
//    public String toString() {
//        return data != null ? data.toString() : "[data null]";
//    }
//
//    @Override
//    public Iterator<WebElementNode<T>> iterator() {
//        WebElementNodeIterator iterator = new WebElementNodeIterator( this);
//        return iterator;
//    }
}
