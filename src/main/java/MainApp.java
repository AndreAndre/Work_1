import database.HibernateSessionFactory;
import entities.ApartmentsEntity;
import entities.HousesEntity;
import entities.PersonalAccountsEntity;
import entities.ResidentsEntity;
import org.hibernate.Session;
import utils.EntityUtils;
import utils.EntityUtilsImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
public class MainApp {
    public static void main(String[] args) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        for(int i = 10; i < 110; i++){
            ApartmentsEntity apartment  = session.load(ApartmentsEntity.class,i);
            if(apartment != null) {
                apartment.setApartmentNumber(333);
                apartment.setSquare(100D);
                apartment.setFloor(111);
                session.saveOrUpdate(apartment);
            }
        }
        session.getTransaction().commit();
        session.close();
        HibernateSessionFactory.shutdown();
    }
}
