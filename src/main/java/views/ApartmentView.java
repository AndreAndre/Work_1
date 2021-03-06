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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static utils.TextUtils.println;

/**
 * Created by Andre on 06.11.2016.
 */
    @WebServlet ("/apartments")
public class ApartmentView extends HttpServlet {

        private HousesEntity house;
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
            response.setContentType("text/html; charset=utf-8");
            println(response,
                    "<html>",
                    "<head>",
                    "<title>Квартиры</title>",
                    "<link rel=\"StyleSheet\" type=\"text/css\" href=\"./css/housesview/style.css\">",
                    "</head>");

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

                        addNewApartment(num, square, floor);

                        String contextPath= "./apartments?houseId=" + house.getId();
                        response.sendRedirect(response.encodeRedirectURL(contextPath));
                        break;

                    case "edit":
                        int numEdit = Integer.parseInt(request.getParameter("num"));
                        double squareEdit = Double.parseDouble(request.getParameter("square"));
                        int floorEdit = Integer.parseInt(request.getParameter("floor"));
                        int apartmentId = Integer.parseInt(request.getParameter("editId"));

                        editApartment(numEdit, squareEdit, floorEdit, apartmentId);
                        String contextPathEdit= "./apartments?houseId=" + house.getId();
                        response.sendRedirect(response.encodeRedirectURL(contextPathEdit));
                        break;

                    case "del":
                        int paramDelete = Integer.parseInt(request.getParameter("removeId"));
                        int houseId = Integer.parseInt(request.getParameter("houseId"));
                        removeApartment(paramDelete);
                        String contextPathDel= "./apartments?houseId=" + houseId;
                        response.sendRedirect(response.encodeRedirectURL(contextPathDel));
                        break;

                    case "editAll" :
                        printApartmentsAllEdit(request, response);
                        break;

                    case "commitEditAll" :
                        editApartments(request);
                        String contextPathCommitEditAll= "./apartments?houseId=" + house.getId();
                        response.sendRedirect(response.encodeRedirectURL(contextPathCommitEditAll));
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
            house = (HousesEntity) entityUtils.get(HousesEntity.class, houseId);
            List<ApartmentsEntity> apartments = house.getApartmentsEntity();
            Collections.sort(apartments);
            //выводим ввод houseId и квартиры нужного дома
            printHtmlPickHouse(response);
            println(response, "<h1>Квариры дома '" + house.getAddress() + "'</h1>");

            println(response, "<a href='./apartments?view=editAll&houseId=" + house.getId() + "'>" +
                    "Перейти в режим редактирования</a></br>");
            printHtmlApartmentTableHead(response);  //шапка таблицы

