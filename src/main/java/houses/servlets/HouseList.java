//TODO: нормальную работу с ссылками вида /house/list
package houses.servlets;

import entities.HousesEntity;
import utils.EntityUtilsImpl2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by fedyu on 20.11.2016.
 */
@WebServlet(name = "HouseList", urlPatterns = {"/house/list", "/house/editable", "/house/editRow"})
public class HouseList extends HttpServlet {
    EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        entityUtils.openSession();

        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        //Определяем действия
        if (request.getServletPath().equals("/house/list")) {
            rHousesTable(request, response);
        } else if (request.getServletPath().equals("/house/editRow")) {
            System.out.println("Получили запрос на редактирование");
            rHouseEditRow(request, response);
        } else if (request.getServletPath().equals("/house/editable")) {
            System.out.println("Открыть таблицу на редактирование");
            rHousesEditableTable(request, response);
        }
        //entityUtils.closeSession();
    }

    private void rHouseEditRow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op", "editableRow");
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на редактирование.");
        request.getRequestDispatcher("../WEB-INF/houses/index.jsp").forward(request,response);
    }


    private void rHousesTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op", "view");
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на просмотр.");
        request.getRequestDispatcher("../WEB-INF/houses/index.jsp").forward(request,response);
    }

    private void rHousesEditableTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op", "editableTable");
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на  редактирование.");
        String fullPath = "../WEB-INF/houses/index.jsp";
        System.out.println(fullPath);

        request.getRequestDispatcher(fullPath).forward(request,response);
    }
}
