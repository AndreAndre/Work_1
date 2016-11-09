package entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
@Entity
@Table(name = "apartments", schema = "public", catalog = "work_v1")
public class ApartmentsEntity {
    private Integer id;
    private Integer apartmentNumber;
    private Integer floor;

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    private Integer houseId;
    private Double square;
    private HousesEntity house;
    private List<PersonalAccountsEntity> personalAccountsEntity;
    public ApartmentsEntity() {
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "apartment_number", nullable = true)
    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(Integer apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Basic
    @Column(name = "floor", nullable = true)
    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    //@Basic
    //@Column(name = "house_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    public HousesEntity getHouse() {
        return this.house;
    }

    public void setHouse(HousesEntity house) {
        this.house = house;
    }

    @Basic
    @Column(name = "square", nullable = true, precision = 0)
    public Double getSquare() {
        return square;
    }

    public void setSquare(Double square) {
        this.square = square;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApartmentsEntity that = (ApartmentsEntity) o;

        if (id != that.id) return false;
        if (apartmentNumber != null ? !apartmentNumber.equals(that.apartmentNumber) : that.apartmentNumber != null)
            return false;
        if (floor != null ? !floor.equals(that.floor) : that.floor != null) return false;
        if (houseId != null ? !houseId.equals(that.houseId) : that.houseId != null) return false;
        if (square != null ? !square.equals(that.square) : that.square != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (apartmentNumber != null ? apartmentNumber.hashCode() : 0);
        result = 31 * result + (floor != null ? floor.hashCode() : 0);
        result = 31 * result + (houseId != null ? houseId.hashCode() : 0);
        result = 31 * result + (square != null ? square.hashCode() : 0);
        return result;
    }

    ApartmentsEntity(int num, int floor, HousesEntity house) {

    }

    @OneToMany(mappedBy = "apartmentsEntity")
    public List<PersonalAccountsEntity> getPersonalAccountsEntity() {
        return personalAccountsEntity;
    }

    public void setPersonalAccountsEntity(List<PersonalAccountsEntity> personalAccountsEntity) {
        this.personalAccountsEntity = personalAccountsEntity;
    }
}
