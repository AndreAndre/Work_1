import database.HibernateSessionFactory;
import entities.ApartmentsEntity;
import entities.HousesEntity;
import utils.EntityUtilsImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
public class MainApp {
    public static void main(String[] args) {
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();
        HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, 0);
        //Выводим информацию о доме
        System.out.println("Выводим информацию о доме с id = 0");
        System.out.println("Адрес: " + house.getAddress() + ". Этажей: " + house.getFloors() + ". Дата постройки: " + house.getBuildDate());


        List<ApartmentsEntity> apartments = house.getApartmentsEntity();
        for (ApartmentsEntity apartment :
                apartments) {
            System.out.println("Выводим квартиры из домов");
            System.out.println("Номер: " + apartment.getApartmentNumber());
            System.out.println("Этаж: " + apartment.getFloor());
            System.out.println("Площадь: " + apartment.getSquare());
            System.out.println("---");
        }


        ApartmentsEntity newApartment = new ApartmentsEntity();
        newApartment.setHouse(house);
        newApartment.setFloor(5);
        newApartment.setApartmentNumber(110);
        newApartment.setSquare(42.5);

        entityUtils.add(newApartment);

        HibernateSessionFactory.shutdown();
    }
}
