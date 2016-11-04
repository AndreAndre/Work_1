package views;

import database.HibernateSessionFactory;
import entities.HousesEntity;
import org.hibernate.Session;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Created by fedyu on 04.11.2016.
 */
public class HousesView extends HttpServlet {
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        httpServletResponse.setContentType("text/html; charset=utf-8");
        httpServletResponse.getWriter().print("<h1>Display houses</h1>");

        //Создаем объект Session из нашей фабрики
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        //Открываем транзакцию
        session.beginTransaction();
        //Получаем из БД объект типа HouseEntity с ID = 0
        HousesEntity house = (HousesEntity) session.load(HousesEntity.class, 0);
        //Выводим информацию о доме
        httpServletResponse.getWriter().print("Адрес: " + house.getAddress() + ". Этажей: " + house.getFloors() + ". Дата постройки: " + house.getBuildDate());
        //Закрываем сессию
        session.close();
        //Закрываем коннект к БД
        HibernateSessionFactory.shutdown();

    }
}
