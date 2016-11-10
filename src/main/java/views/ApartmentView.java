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
import java.util.ArrayList;
import java.util.List;

import static utils.TextUtils.println;

/**
 * Created by Andre on 06.11.2016.
 */
    @WebServlet ("/apartments")
public class ApartmentView extends HttpServlet {

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
            response.setContentType("text/html; charset=utf-8");
            println(response,
                    "<html>",
                    "<head>",
                    "<title>Квартиры</title>",
                    "</head>");

            println(response, "<h2 align='center'>Укажите ID дома или перейдите к выбору адреса. beegom.</h2>");

            //получаем параметры
            String paramView = request.getParameter("view");
            if(paramView == null || paramView.isEmpty()){
                String paramHouseId = request.getParameter("houseId");
                if (paramHouseId == null || paramHouseId.isEmpty())
                    printApartmentOfNullHouse(request, response);
                else printApartmentTable(request, response);
            }
            else {
                switch(paramView) {
                    case "add":
                        //достаем параметры и дом
                        int num = Integer.parseInt(request.getParameter("num"));
                        double square = Double.parseDouble(request.getParameter("square"));
                        int floor = Integer.parseInt(request.getParameter("floor"));
                        int houseId = Integer.parseInt(request.getParameter("houseId"));
                        println("перед адд");
                        addNewApartment(num, square, floor, houseId);
                        println("после адд");

                        String contextPath= "./apartments?houseId=" + houseId;
                        response.sendRedirect(response.encodeRedirectURL(contextPath));
                        println("перед брейком");
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
     * В последнем строке -- форма для добавления квартиры... будет
     * @param request
     * @param response
     */
    public void printApartmentTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

         int houseId = Integer.parseInt(request.getParameter("houseId")); //получаем ID дома
            //берем из базы указанный дом и все его квартиры помещаем в лист apartments.
            EntityUtilsImpl entityUtils = new EntityUtilsImpl();
            HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, houseId);
            List<ApartmentsEntity> apartments = house.getApartmentsEntity();
            //выводим ввод houseId и квартиры нужного дома
            println(response,
                    "<form action='./apartments' method=GET>",
                    "<tr height='40'>",
                    "<td><input type='number' placeholder='Введите сюда ID дома.' min=0" +
                            " name='houseId' style='width:100%; height:40px; border:0'></td>",
                    "<td><input type='submit' value='Ввести'></td>",
                    "</tr>",
                    "</form>",
                    "<h1>Квартиры дома '" + house.getAddress() + "' </h1>",
                    "<table cellspacing border cellpadding='2' " +
                            "align='left' bgcolor='#eeeeee' cols='5' width='960' ",
                    "<tr>",
                    "<td width='350'>Адрес дома</td>",
                    "<td width='50'>Номер квартиры</td>",
                    "<td width='50'>Площадь</td>",
                    "<td width='50'>Этаж</td>",
                    "<td width='50'></td>",
                    "</tr>");
            for (ApartmentsEntity apartment : apartments) {
                println(response,
                        "<tr>",
                        "<td>" + house.getAddress() + "</td>",
                        "<td>" + apartment.getApartmentNumber() + "</td>",
                        "<td>" + apartment.getSquare() + "</td>",
                        "<td>" + apartment.getFloor() + "</td>",
                        "<td><a href='./personal_accounts?apartmentId=" + apartment.getId() +
                                "'><img src='./images/document_16.png'></a></td>",
                        "<td><a href='./residents?apartmentId=" + apartment.getId() +
                                "'><img src='./images/peoples_16.png'></a></td>",
                        "<td><a href='?view=edit&editId=" + apartment.getId() +
                                "'><img src='./images/pencil_16.png'></a></td>",
                        "</tr>");
            }
            //добавляем форму для добавления квартиры..
            println(response,
                    "<form action='./apartments' method='POST' accept-charset=\"UTF-8\">",
                    "<tr>",
                    "<td>Добавить квартиру в этот дом:</td>",
                    "<td><input type='number' placeholder='Номер' min=1 name='num' " +
                            "style='width:100%; height:40px; border:0'></td>",
                    "<td><input type='number' placeholder='Площадь' min=1 name='square' " +
                            "style='width:100%; height:40px; border:0'></td>",
                    "<td><input type='number' placeholder='Этаж' min=1 name='floor' " +
                            "style='width:100%; height:40px; border:0'></td>",
                    "<input type='hidden' name='view' value='add'>",
                    "<input type='hidden' name='houseId' value='" + house.getId() + "'>",
                    "<td><input type='submit' value='Добавить квартиру в дом'></td>",
                    "</tr>",
                    "</form>");
            println(response, "</table>");

        }
    /**
     * Метод вызывается при houseId == null
     * Отображает страницу с таблицей со всеми домами
     * и предлагает выбрать дом.
     * ну как выбрать... пока можно ввести ID дома.
     * @throws IOException
     * */
    //TODO прикрутить возможность выбора дома методом прямого тыка
    public void printApartmentOfNullHouse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        println(response,
                "<h2 align='center'>Показаны все квартиры во всех домах. Для того, чтобы увидеть квартиры определенного" +
                        " дома, укажите ID дома или перейдите к выбору адреса. beegom, suk.</h2>",
                "<form action='./apartments' method=GET>",
                "<tr height='40'>",
                "<td><input type='number' placeholder='Введите сюда ID дома.' min=0" +
                        " name='houseId' style='width:100%; height:40px; border:0'></td>",
                "<td><input type='submit' value='Ввести'></td>",
                "</tr>",
                "</form>");

