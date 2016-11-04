package utils;

import database.HibernateSessionFactory;
import entities.HousesEntity;
import org.hibernate.Session;

import java.util.List;

//TODO: Сделать удаление домов
/**
 * Created by fedyu on 04.11.2016.
 * Класс для служебных методов по работе с домами (добавить, удалить, обновить, считать и т.д)
 */
public class HouseUtilsImpl implements HouseUtils{

    private void closeSession() {
        //HibernateSessionFactory.getSessionFactory().openSession().close();
        //HibernateSessionFactory.shutdown();
    }

    public void addHouse(HousesEntity house) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(house);
        session.getTransaction().commit();
        closeSession();
    }

    public HousesEntity getHouse(int id) {
        HousesEntity house = (HousesEntity) HibernateSessionFactory.getSessionFactory().openSession().load(HousesEntity.class, id);
        closeSession();
        return house;
    }

    public void removeHouse(int id) {
        HousesEntity contact = (HousesEntity) HibernateSessionFactory.getSessionFactory().openSession().load(
                HousesEntity.class, id);
        if (null != contact) {
            HibernateSessionFactory.getSessionFactory().openSession().delete(contact);
        }
        closeSession();
    }

    public List<HousesEntity> listHouse() {
        List<HousesEntity> houses = HibernateSessionFactory.getSessionFactory().openSession().createQuery("from HousesEntity").list();
        closeSession();
        return houses;
    }
}


