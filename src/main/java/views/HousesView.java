package views;

import com.mchange.v2.collection.MapEntry;
import entities.HousesEntity;
import utils.EntityUtilsImpl;
import utils.EntityUtilsImpl2;
import utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
//TODO: HTML тупо в лоб - не очень хорошо. Нужно подумать, как лучше генерировать страницы
//TODO: комментарии и документация к методам.
//TODO: подтверждение удаления дома
//TODO: каскадное удаление? (надо ли?)
/**
 * Created by fedyu on 04.11.2016.
 */
@WebServlet("/houses")
public class HousesView extends HttpServlet {

    //Объявляем объект класса с утилитами для работы с Entity
    EntityUtilsImpl2 entityUtils = new EntityUtilsImpl2();
    /*
    public HousesView() {
        super();

    }
*/
    //Делаем при работе с GET-запросами
    //Основной метод
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        entityUtils.openSession();

        //Устанавливаем кодировку на запрос/ответ
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        //Выводим заголовок страницы
        TextUtils.println(response,"<html><head><title>Список домов</title>",
            "<link rel=\"StyleSheet\" type=\"text/css\" href=\"./css/housesview/style.css\">",
            "</head><body>");
        //PrintWriter htmlPage = response.getWriter();
        TextUtils.println(response,"<h1>Дома</h1>");

        //Параметр view передаваемый в request - отвечает за функции страницы
        String paramView = request.getParameter("view");
        //Если параметров нет - отображаем по умолчанию таблицу домов
        if (paramView == null || paramView.isEmpty())
            printHousesTable(request, response);
        //Иначе проверяем значение параметра и делаем что-нибудь соответственно
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
                    //Организовываем переадресацию страницы чтобы избежать повторного добавления
                    redirect(response,"./houses?view=message&messageStatus=ok&messageText="+URLEncoder.encode("Дом успешно добавлен в базу данных.","UTF-8"));
                    break;
                //Переадресовываем сюда после выполнения какой либо операции.
                //с этим значением необходимо передавать параметр messageStatus=ok(bad) и messageText=[string]
                case "message":
                    String pMessageStatus = request.getParameter("messageStatus");
                    String pMessageText = request.getParameter("messageText");
                    //Если параметры не пустые
                    if ((pMessageStatus == null || pMessageStatus.isEmpty())
                            && (pMessageText == null || pMessageText.isEmpty())) {
                        printHousesTable(request, response);
                    } else {
                        messageStatus status;
                        switch (pMessageStatus) {
                            case ("ok"):
                                status = messageStatus.OK;
                                break;
                            case ("bad"):
                                status = messageStatus.BAD;
                                break;
                            case ("q"):
                                status = messageStatus.QUESTION;
                                break;
                            case ("attention"):
                                status = messageStatus.ATTENTION;
                                break;
                            case ("info"):
                                status = messageStatus.INFO;
                                break;
                            default:
                                status = messageStatus.UNKNOWN;
                                break;
                        }
                        printStatusMessage(response, status, pMessageText);
                        printHousesTable(request, response);
                    }
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
                    //Организовываем переадресацию страницы чтобы избежать повторного добавления
                    redirect(response, "./houses?view=message&messageStatus=ok&messageText="+URLEncoder.encode("Дом успешно удален из базы данных.","UTF-8"));
                    break;
                // Отображаем один дом (на случай когда много информации о доме будет).
                // Так называемая "Карточка дома"
                case "house":
                    printHouseInfo(request, response);
                    break;
                case "table_edit":
                    printEditableHousesTable(request, response);
                    break;
                case "edit":
                    //Получаем список параметров и выводим на консоль (отладка)
                    //Enumeration<String> params =  request.getParameterNames();
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

                    //entityUtils.updateList(editableHouses);
                    printHousesTable(request, response);


