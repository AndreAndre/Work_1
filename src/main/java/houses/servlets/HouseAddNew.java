package houses.servlets;

import entities.HousesEntity;
import utils.EntityUtilsImpl2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fedyu on 21.11.2016.
 */
@WebServlet(name = "HouseAddNew", urlPatterns = {"/houseAddNew"})
public class HouseAddNew extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();
        entityUtils.openSession();

        String paramAddress = request.getParameter("address");
        int paramFloors =  Integer.parseInt(request.getParameter("floors"));
        Date paramBuildDate = Date.valueOf(request.getParameter("buildDate"));
        //Вызываем метод добавления новой записи в БД
        HousesEntity newHouse = new HousesEntity(paramAddress, paramFloors, paramBuildDate);
        entityUtils.update(newHouse);

        response.sendRedirect(response.encodeRedirectURL("/houseList"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
