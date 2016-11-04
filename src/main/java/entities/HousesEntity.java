package entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
@Entity
@Table(name = "houses", schema = "public", catalog = "work_v1")
public class HousesEntity {
    private int id;
    private String address;
    private Integer floors;
    private Date buildDate;
    private List<ApartmentsEntity> apartmentsList;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "address", nullable = true, length = -1)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "floors", nullable = true)
    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    @Basic
    @Column(name = "build_date", nullable = true)
    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HousesEntity that = (HousesEntity) o;

        if (id != that.id) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (floors != null ? !floors.equals(that.floors) : that.floors != null) return false;
        if (buildDate != null ? !buildDate.equals(that.buildDate) : that.buildDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (floors != null ? floors.hashCode() : 0);
        result = 31 * result + (buildDate != null ? buildDate.hashCode() : 0);
        return result;
    }

    public HousesEntity (String address, int floors, Date buildDate, List<ApartmentsEntity> apartmentsList) {
        this(address, floors, buildDate);
        this.apartmentsList = apartmentsList;
    }

    public HousesEntity (String address, int floors, Date buildDate) {
        this.address = address;
        this.floors = floors;
        this.buildDate = buildDate;
    }

    public HousesEntity () {
        address = "адрес не указан";
        floors = 0;
    }

    public void addApartment (int num, int floor) {
        ApartmentsEntity apartment = new ApartmentsEntity(num, floor, this);
        apartmentsList.add(apartment);
    }

    public boolean removeApartment (int num){
        for (ApartmentsEntity apartment : apartmentsList) {
            if (apartment.getApartmentNumber() == num) {
                apartmentsList.remove(apartment);
                return true;
            }
        }
        return false;
    }

    public void addApartments (List<ApartmentsEntity> list) {
        for (ApartmentsEntity apartment : list) {
            apartment.setHouse(this);
            apartmentsList.add(apartment);
        }
    }

    public boolean removeApartments (List<ApartmentsEntity> list, HousesEntity House) {

        Boolean bool = false;
        List<ApartmentsEntity> notFoundApartments = new ArrayList<ApartmentsEntity>();

        for (ApartmentsEntity apartmentForRemove : list) {           //перебираем лист с квартирами на удаление,

            bool = false;
            for (ApartmentsEntity apartment : apartmentsList) {      //затем перебираем квартиры в доме, ищем нужную квартиру.
                if (apartment.equals(apartmentForRemove)) {          //если нашли квартиру в доме, то
                    apartmentsList.remove(apartment);                //удаляем ее из дома
                    apartment.setHouse(null);
                    bool = true;                                     //и говорим что все норм
                    break;
                }
            }
            if (!bool) notFoundApartments.add(apartmentForRemove);   //Если не нашли квартиру в доме, то откладываем ее.
        }
        if (notFoundApartments.isEmpty()) return true;
        return false;
    }
}
