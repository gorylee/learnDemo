package example.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author GoryLee
 * @Date 2022/12/5 00:31
 * @Version 1.0
 */
public class WebUtil {

    public static String renderString(HttpServletResponse response, String data){
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
