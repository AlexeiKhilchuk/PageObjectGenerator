package by.bsuir.pogen.template;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.models.WebElement;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Alexei Khilchuk on 15.08.2018.
 */
public class JavaClassTemplate extends Template {

    private Collection<String> elementCollection = new ArrayList<String>();
    private Collection<String> methodsCollection = new ArrayList<String>();

    public JavaClassTemplate(String name, List<WebElement> elements, boolean isGenerateMethods, boolean isAndroid){
        ObjectGenerator objectGenerator = new ObjectGenerator(Constants.ProgrammingLanguage.JAVA);
        MethodsGenerator methodsGenerator = new MethodsGenerator(Constants.ProgrammingLanguage.JAVA);
        for (WebElement element : elements){
            elementCollection.add(objectGenerator.getObjectDeclaration(element, isAndroid));
            if (isGenerateMethods && !element.getMultipleElements()){
                methodsCollection.add(methodsGenerator.generateMethodsForElement(element));
            }
        }

        add("pageName", name);
        addCollection("elementDeclarations", elementCollection);
        addCollection("methods", methodsCollection);
    }

    @Override
    protected String getFilePath() {
        return "java_template.java";
    }
}
