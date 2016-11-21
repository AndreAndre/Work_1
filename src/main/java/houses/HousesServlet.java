package houses;

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
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by fedyu on 20.11.2016.
 */
@WebServlet(name = "HousesServlet", urlPatterns = "/houseList")
public class HousesServlet extends HttpServlet {
    EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        entityUtils.openSession();

        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        //Параметр view передаваемый в request - отвечает за функции страницы
        String pOperation = request.getParameter("op");
        //Если параметров нет, или равен "view"  - отображаем по умолчанию таблицу домов
        if (pOperation == null || pOperation.isEmpty() || pOperation == "view")
            rHousesTable(request, response);
            //Иначе проверяем значение параметра и делаем что-нибудь соответственно
        else {
            switch (pOperation) {
                //Удаление записи
                case "deleteRow":
                    rDeleteRow(request, response);
                    break;
                //Редактирование строки
                case "editableRow":
                    rEditableRow(request, response);
                    break;
                //Редактирование таблицы
                case "editableTable":
                    rEditableTable(request, response);
                    break;
                //Приход команды на изменение данных
                case "saveOrUpdate":
                    rSaveOrUpdate(request, response);
                    break;
                default:
                    rHousesTable(request, response);
            }
        }


        entityUtils.closeSession();
    }

    private void rSaveOrUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String[]> params = request.getParameterMap();
        String[] houseIDs = params.get("houseID");
        String[] address = params.get("address");
        String[] floors = params.get("floors");
        String[] builDates = params.get("buildDate");
        List<Object> editableHouses = new ArrayList<Object>();
        System.out.println("houseIDs cnt: " + houseIDs.length);
        for (int i = 0; i < houseIDs.length; i++) {
            HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, Integer.parseInt(houseIDs[i]));
            if (house != null) {
                house.setAddress(address[i]);
                house.setFloors(Integer.parseInt(floors[i]));
                house.setBuildDate(Date.valueOf(builDates[i]));
                entityUtils.update(house);
                //editableHouses.add(house);
            }
        }
        rHousesTable(request, response);//Перенаправдяем на отрисовку
    }

    private void rEditableTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op","editableTable");
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Редактирование таблицы: ");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
    }

    private void rEditableRow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op","editableRow");
        request.setAttribute("id",request.getParameter("id"));
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Редактирование записи: ");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
    }

    private void rDeleteRow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("Delete id = " + id);
        entityUtils.remove(HousesEntity.class,id);
        rHousesTable(request, response);//Перенаправдяем на отрисовку
    }

    private void rHousesTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<String> params =  request.getParameterNames();
        while (params.hasMoreElements()) {
            request.removeAttribute(params.nextElement());
        }
        request.setAttribute("op", "view");
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на просмотр.");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
    }
}
