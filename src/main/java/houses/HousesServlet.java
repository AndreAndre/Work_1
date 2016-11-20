package houses;

import entities.HousesEntity;
import utils.EntityUtilsImpl2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 20.11.2016.
 */
@WebServlet(name = "HousesServlet", urlPatterns = "/houseList")
public class HousesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();
        entityUtils.openSession();
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Это заголовок H1, вашу мать!!!");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
        entityUtils.closeSession();
    }
}
