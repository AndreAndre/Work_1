package houses.servlets;

import entities.HousesEntity;
import utils.EntityUtilsImpl2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fedyu on 21.11.2016.
 */
@WebServlet(name = "HouseRemove", urlPatterns = {"/houseRemove", "/house/remove"})
public class HouseRemove extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        
        EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();
        entityUtils.openSession();
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("paramDelete = " + id);
        entityUtils.remove(HousesEntity.class,id);
        response.sendRedirect(response.encodeRedirectURL("/houseList"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
