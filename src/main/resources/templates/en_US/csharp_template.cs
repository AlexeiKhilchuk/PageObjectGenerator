using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using OpenQA.Selenium.Support.PageObjects;
using OpenQA.Selenium;

namespace Bsuir.Pogen.Pages
{
    public class ~pageName~Page : MyPageBase
    {
        //Object Declarations:
    ~for elementDeclaration in elementDeclarations:
    ~elementDeclaration~
    :~

        //Action methods for declared objects:
    ~for method in methods:
    ~method~
    :~
    }
}
