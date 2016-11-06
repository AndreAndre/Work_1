package views;

import entities.HousesEntity;
import utils.EntityUtilsImpl;
import utils.TextUtils;

import javax.servlet.ServletException;
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

    //TODO: Исправить - выводит только один раз. После обновления страницы или повторного захода - не отображается (WTF?!)
    private void printHousesTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter htmlPage = response.getWriter();
        htmlPage.print("<h1>Дома</h1>");
        TextUtils.println(response,
                "<h2>1" +
                        "11" +
                        "1" + "</h2>",
                "<h3>2222</h3>",
                "<h4>3333</h4>");

        //Получим и выведем табличку с домами
        EntityUtilsImpl houseUtils = new EntityUtilsImpl();
        List<HousesEntity> houses = houseUtils.listHouse();
        htmlPage.println("<table cellspacing=\"2\" border=\"1\" cellpadding=\"2\" width=\"960\">");
        htmlPage.println("<tr>");
        htmlPage.println("<td>Адрес дома</td>");
        htmlPage.println("<td>Количество этажей</td>");
        htmlPage.println("<td>Дата постройки</td>");
        htmlPage.println("<td width=\"40\"></td>");
        htmlPage.println("<td width=\"40\"></td>");
        htmlPage.println("<td width=\"40\"></td>");
        htmlPage.println("</tr>");
        for (HousesEntity house :
                houses) {
            htmlPage.println("<tr>");
            htmlPage.println("<td>"+house.getAddress()+"</td>");
            htmlPage.println("<td>"+house.getFloors()+"</td>");
            htmlPage.println("<td>"+house.getBuildDate()+"</td>");
            htmlPage.println("<td><a href='#'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/City-32.png'></a></td>");
            htmlPage.println("<td><a href='#'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/EditDocument-32.png'></a></td>");
            htmlPage.println("<td><a href='#'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/Cancel-32.png'></a></td>");
            htmlPage.println("</tr>");
        }

        htmlPage.println("</table>");
        htmlPage.println("<a href='#'><img src='https://cdn2.iconfinder.com/data/icons/bitsies/128/Add-32.png'>Добавить новую запись.</a> ");
        //

        //Закрываем коннект к БД
        //HibernateSessionFactory.shutdown();
    }

    //TODO: Реализовать форму по добавлению нового дома в БД
    private void printAddNewHouseForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityUtilsImpl houseUtils = new EntityUtilsImpl();

        PrintWriter htmlPage = response.getWriter();
        htmlPage.print("<h1>Дома</h1>");
        htmlPage.print("Добавляем новый дом");

        String formAddress = "ул. Балбеса, 22";
        int formFloors = 5;
        Date formDate = Date.valueOf("1980-01-02");

        HousesEntity newHouse = new HousesEntity(formAddress, formFloors, formDate);
        houseUtils.add(newHouse);
    }

}
