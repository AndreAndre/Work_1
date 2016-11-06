package utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Andre on 06.11.2016.
 */
public class TextUtils {

    public static void println(HttpServletResponse response, String ... values) throws IOException {

        PrintWriter htmlPage = response.getWriter();

        for (String s : values) {
            htmlPage.println(s);
        }
    }

    public static void println(String ... values) throws IOException {

        for (String s : values) {
            System.out.println(s);
        }
    }

}