            for (ApartmentsEntity apartment : apartments) {
                int editId;
                if(request.getParameter("editId") == null || request.getParameter("editId").isEmpty()) {
                    printHtmlApartment(response, apartment);
                }
                else {
                    editId = Integer.parseInt(request.getParameter("editId"));
                    if (editId != apartment.getId()) printHtmlApartment(response, apartment);
                    else printHtmlApartmentEdit(response, apartment);
                }
            }
            //добавляем форму для добавления квартиры..
            printHtmlApartmentAdd(response);
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
                        " дома, укажите ID дома или перейдите к выбору адреса. beegom, suk.</h2>");
        printHtmlPickHouse(response);

        //таблица со всеми квартирами. Вообщее со всеми. Со всех домов.
        println(response,
                "<h1>ВСЕ квартиры. Полностью.</h1>");

        printHtmlApartmentTableHead(response);
        //создаем коллекцию и вносим в нее ВСЕ квартиры.
        List<ApartmentsEntity> apartments = new ArrayList<ApartmentsEntity>();
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        apartments = entityUtils.listApartments();
        Collections.sort(apartments);
        //выводим все квартиры в таблицу
        for(ApartmentsEntity apartment : apartments){
            printHtmlApartment(response, apartment);
        }
    }

    /**
     * рисует таблицу где все строки редактируются
     * @param request
     * @param response
     * @throws IOException
     */
    public void printApartmentsAllEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int houseId = Integer.parseInt(request.getParameter("houseId")); //получаем ID дома
        //берем из базы указанный дом и все его квартиры помещаем в лист apartments.
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        house = (HousesEntity) entityUtils.get(HousesEntity.class, houseId);
        List<ApartmentsEntity> apartments = house.getApartmentsEntity();
        Collections.sort(apartments);
        //выводим ввод houseId и квартиры нужного дома
        printHtmlPickHouse(response);
        println(response, "<h1>Режим редактирования! Квариры дома '" + house.getAddress() + "'</h1>");

        printHtmlApartmentTableHead(response);
        println(response, "<form action='./apartments' method='POST' accept-charset=\"UTF-8\">");
        for(ApartmentsEntity apartment : apartments){
            println(response,
                    "<tr>",
                    "<td>" + house.getAddress() + "</td>",
                    "<input type='hidden' name='apartmentId' value='" + apartment.getId() + "'>",
                    "<td><input type='number' value='" + apartment.getApartmentNumber() + "' min=1 name='num' " +
                            "style='width:100%; height:40px; border:0'></td>",
                    "<td><input type='number' step=0.1 value='" + apartment.getSquare() + "' min=1 name='square' " +
                            "style='width:100%; height:40px; border:0'></td>",
                    "<td><input type='number' value='" + apartment.getFloor() + "' min=1 name='floor' " +
                            "style='width:100%; height:40px; border:0'></td>",
                    "</tr>");
        }
        println(response,
                "<tr>",
                "<input type='hidden' name='view' value='commitEditAll'>",
                "<td><input type='submit' value='Сохранить все изменения'></td>",
                "</tr>");
        println(response, "</form>");

    }


    //TODO разобраться почему не работает как надо(или как хочется)
    public void addNewApartment(int paramNum, double paramSquare, int paramFloor){
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();

        ApartmentsEntity apartment = new ApartmentsEntity(paramNum, paramSquare, paramFloor, house);
        entityUtils.add(apartment);
    }

    public void removeApartment(int apartmentId) throws IOException {
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        entityUtils.remove(ApartmentsEntity.class, apartmentId);
    }

    /**
     * Метод редактирует данные о квартире
     * @param editNum
     * @param editSquare
     * @param editFloor
     * @param apartmentId
     */
    public void editApartment(int editNum, double editSquare, int editFloor, int apartmentId){
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        List<ApartmentsEntity> apartments = house.getApartmentsEntity();
        ApartmentsEntity apartment = apartments.get(apartments.indexOf(entityUtils.get(ApartmentsEntity.class, apartmentId)));
        apartment.setApartmentNumber(editNum);
        apartment.setSquare(editSquare);
        apartment.setFloor(editFloor);
        entityUtils.update(apartment);
    }

    public void editApartments(HttpServletRequest request){
        Map<String,String[]> editApartments = request.getParameterMap();
        String[] id = editApartments.get("apartmentId");
        String[] num = editApartments.get("num");
        String[] square = editApartments.get("square");
        String[] floor = editApartments.get("floor");
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        List<Object> apartments = new ArrayList<Object>();
        for(int i = 0; i < num.length; i++){
            ApartmentsEntity apartment = (ApartmentsEntity) entityUtils.get(ApartmentsEntity.class, Integer.parseInt(id[i]));
            if(apartment != null) {
                int numEdit = Integer.parseInt(num[i]);
                Double squareEdit = Double.parseDouble(square[i]);
                int floorEdit = Integer.parseInt(floor[i]);
                int idEdit = Integer.parseInt(id[i]);

                apartment.setApartmentNumber(numEdit);
                apartment.setSquare(squareEdit);
                apartment.setFloor(floorEdit);

                apartments.add(apartment);
                //editApartment(numEdit, squareEdit, floorEdit, idEdit);
            }
        }
        entityUtils.updateList(apartments);


    }


    /**
     * Заголовок таблицы
     * @param response
     * @throws IOException
     */
    public void printHtmlApartmentTableHead(HttpServletResponse response) throws IOException{
        println(response,
                "<table cellspacing border cellpadding='2' " +
                        "align='left' bgcolor='#eeeeee' cols='5' width='960' ",
                "<tr>",
                "<th width='350'>Адрес дома</th>",
                "<th width='50'>Номер квартиры</th>",
                "<th width='50'>Площадь</th>",
                "<th width='50'>Этаж</th>",
                "<th width='50'></th>",
                "</tr>");
    }

    public void printHtmlApartmentTableHead(HttpServletResponse response, String message) throws IOException{
        printStatusMessage(response, message);
        println(response,
                "<table cellspacing border cellpadding='2' " +
                        "align='left' bgcolor='#eeeeee' cols='5' width='960' ",
                "<tr>",
                "<th width='350'>Адрес дома</th>",
                "<th width='50'>Номер квартиры</th>",
                "<th width='50'>Площадь</th>",
                "<th width='50'>Этаж</th>",
                "<th width='50'></th>",
                "</tr>");
    }

    /**
     * Метод респонсит HTML код, который рисует простую табличную строку с данными о квартире.
     * @param response
     * @param apartment
     * @throws IOException
     */
    public void printHtmlApartment(HttpServletResponse response, ApartmentsEntity apartment) throws IOException {
        HousesEntity house = apartment.getHouse();

        println(response,
                "<tr>",
                "<td>" + house.getAddress() + "</td>",
                "<td>" + apartment.getApartmentNumber() + "</td>",
                "<td>" + apartment.getSquare() + "</td>",
                "<td>" + apartment.getFloor() + "</td>",
                "<td><a href='./personal_accounts?apartmentId=" + apartment.getId() +
                        "'><img src='./images/document_16.png'></a>",
                "<a href='./residents?apartmentId=" + apartment.getId() +
                        "'><img src='./images/peoples_16.png'></a>",
                "<a href='?houseId=" + house.getId() + "&editId=" + apartment.getId() +
                        "'><img src='./images/pencil_16.png'></a>",
                "<a href='?view=del&houseId=" + house.getId() + "&removeId=" + apartment.getId() +
                        "'><img src='./images/remove_16.png'></a></td>",
                "</tr>");
    }

    /**
     * Респонсит HTML который рисует строку для добавления квартиры
     * @param response
     * @throws IOException
     */
    public void printHtmlApartmentAdd(HttpServletResponse response) throws IOException {
        println(response,
                "<form action='./apartments' method='POST' accept-charset=\"UTF-8\">",
                "<tr>",
                "<td>Добавить квартиру в этот дом:</td>",
                "<td><input type='number' placeholder='Номер' min=1 name='num' " +
                        "style='width:100%; height:40px; border:0'></td>",
                "<td><input type='number' step=0.1 placeholder='Площадь' min=1 name='square' " +
                        "style='width:100%; height:40px; border:0'></td>",
                "<td><input type='number' placeholder='Этаж' min=1 name='floor' " +
                        "style='width:100%; height:40px; border:0'></td>",
                "<input type='hidden' name='view' value='add'>",
                "<input type='hidden' name='houseId' value='" + house.getId() + "'>",
                "<td><input type='submit' value='Добавить квартиру в дом'></td>",
                "</tr>",
                "</form>");
    }


    public void printHtmlApartmentEdit(HttpServletResponse response, ApartmentsEntity apartment) throws IOException {
        println(response,
                "<form action='./apartments?houseId=" + house.getId() + "' method='POST' accept-charset=\"UTF-8\">",
                "<tr>",
                "<td>Измените данные</td>",
                "<td><input type='number' value='" + apartment.getApartmentNumber() + "' min=1 name='num' " +
                        "style='width:100%; height:40px; border:0'></td>",
                "<td><input type='number' step=0.1 value='" + apartment.getSquare() + "' min=1 name='square' " +
                        "style='width:100%; height:40px; border:0'></td>",
                "<td><input type='number' value='" + apartment.getFloor() + "' min=1 name='floor' " +
                        "style='width:100%; height:40px; border:0'></td>",
                "<input type='hidden' name='view' value='edit'>",
                "<input type='hidden' name='editId' value='" + apartment.getId() + "'>",
                "<td><input type='submit' value='Сохранить изменения'></td>",
                "</tr>",
                "</form>");
    }


    /**
     * Инпут для выбора ID дома
     * @param response
     * @throws IOException
     */
    public void printHtmlPickHouse(HttpServletResponse response) throws IOException {
        println(response,
                "<form action='./apartments' method=POST>",
                "<tr height='40'>",
                "<td><input type='number' placeholder='Введите сюда ID дома.' min=0" +
                        " name='houseId' style='width:100%; height:40px; border:0'></td>",
                "<td><input type='submit' value='Ввести'></td>",
                "</tr>");
    }

    public void printStatusMessage(HttpServletResponse response, String message) throws IOException {
        println(response, "<h2>" + message + "</h2>");
    }


}

