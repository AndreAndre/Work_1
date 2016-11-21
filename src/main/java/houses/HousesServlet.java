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
@WebServlet(name = "HousesServlet", urlPatterns = {"/houseList", "/editableHouseList", "/houseRemove", "/houseEdit", "/houseSaveOrUpdate", "/houseAddNew"})
public class HousesServlet extends HttpServlet {
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
        if (request.getServletPath().equals("/houseList")) {
            rHousesTable(request, response);
        } else if (request.getServletPath().equals("/houseRemove")) {
            System.out.println("Получили запрос на удаление");
            rHouseRemove(request, response);
        } else if (request.getServletPath().equals("/houseEdit")) {
            System.out.println("Получили запрос на редактирование");
            rHouseEditRow(request, response);
        } else if (request.getServletPath().equals("/editableHouseList")) {
            System.out.println("Открыть таблицу на редактирование");
            rHousesEditableTable(request, response);
        } else if (request.getServletPath().equals("/houseSaveOrUpdate")) {
            System.out.println("Приняли запрос на изменение данных");
            rHouseSaveOrUpdate(request, response);
        } else if (request.getServletPath().equals("/houseAddNew")) {
            System.out.println("Добавляем новую строку");
            rHouseAddNew(request, response);
        }

        //entityUtils.closeSession();
    }

    private void rHouseEditRow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op", "editableRow");
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на редактирование.");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
    }

    private void rHouseAddNew(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Получаем параметры из запроса
        String paramAddress = request.getParameter("address");
        int paramFloors =  Integer.parseInt(request.getParameter("floors"));
        Date paramBuildDate = Date.valueOf(request.getParameter("buildDate"));
        //Вызываем метод добавления новой записи в БД
        HousesEntity newHouse = new HousesEntity(paramAddress, paramFloors, paramBuildDate);
        entityUtils.add(newHouse);
        response.sendRedirect(response.encodeRedirectURL("/houseList"));
    }

    private void rHouseSaveOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        response.sendRedirect(response.encodeRedirectURL("/houseList"));
    }

    private void rHouseRemove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("paramDelete = " + id);
        entityUtils.remove(HousesEntity.class,id);
        response.sendRedirect(response.encodeRedirectURL("/houseList"));
    }

    private void rHousesTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op", "view");
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на просмотр.");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
    }

    private void rHousesEditableTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("op", "editableTable");
        request.setAttribute("houses", entityUtils.listHouse());
        request.setAttribute("text_h1","Список домов на редактирование.");
        request.getRequestDispatcher("WEB-INF/houses/index.jsp").forward(request,response);
    }
}
