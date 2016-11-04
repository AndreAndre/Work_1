import database.HibernateSessionFactory;
import entities.HousesEntity;
import org.hibernate.Session;

/**
 * Created by fedyu on 04.11.2016.
 */
public class MainApp {
    public static void main(String[] args) {
        //Создаем объект Session из нашей фабрики
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        //Открываем транзакцию
        session.beginTransaction();
        //Получаем из БД объект типа HouseEntity с ID = 0
        HousesEntity house = (HousesEntity) session.load(HousesEntity.class, 0);
        //Выводим информацию о доме
        System.out.println("Адресссук: " + house.getAddress() + ". Этажей: " + house.getFloors() + ". Дата постройки: " + house.getBuildDate());
        //Закрываем сессию
        session.close();
        //Закрываем коннект к БД
        HibernateSessionFactory.shutdown();
    }
}
