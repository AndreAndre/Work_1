package views;

import entities.HousesEntity;
import utils.EntityUtilsImpl;
import utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Enumeration;
import java.util.List;
//TODO: HTML тупо в лоб - не очень хорошо. Нужно подумать, как лучше генерировать страницы
/**
 * Created by fedyu on 04.11.2016.
 */
@WebServlet("/houses")
public class HousesView extends HttpServlet {


    //Делаем при работе с GET-запросами
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html; charset=utf-8");

        Enumeration<String> params =  request.getParameterNames();

        while (params.hasMoreElements()) {
            System.out.println("Параметр " + params.nextElement());
        }


        String paramView = request.getParameter("view");
        if (paramView == null || paramView.isEmpty())
            printHousesTable(request, response);
        else {
            switch (paramView) {
                case "add":
                    printAddNewHouseForm(request, response);
                    break;
                case "list":
                    printHousesTable(request, response);
                    break;
                case "del":
                    int paramDelete = Integer.getInteger(request.getParameter("removeId"));
                    System.out.println("paramDelete = " + paramDelete);
                    removeHouse(paramDelete);
                    printHousesTable(request, response);
                    break;
                default:
                    printHousesTable(request, response);
                    break;
            }
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        doGet(request, response);
    }


    private void printHousesTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter htmlPage = response.getWriter();
        htmlPage.println("<h1>Дома</h1>");

        //Получим и выведем табличку с домами
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        List<HousesEntity> houses = entityUtils.listHouse();
        TextUtils.println(response,
            "<table cellspacing=\"2\" border=\"1\" cellpadding=\"2\" width=\"960\">",
            "<tr>",
            "<td>Адрес дома</td>",
            "<td>Количество этажей</td>",
            "<td>Дата постройки</td>",
            "<td width=\"40\"></td>",
            "<td width=\"40\"></td>",
            "<td width=\"40\"></td>",
           "</tr>");

        for (HousesEntity house :
                houses) {
            TextUtils.println(response,"<tr>",
                "<td>"+house.getAddress()+"</td>",
                "<td>"+house.getFloors()+"</td>",
                "<td>"+house.getBuildDate()+"</td>",
                "<td><a href='./apartments?houseId=" + house.getId() + "'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/City-32.png'></a></td>",
                "<td><a href='#'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/EditDocument-32.png'></a></td>",
                "<td><a href='?view=del&removeId=" + house.getId() + "'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/Cancel-32.png'></a></td>",
                "</tr>");
        }

        htmlPage.println("</table>");
        htmlPage.println("<a href='?view=add'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/Add-32.png'>Добавить новую запись.</a> ");
        //

        //Закрываем коннект к БД
        //HibernateSessionFactory.shutdown();
    }

    //TODO: Реализовать форму по добавлению нового дома в БД
    private void printAddNewHouseForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();

        PrintWriter htmlPage = response.getWriter();
        htmlPage.print("<h1>Дома</h1>");
        htmlPage.print("Добавляем новый дом");

        String formAddress = "ул. Балбеса, 22";
        int formFloors = 5;
        Date formDate = Date.valueOf("1980-01-02");

        HousesEntity newHouse = new HousesEntity(formAddress, formFloors, formDate);
        entityUtils.add(newHouse);
    }

    //TODO: Удаление домов (сейчас NullPointerException)
    private void removeHouse(int houseId) throws IOException {
        System.out.println("remove houseId = " + houseId);
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        entityUtils.remove(HousesEntity.class,houseId);
    }

}