                    break;
                default:
                    printHousesTable(request, response);
                    break;
            }
        }

        TextUtils.println(response,
                "</bode></html>");
        //entityUtils.closeSession();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException    {
        //Говорим, что doPost делает тоже самое, что и doGet
        doGet(request, response);
    }

    private void redirect(HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(path));
    }

    /**
     * Метод выводит сообщени об успешной операции
     * @param response
     * @throws IOException
     */
    private void printStatusMessage(HttpServletResponse response, messageStatus status, String message) throws IOException {
        String imageName = null;
        switch (status) {
            case OK:
                imageName = "success_128.png";
                break;
            case BAD:
                imageName = "bad_128.png";
                break;
            case QUESTION:
                imageName = "question_128.png";
                break;
            case ATTENTION:
                imageName = "attention_128.png";
                break;
            case INFO:
                imageName = "info_128.png";
                break;
            case UNKNOWN:
                imageName = "info_128.png";
                break;
            default:
                break;
        }

        TextUtils.println(response,"</br><img src='./images/"+imageName+"'>",
                "</br>"+message);
    }

    /**
     * Метод добавляет новый дом в БД
     * @param paramAddress - Адрес дома
     * @param paramFloors - количество этажей
     * @param paramBuildDate - дата постройки
     */
    private void addNewHouse(String paramAddress, int paramFloors, Date paramBuildDate) {
        //EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        HousesEntity newHouse = new HousesEntity(paramAddress, paramFloors, paramBuildDate);
        entityUtils.add(newHouse);
    }

    /**
     * Информация об отдельном доме (на случай, когда будет много инфы в домах);
     * @param request
     * @param response
     * @throws IOException
     */
    private void printHouseInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TextUtils.println(response,"<h2>Информация о доме</h2>");

    }

    /**
     * Метод отображает таблицу с домами
     * @param request
     * @param response
     * @throws IOException
     */
    private void printHousesTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Получим и выведем табличку с домами
        //EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        List<HousesEntity> houses = entityUtils.listHouse();
        Collections.sort(houses);
        TextUtils.println(response,
                "<a href='./houses?view=table_edit'><img src='./images/edit_32.png' title='Редактировать дом' width='16px'></a>",
                "<table cellspacing=\"2\" border=\"1\" cellpadding=\"2\" width=\"960\">",
                "<tr>",
                "<th>Адрес дома</th>",
                "<th>Количество этажей</th>",
                "<th>Дата постройки</th>",
                "<th width=\"120\">Операции</th>",
                 "</tr>");

        for (HousesEntity house :
                houses) {
            TextUtils.println(response,printTableRow(house));
        }

        TextUtils.println(response,
                "<form action='./houses' method='POST' accept-charset=\"UTF-8\">",
                "<tr>",
                "<td><input type='text' placeholder='Адрес нового дома' name='address' style='width:100%; height:40px; border:0'></td>",
                "<td><input type='number' placeholder='Этажей' min=1 name='floors' style='width:100%; height:40px; border:0'></td>",
                "<td><input type='date' placeholder='Дата постройки' name='buildDate' style='width:100%; height:40px; border:0'></td>",
                "<input type='hidden' name='view' value='edit'>",
                "<td><input type='submit' value='Добавить дом'></td>",
                "</tr>",
                "</form>"
        );

        TextUtils.println(response,"</table>");
    }

    /**
     * Отображает таблицу с домами, доступную для редактирования
     * @param request
     * @param response
     * @throws IOException
     */
    private void printEditableHousesTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Получим и выведем табличку с домами
        //EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        List<HousesEntity> houses = entityUtils.listHouse();
        Collections.sort(houses);
        TextUtils.println(response,
                "<table cellspacing=\"2\" border=\"1\" cellpadding=\"2\" width=\"960\">",
                "<form action='./houses' method='POST' accept-charset=\"UTF-8\">",
                "<tr>",
                "<th>Адрес дома</th>",
                "<th>Количество этажей</th>",
                "<th>Дата постройки</th>",
                "<th width=\"120\">Операции</th>",
                "</tr>");

        for (HousesEntity house : houses) {
            TextUtils.println(response,printEditableTableRow(house));
        }

        TextUtils.println(response,
                "<tr>",
                "<td></td>",
                "<td></td>",
                "<td></td>",
                "<input type='hidden' name='view' value='edit'>",
                "<td><input type='submit' value='Сохранить изменения'></td>",
                "</tr>",
                "</form>"
        );

        TextUtils.println(response,"</table>");
    }

    private String printTableRow(HousesEntity house) {
        //Формат даты
        DateFormat formatForm = new SimpleDateFormat("dd.MM.yyyy");//dd.MM.yyyy
        String row =
        "<tr>"+
        "<td>"+house.getAddress()+"</td>"+
        "<td>"+house.getFloors()+"</td>"+
        "<td>"+formatForm.format(house.getBuildDate())+"</td>"+
        "<td><a href='./apartments?houseId=" + house.getId() + "'><img src='./images/apartments_32.png' title='Список квартир' width='16px'></a>"+
        "<a href='./houses?view=house'><img src='./images/edit_32.png' title='Редактировать дом' width='16px'></a>"+
        "<a href='./houses?view=del&removeId=" + house.getId() + "'><img src='./images/remove_32.png' title='Удалить дом' width='16px'></a></td>"+
         "</tr>";
        return row;
    }

    private String printEditableTableRow(HousesEntity house) {
        //Формат даты
        DateFormat formatForm = new SimpleDateFormat("dd.MM.yyyy");//dd.MM.yyyy
        String row =
            "<tr>"+
            "<input type='hidden' name='houseID' value='"+house.getId()+"'>"+
            "<td><input type='text' placeholder='Адрес нового дома' value='"+house.getAddress()+"' name='address' style='width:100%; height:40px; border:0'></td>"+
            "<td><input type='number' placeholder='Этажей' value='"+house.getFloors()+"' min=1 name='floors' style='width:100%; height:40px; border:0'></td>"+
            "<td><input type='date' placeholder='Дата постройки'value='"+house.getBuildDate()+"' name='buildDate' style='width:100%; height:40px; border:0'></td>"+
            "<td></td>"+
            "</tr>";
        return row;
    }

    /**
     * Метод получает ID-дома и удаляет его из БД
     * @param houseId
     * @throws IOException
     */
    private void removeHouse(int houseId) throws IOException {
        System.out.println("remove houseId = " + houseId);
        //EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        entityUtils.remove(HousesEntity.class,houseId);
    }

}

//Перечисление для статуса сообщения
enum messageStatus {
    OK,
    BAD,
    QUESTION,
    ATTENTION,
    INFO,
    UNKNOWN }