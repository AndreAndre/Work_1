package utils;

import database.HibernateSessionFactory;
import entities.HousesEntity;
import org.hibernate.ObjectNotFoundException;
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
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        HousesEntity house = session.load(HousesEntity.class, id);

        //TODO: Добавить нормальную проверку на существованеи объекта. Сейчас, даже если load Не находит по id объект в базе, то почему то house != null
        if (null != house) {
            try {
                session.delete(house);
                session.getTransaction().commit();
                session.close();
            } catch (ObjectNotFoundException e) {
                System.out.println("Exception: " + e);
                System.out.println("Объекта, который вы пытаетесь удалить - не существует.");
            }
        } else {

        }


        closeSession();
    }

    public List<HousesEntity> listHouse() {
        List<HousesEntity> houses = HibernateSessionFactory.getSessionFactory().openSession().createQuery("from HousesEntity").list();
        closeSession();
        return houses;
    }
}


