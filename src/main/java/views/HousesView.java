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
import java.net.URL;
import java.sql.Date;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
//TODO: HTML тупо в лоб - не очень хорошо. Нужно подумать, как лучше генерировать страницы
//TODO: комментарии и документация к методам.
/**
 * Created by fedyu on 04.11.2016.
 */
@WebServlet("/houses")
public class HousesView extends HttpServlet {

    //Делаем при работе с GET-запросами
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        TextUtils.println(response,"<html><head><title>Список домов</title>",
            "<link rel=\"StyleSheet\" type=\"text/css\" href=\"./css/housesview/style.css\">",
            "</head><body>");


                //Получаем список параметров и выводим на консоль (отладка)
        Enumeration<String> params =  request.getParameterNames();
        while (params.hasMoreElements()) {
            System.out.println("Параметр " + params.nextElement());
        }



        //Параметр view передаваемый в request - отвечает за функции страницы
        String paramView = request.getParameter("view");
        if (paramView == null || paramView.isEmpty())
            printHousesTable(request, response);
        else {
            switch (paramView) {
                //add - добавляем новую запись в таблицу домов
                case "add":
                    //Получаем параметры из запроса
                    String paramAddress = request.getParameter("address");
                    int paramFloors =  Integer.parseInt(request.getParameter("floors"));
                    Date paramBuildDate = Date.valueOf(request.getParameter("buildDate"));
                    //Вызываем метод добавления новой записи в БД
                    addNewHouse(paramAddress, paramFloors, paramBuildDate);
                    //Организовываем переадресацию страницы
                    String contextPath= "./houses?view=ok";
                    response.sendRedirect(response.encodeRedirectURL(contextPath));
                    //printHousesTable(request, response);
                    //printSuccessfulAddMessage(response);
                    break;
                //Переадресовываем сюда после добавления дома
                case "ok":
                    printHousesTable(request, response);
                    printSuccessfulAddMessage(response);
                    break;
                //Выводим список всех домов
                case "list":
                    printHousesTable(request, response);
                    break;
                //Удаляем дом из таблицы домов
                case "del":
                    int paramDelete = Integer.parseInt(request.getParameter("removeId"));
                    System.out.println("paramDelete = " + paramDelete);
                    removeHouse(paramDelete);
                    printHousesTable(request, response);
                    break;
                default:
                    printHousesTable(request, response);
                    break;
            }
        }

        TextUtils.println(response,
                "</bode></html>");
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException    {
        //response.setContentType("text/html; charset=utf-8");
        doGet(request, response);
    }

    /**
     * Метод выводит сообщени об успешной операции
     * @param response
     * @throws IOException
     */
    private void printSuccessfulAddMessage(HttpServletResponse response) throws IOException {
        response.getWriter().print("</br><img src='./images/success_128.png'><h2>Дом успешно добавлен</h2>");
    }

    /**
     * Метод добавляет новый дом в БД
     * @param paramAddress - Адрес дома
     * @param paramFloors - количество этажей
     * @param paramBuildDate - дата постройки
     */
    private void addNewHouse(String paramAddress, int paramFloors, Date paramBuildDate) {
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        HousesEntity newHouse = new HousesEntity(paramAddress, paramFloors, paramBuildDate);
        entityUtils.add(newHouse);
    }



    /**
     * Метод отображает таблицу с домами
     * @param request
     * @param response
     * @throws IOException
     */
    private void printHousesTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter htmlPage = response.getWriter();
        htmlPage.println("<h1>Дома</h1>");

        //Получим и выведем табличку с домами
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        List<HousesEntity> houses = entityUtils.listHouse();
        Collections.sort(houses);
        TextUtils.println(response,
            "<table cellspacing=\"2\" border=\"1\" cellpadding=\"2\" width=\"960\">",
            "<tr>",
            "<th>Адрес дома</th>",
            "<th>Количество этажей</th>",
            "<th>Дата постройки</th>",
            "<th width=\"120\">Операции</th>",
           "</tr>");

        for (HousesEntity house :
                houses) {
            TextUtils.println(response,"<tr>",
                "<td>"+house.getAddress()+"</td>",
                "<td>"+house.getFloors()+"</td>",
                "<td>"+house.getBuildDate()+"</td>",

                "<td><a href='./apartments?houseId=" + house.getId() + "'><img src='./images/apartments_32.png' title='Список квартир' width='16px'></a>",
                "<a href='#'><img src='./images/edit_32.png' title='Редактировать дом' width='16px'></a>",
                "<a href='?view=del&removeId=" + house.getId() + "'><img src='./images/remove_32.png' title='Удалить дом' width='16px'></a></td>",
                "</tr>");
        }



        TextUtils.println(response,
                "<form action='./houses' method='POST' accept-charset=\"UTF-8\">",
                "<tr>",
                "<td><input type='text' placeholder='Адрес нового дома' name='address' style='width:100%; height:40px; border:0'></td>",
                "<td><input type='number' placeholder='Этажей' min=1 name='floors' style='width:100%; height:40px; border:0'></td>",
                "<td><input type='date' placeholder='Дата постройки' name='buildDate' style='width:100%; height:40px; border:0'></td>",
                "<input type='hidden' name='view' value='add'>",
                "<td><input type='submit' value='Добавить дом'></td>",
                "</tr>",
                "</form>"
        );

        htmlPage.println("</table>");
        //

        //Закрываем коннект к БД
        //HibernateSessionFactory.shutdown();
    }


    /**
     * Метод получает ID-дома и удаляет его из БД
     * @param houseId
     * @throws IOException
     */
    private void removeHouse(int houseId) throws IOException {
        System.out.println("remove houseId = " + houseId);
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        entityUtils.remove(HousesEntity.class,houseId);
    }

}
