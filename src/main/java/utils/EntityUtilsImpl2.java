package utils;

import database.HibernateSessionFactory;
import entities.ApartmentsEntity;
import entities.HousesEntity;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import views.HousesView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fedyu on 04.11.2016.
 * Класс для служебных методов по работе с домами (добавить, удалить, обновить, считать и т.д)
 */
public class EntityUtilsImpl2 implements EntityUtils {
    Session session;

    public void openSession() {
        session = HibernateSessionFactory.getSessionFactory().openSession();
    }

    public void closeSession() {
        session.close();
    }

    public void add(Object entity) {
        session.beginTransaction();
        //session.refresh(entity);
        session.save(entity);
        session.getTransaction().commit();
        session.flush();
    }

    public void update(Object entity) {
        session.beginTransaction();
        session.saveOrUpdate(entity);
        session.getTransaction().commit();
        session.flush();
    }

    public void updateList(List<Object> entitys) {
        session.beginTransaction();
        for (Object entity :
                entitys) {
            session.saveOrUpdate(entity);
        }
        session.getTransaction().commit();
        session.flush();
    }

    public Object get(Class objClass, int id) {
        Object entity = session.get(objClass, id);
        return entity;
    }

    public Object load(Class objClass, int id) {
        Object entity = session.load(objClass, id);
        return entity;
    }

    public void remove(Class objClass, int id) {
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
    }

    //TODO: Не работает. Починить
    public List<Object> list(Class objClass) {
        String entityName = objClass.toString();
        System.out.println("entityName " + entityName);
        List<HousesEntity> entities = HibernateSessionFactory.getSessionFactory().openSession().createQuery("from HousesEntity").list();
        List<Object> obj = new ArrayList<Object>();
        obj.add(entities);
        return obj;
    }

    //Заглушка
    public List<HousesEntity> listHouse() {
        List<HousesEntity> entities = HibernateSessionFactory.getSessionFactory().openSession().createQuery("from HousesEntity").list();
        return entities;

    }

    /**
     * возвращает лист со всеми квартирами
     */
    public List<ApartmentsEntity> listApartments() {
        List<ApartmentsEntity> entities = HibernateSessionFactory.getSessionFactory().openSession().createQuery("from ApartmentsEntity ").list();
        return entities;
    }
/*
    public void refresh(Object obj) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.refresh(obj);
        closeSession();
    }
*/
}


