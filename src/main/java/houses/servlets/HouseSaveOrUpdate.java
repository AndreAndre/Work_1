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
@WebServlet(name = "HouseSaveOrUpdate", urlPatterns = {"/houseSaveOrUpdate"})
public class HouseSaveOrUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();
        entityUtils.openSession();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
