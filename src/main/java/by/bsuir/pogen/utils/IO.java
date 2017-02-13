package by.bsuir.pogen.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class IO {
    public static String getFileSource(File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        while ((str = in.readLine()) != null) {
            contentBuilder.append(str);
        }
        in.close();

        return contentBuilder.toString();
    }
}
