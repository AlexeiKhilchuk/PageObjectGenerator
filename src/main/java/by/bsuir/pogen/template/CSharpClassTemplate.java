package by.bsuir.pogen.template;

import by.bsuir.pogen.constants.Constants;
import by.bsuir.pogen.models.WebElement;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by alexei.khilchuk on 09/04/2017.
 */
public class CSharpClassTemplate extends Template {

    private Collection<String> elementCollection = new ArrayList<String>();
    private Collection<String> methodsCollection = new ArrayList<String>();

    public CSharpClassTemplate(String name, List<WebElement> elements, boolean isGenerateMethods, boolean isAndroid){
        ObjectGenerator objectGenerator = new ObjectGenerator(Constants.ProgrammingLanguage.C_SHARP);
        MethodsGenerator methodsGenerator = new MethodsGenerator(Constants.ProgrammingLanguage.C_SHARP);
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
        return "csharp_template.cs";
    }
}
