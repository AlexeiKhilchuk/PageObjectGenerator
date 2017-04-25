package by.bsuir.pogen.models;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.Serializable;

/**
 * Created by alexei.khilchuk on 09/04/2017.
 */
public class MyElement extends Element implements Serializable{
    public MyElement(String tag) {
        super(tag);
    }

    public MyElement(Tag tag, String baseUri, Attributes attributes) {
        super(tag, baseUri, attributes);
    }

    public MyElement(Tag tag, String baseUri) {
        super(tag, baseUri);
    }



}
