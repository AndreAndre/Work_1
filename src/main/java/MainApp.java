import entities.HousesEntity;
import utils.HouseUtilsImpl;

import java.util.ArrayList;

/**
 * Created by fedyu on 04.11.2016.
 */
public class MainApp {
    public static void main(String[] args) {
        HouseUtilsImpl houseUtils = new HouseUtilsImpl();
        HousesEntity house = houseUtils.getHouse(0);
        //Выводим информацию о доме
        System.out.println("Адресссук: " + house.getAddress() + ". Этажей: " + house.getFloors() + ". Дата постройки: " + house.getBuildDate());

        System.out.println("---");
        HousesEntity house2 = new HousesEntity();
        house2.setAddress("Дворцовый переулок, 12");
        house2.setId(3);
        houseUtils.addHouse(house2);
        System.out.println("---");
        ArrayList<HousesEntity> houseList = (ArrayList<HousesEntity>) houseUtils.listHouse();
        for (HousesEntity house1 :
                houseList) {
            System.out.println("Адресссук: " + house1.getAddress() + ". Этажей: " + house1.getFloors() + ". Дата постройки: " + house1.getBuildDate());
        }
        System.out.println("---");
         houseUtils.removeHouse(3);
        System.out.println("---");
            houseList = (ArrayList<HousesEntity>) houseUtils.listHouse();
            for (HousesEntity house3 :
                    houseList) {
                System.out.println("Адресссук: " + house3.getAddress() + ". Этажей: " + house3.getFloors() + ". Дата постройки: " + house3.getBuildDate());
            }
    }
}
