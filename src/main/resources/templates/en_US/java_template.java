package by.bsuir.pogen.pages;
 
import java.util.List;
 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
 
public class ~pageName~Page extends BasePage {
    //Object Declarations:
~for elementDeclaration in elementDeclarations:
~elementDeclaration~
:~

    //Action methods for declared objects:
~for method in methods:
~method~
:~
}