        //таблица со всеми квартирами. Вообщее со всеми. Со всех домов.
        println(response,
                "<h1>ВСЕ квартиры. Полностью.</h1>",
                "<table cellspacing border cellpadding=\"2\" " +
                "align=\"left\" bgcolor=\"#eeeeee\" cols=\"5\"  width=\"960\" ",
                "<tr>",
                "<td>Адрес дома</td>",
                "<td>Номер квартиры</td>",
                "<td>Площадь</td>",
                "<td>Этаж</td>",
                "<td width='20'></td>",
                "<td width='20'></td>",
                "<td width='20'></td>",
                "</tr>");
        //создаем коллекцию и вносим в нее ВСЕ квартиры.
        List<ApartmentsEntity> apartments = new ArrayList<ApartmentsEntity>();
        apartments = EntityUtilsImpl.listApartments();
        //выводим все квартиры в таблицу
        for(ApartmentsEntity apartment : apartments){
            println(response,
                    "<tr>",
                    "<td>" + apartment.getHouse().getAddress() + "</td>",
                    "<td>" + apartment.getApartmentNumber() +"</td>",
                    "<td>" + apartment.getSquare() + "</td>",
                    "<td>" + apartment.getFloor() + "</td>",
                    "<td><a href='./personal_accounts?apartmentId=" + apartment.getId() +
                            "'><img src='./images/document_16.png'></a></td>",
                    "<td><a href='./residents?apartmentId=" + apartment.getId() +
                            "'><img src='./images/peoples_16.png'></a></td>",
                    "<td><a href='?view=edit&editId=" + apartment.getId() +
                            "'><img src='./images/pencil_16.png'></a></td>",
                    "</tr>");
        }
    }


    //TODO разобраться почему не работает как надо(или как хочется)
    public void addNewApartment(int paramNum, double paramSquare, int paramFloor, int houseId){
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, houseId);
        System.out.println("Адрес: " + house.getAddress() + ". Этажей: "                //нужный код
                + house.getFloors() + ". Дата постройки: " + house.getBuildDate());     //не удалять

        ApartmentsEntity apartment = new ApartmentsEntity(paramNum, paramSquare, paramFloor, house);
        entityUtils.add(apartment);
    }
}
