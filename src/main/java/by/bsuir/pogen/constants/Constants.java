package by.bsuir.pogen.constants;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class Constants {
    public enum ProgrammingLanguage {JAVA, C_SHARP}
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

}
