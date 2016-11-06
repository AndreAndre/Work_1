package utils;

import database.HibernateSessionFactory;
import entities.HousesEntity;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import java.util.List;


/**
 * Created by fedyu on 04.11.2016.
 * Класс для служебных методов по работе с домами (добавить, удалить, обновить, считать и т.д)
 */
public class EntityUtilsImpl implements EntityUtils {

    private void closeSession() {
        //HibernateSessionFactory.getSessionFactory().openSession().close();
        //HibernateSessionFactory.shutdown();
    }

    public void add(Object entity) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        closeSession();
    }

    public Object get(Class objClass, int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Object entity = session.load(objClass, id);
        closeSession();
        return entity;
    }

    public void remove(Class objClass, int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();

        Object entity = session.load(objClass, id);

        //TODO: Добавить нормальную проверку на существованеи объекта. Сейчас, даже если load Не находит по id объект в базе, то почему то house != null
        if (null != entity) {
            try {
                session.delete(entity);
                session.getTransaction().commit();
                session.close();
            } catch (ObjectNotFoundException e) {
                System.out.println("Exception: " + e);
                System.out.println("Объекта, который вы пытаетесь удалить - не существует.");
            }
        }
        closeSession();
    }

    public List<Object> list(Class objClass) {
        String entityName = objClass.toString();
        List<Object> entities = HibernateSessionFactory.getSessionFactory().openSession().createQuery("from " + entityName).list();
        closeSession();
        return entities;
    }
}


