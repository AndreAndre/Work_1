package views;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import static utils.TextUtils.println;

/**
 * Created by Andre on 06.11.2016.
 */
    @WebServlet ("/apartments")
public class ApartmentView extends HttpServlet {

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
            response.setContentType("text/html; charset=utf-8");

            Enumeration<String> params = request.getParameterNames();

            while (params.hasMoreElements()) {
                System.out.println("параметр " + params.nextElement());
            }

            String paramView = request.getParameter("view");
            if(paramView == null || paramView.isEmpty()){
                println(response,
                        "<h1>На этой странице отображаются квартиры</h1>" +
                        "<a href=\"./apartments?view=list\">Дома (список)</a></br>");
            }
        }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        doGet(request, response);
    }

    public void printApartmentTable(HttpServletRequest req, HttpServletResponse res){

    }
}
