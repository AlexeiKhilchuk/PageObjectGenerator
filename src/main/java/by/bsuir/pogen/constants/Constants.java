package by.bsuir.pogen.constants;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class Constants {
    public enum ProgrammingLanguage {
        JAVA("Java"),
        C_SHARP("C#");

        private final String name;
        private ProgrammingLanguage(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }
    }

    public enum LoadStatus {
        LOADED("LOADED"),
        NOT_LOADED("NOT LOADED");

        private final String name;
        private LoadStatus(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }
    }

    public enum BrowserType {CHROME, FIREFOX}

    public enum LocatorType {
        NAME("Name locator"),
        ID("Id locator"),
        CLASS_NAME("Class Name locator"),
        TAG_NAME("Tag Name locator"),
        LINK_TEXT("Link Text locator"),
        CSS("CSS locator"),
        XPATH("XPath locator");

        private final String name;
        private LocatorType(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }
    }

    public static final String SCRIPT_GET_ELEMENT_BORDER =
        "var elem = arguments[0]; \n" +
        "if (elem.currentStyle) {\n" +
        "    var style = elem.currentStyle;\n" +
        "    var border = style['borderTopWidth']\n" +
    "            + ' ' + style['borderTopStyle']\n" +
    "            + ' ' + style['borderTopColor']\n" +
    "            + ';' + style['borderRightWidth']\n" +
    "            + ' ' + style['borderRightStyle']\n" +
    "            + ' ' + style['borderRightColor']\n" +
    "            + ';' + style['borderBottomWidth']\n" +
    "            + ' ' + style['borderBottomStyle']\n" +
    "            + ' ' + style['borderBottomColor']\n" +
    "            + ';' + style['borderLeftWidth']\n" +
    "            + ' ' + style['borderLeftStyle']\n" +
    "            + ' ' + style['borderLeftColor'];\n" +
        "} else if (window.getComputedStyle) {\n" +
        "    var style = document.defaultView.getComputedStyle(elem);\n" +
        "    var border = style.getPropertyValue('border-top-width')\n" +
    "            + ' ' + style.getPropertyValue('border-top-style')\n" +
    "            + ' ' + style.getPropertyValue('border-top-color')\n" +
    "            + ';' + style.getPropertyValue('border-right-width')\n" +
    "            + ' ' + style.getPropertyValue('border-right-style')\n" +
    "            + ' ' + style.getPropertyValue('border-right-color')\n" +
    "            + ';' + style.getPropertyValue('border-bottom-width')\n" +
    "            + ' ' + style.getPropertyValue('border-bottom-style')\n" +
    "            + ' ' + style.getPropertyValue('border-bottom-color')\n" +
    "            + ';' + style.getPropertyValue('border-left-width')\n" +
    "            + ' ' + style.getPropertyValue('border-left-style')\n" +
    "            + ' ' + style.getPropertyValue('border-left-color');\n" +
        "}\n" +
        "// highlight the element\n" +
        "elem.style.border = '2px solid red';\n" +
        "return border;";

    public static final String SCRIPT_UNHIGHLIGHT_ELEMENT =
        "var elem = arguments[0];\n" +
        "var borders = arguments[1].split(';');\n" +
        "elem.style.borderTop = borders[0];\n" +
        "elem.style.borderRight = borders[1];\n" +
        "elem.style.borderBottom = borders[2];\n" +
        "elem.style.borderLeft = borders[3];";

    public static final String GENERATION_IN_PROGRESS = " - Locator generation in progress...";
}
