package by.bsuir.pogen.pages;
 
import java.util.List;
 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
 
public class @Raw(@Model.PageObject.PageObjectName) extends MyBasePage {
    @foreach (var element in @Model.PageObject.Items ) 
    {
        if(element.ReturnsCollection) 
        {        
        <text>
            @@FindBy(how=How.@Raw(@JavaHow(@element.How)), using="@Raw(@QuoteLocator(@element.Locator))")
            private List<WebElement> @Raw(@element.Name);
        </text>
        }
        else 
        {
        <text>
            @@FindBy(how=How.@Raw(@JavaHow(@element.How)), using="@Raw(@QuoteLocator(@element.Locator))")
            private WebElement @Raw(@element.Name);
        </text>
        }
    }
}
