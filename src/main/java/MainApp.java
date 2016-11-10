import database.HibernateSessionFactory;
import entities.ApartmentsEntity;
import entities.HousesEntity;
import entities.PersonalAccountsEntity;
import entities.ResidentsEntity;
import utils.EntityUtils;
import utils.EntityUtilsImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
public class MainApp {
    public static void main(String[] args) {
        EntityUtilsImpl entityUtils = new EntityUtilsImpl();


        List<Object> houses = entityUtils.list(HousesEntity.class) ;
/*
        for (Object house :
                houses) {
            HousesEntity houseEntity = (HousesEntity) house;
            System.out.println("Адрес: " + houseEntity.getAddress());
        }
*/
        HousesEntity house = (HousesEntity) entityUtils.get(HousesEntity.class, 0);
        //Выводим информацию о доме
        System.out.println("Выводим информацию о доме с id = 0");
        System.out.println("Адрес: " + house.getAddress() + ". Этажей: " + house.getFloors() + ". Дата постройки: " + house.getBuildDate());





        ApartmentsEntity newApartment = new ApartmentsEntity();
        newApartment.setHouse(house); //id=0
        newApartment.setHouseId(100);
        newApartment.setFloor(8);
        newApartment.setApartmentNumber(1999);
        newApartment.setSquare(55.5);
        entityUtils.add(newApartment);

        List<ApartmentsEntity> apartments = house.getApartmentsEntity();

        PersonalAccountsEntity pers = new PersonalAccountsEntity();
        pers.setApartmentsEntity(apartments.get(1));
        pers.setAccountNumber("А123-ОУ-77726");

        ResidentsEntity resident = new ResidentsEntity();
        resident.setName("Павлик");
        entityUtils.add(resident);
        pers.addResident(resident);

        resident = new ResidentsEntity();
        resident.setName("Борис");
        entityUtils.add(resident);
        pers.addResident(resident);

        entityUtils.add(pers);

        pers.addResident(resident);


        for (ApartmentsEntity apartment :
                apartments) {
            System.out.println("->  Выводим квартиры из домов");
            System.out.println("    Номер: " + apartment.getApartmentNumber());
            System.out.println("    Этаж: " + apartment.getFloor());
            System.out.println("    Площадь: " + apartment.getSquare());
            System.out.println("---");

            List<PersonalAccountsEntity> persAccounts = apartment.getPersonalAccountsEntity();
            for (PersonalAccountsEntity persAccount:persAccounts) {
                System.out.println("  ->  Лицевые счета: ");
                System.out.println("      Номер: " + persAccount.getAccountNumber());
                System.out.println("---");


                List<ResidentsEntity> residents = persAccount.getResidentsEntity();

                for (ResidentsEntity resident1 : residents) {
                    System.out.println("        --> Жильцы:");
                    System.out.println("Name: " + resident1.getName());
                }
            }
        }

        HibernateSessionFactory.shutdown();
    }
}
