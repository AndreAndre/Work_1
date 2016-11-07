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
            println("<h2 align='center'>Укажите ID дома или перейдите к выбору адреса. beegom.</h2>");
            printApartmentOfNullHouse(request, response);

            //получаем параметры
            String paramView = request.getParameter("view");
            if(paramView == null || paramView.isEmpty()){
                printApartmentOfNullHouse(request, response);
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

    /**
     * Метод выводит таблицу со списком домов
     * В последнем строке -- форма для добавления квартиры
     * @param request
     * @param response
     */
    public void printApartmentTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int houseId;
        String paramHouseId = request.getParameter("houseId");

        if (paramHouseId.isEmpty() || paramHouseId == null) printApartmentOfNullHouse(request, response);
        else {
            houseId = Integer.parseInt(request.getParameter("houseId")); //получаем ID дома

            EntityUtilsImpl entityUtils = new EntityUtilsImpl();
            HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, houseId);
            List<ApartmentsEntity> apartments = house.getApartmentsEntity();


            println(response,
                    "<h1>Квартиры дома \"" + house.getAddress() + "\" </h1>",
                    "<table cellspacing border cellpadding=\"2\" " +
                            "align=\"left\" bgcolor=\"#50ff50\" cols=\"5\"  width=\"960\" ",
                    "<tr>",
                    "<td>Номер квартиры</td>",
                    "<td>Площадь</td>",
                    "<td>Этаж</td>",
                    "<td width='40'>Лицевые счета</td>",
                    "<td width='40'>Жильцы</td>",
                    "<td width='40'>Редактирование</td>",
                    "</tr>");
            for (ApartmentsEntity apartment : apartments) {
                println(response,
                        "<tr>",
                        "<td>" + apartment.getApartmentNumber() + "</td>",
                        "<td>" + apartment.getSquare() + "</td>",
                        "<td>" + apartment.getFloor() + "</td>",
                        "</tr>");
            }
            println(response,

                    "</table>"
            );

        }
    }
    /**
     * Метод вызывается при houseId == null
     * Отображает страницу с пустой таблицей
     * и предлагает выбрать дом
     * @throws IOException
     * */
    public void printApartmentOfNullHouse(HttpServletRequest request, HttpServletResponse response) throws IOException {



        println(response,
                "<h2 align='center'>Укажите ID дома или перейдите к выбору адреса. beegom.</h2>",
                "<form action='./apartments' method=GET>",
                "<tr height='40'>",
                "<td><input type='number' placeholder='Введите сюда ID дома.' min=0" +
                        " name='houseId' style='width:100%; height:40px; border:0'></td>",
                "<input type='hidden' name='houseId' value='houseId'>",
                "<td><input type='submit' value='Ввести ID'></td>",
                "</tr>",
                "</form>");

        //пустая таблица
        println(response,
                "<h1>Квартиры дома \"???\" </h1>",
                "<table cellspacing border cellpadding=\"2\" " +
                "align=\"left\" bgcolor=\"#50ff50\" cols=\"5\"  width=\"960\" ",
                "<tr>",
                "<td>Номер квартиры</td>",
                "<td>Площадь</td>",
                "<td>Этаж</td>",
                "<td width='40'>Лицевые счета</td>",
                "<td width='40'>Жильцы</td>",
                "<td width='40'>Редактирование</td>",
                "</tr>");
        for (int i = 0; i < 20; i ++){
            println(response,
                    "<tr>",
                    "<td></td>",
                    "<td></td>",
                    "<td></td>",
                    "<td></td>",
                    "<td></td>",
                    "<td></td>",
                    "</tr>");

        }
    }



    public void printAddNewApartment(HttpServletRequest request, HttpServletResponse response){

    }
}
