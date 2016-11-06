package views;

import entities.ApartmentsEntity;
import entities.HousesEntity;
import utils.EntityUtilsImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import static utils.TextUtils.println;

/**
 * Created by Andre on 06.11.2016.
 */
    @WebServlet ("/apartments")
public class ApartmentView extends HttpServlet {

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{

            response.setContentType("text/html; charset=utf-8");

            String paramView = request.getParameter("view");
            if(paramView == null || paramView.isEmpty()){
                printApartmentTable(request, response);
            }
            else {
                switch(paramView) {
                    case "add":
                        printAddNewApartment(request, response);
                        break;
                    default:
                        printApartmentTable(request, response);
                        break;
                }
            }
        }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        doGet(request, response);
    }

    public void printApartmentTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int houseId = Integer.parseInt(request.getParameter("houseId")); //получаем ID дома
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, houseId);
        List<ApartmentsEntity> apartments = house.getApartmentsEntity();


        println(response,
                "<h1>Квартиры дома \"" + house.getAddress() + "\" </h1>",
                "<table cellspacing border cellpadding=\"2\" " +
                "align=\"center\" bgcolor=\"#50ff50\" cols=\"5\"  width=\"960\" ",
                "<tr>",
                "<td>Номер квартиры</td>",
                "<td>Площадь</td>",
                "<td>Этаж</td>",
                "</tr>");
        for (ApartmentsEntity apartment : apartments){
            println(response,
                    "<tr>",
                    "<td>" + apartment.getApartmentNumber() + "</td>",
                    "<td>" + apartment.getSquare() + "</td>",
                    "<td>" + apartment.getFloor() + "</td>",
                    "</tr>");
        }
        println(response,
                "</table>",
                "<h2>вот така хуйня, малята...<h2>",
                "<h2>чтоб таблица заполнилась, нужно как-то дом выбрать</h2>");
            }

    public void printAddNewApartment(HttpServletRequest request, HttpServletResponse response){

    }
}
