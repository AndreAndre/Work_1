import database.HibernateSessionFactory;
import entities.ApartmentsEntity;
import entities.HousesEntity;
import utils.HouseUtilsImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
public class MainApp {
    public static void main(String[] args) {
        HouseUtilsImpl houseUtils = new HouseUtilsImpl();
        HousesEntity house = houseUtils.getHouse(0);
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


        System.out.println("Добавляем новый дом");
        HousesEntity house2 = new HousesEntity();
        house2.setId(100);
        house2.setAddress("ID сам не сгенерировался");
        System.out.println("ID из house2 (0): " +house2.getId());

        houseUtils.addHouse(house2);

        System.out.println("ID из house2: " +house2.getId());

        System.out.println("И ещё дом Добавляем новый дом");
        HousesEntity house4 = new HousesEntity();
        //house2.setId(100);
        house4.setAddress("ID сам сгенерировался");
        System.out.println("ID из house4 (0): " +house4.getId());

        houseUtils.addHouse(house4);

        System.out.println("Выводим весь список домов");
        ArrayList<HousesEntity> houseList = (ArrayList<HousesEntity>) houseUtils.listHouse();
        for (HousesEntity house1 :
                houseList) {
            System.out.println(house1.getId() + ". Адрес: " + house1.getAddress() + ". Этажей: " + house1.getFloors() + ". Дата постройки: " + house1.getBuildDate());
        }

        System.out.println("Пытаемся удалить дом");
         houseUtils.removeHouse(3);

        System.out.println("Прошли удаление, выводим список после удаления");
            houseList = (ArrayList<HousesEntity>) houseUtils.listHouse();
            for (HousesEntity house3 :
                    houseList) {
                System.out.println("Адрес: " + house3.getAddress() + ". Этажей: " + house3.getFloors() + ". Дата постройки: " + house3.getBuildDate());
            }

        HibernateSessionFactory.shutdown();
    }
}
