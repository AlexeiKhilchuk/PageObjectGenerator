package by.bsuir.pogen.utils;

import java.io.*;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class IO {
    public static boolean isPageReadable(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            return false;
        }
        String str;
        try {
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
