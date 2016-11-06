package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
@Entity
@Table(name = "personal_accounts", schema = "public", catalog = "work_v1")
public class PersonalAccountsEntity {
    private int id;
    private String accountNumber;
    private Integer apartmentId;
    private ApartmentsEntity apartmentsEntity;

    private List<ResidentsEntity> residentsEntity;

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
    @Column(name = "account_number", nullable = true, length = -1)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Basic
    @Column(name = "apartment_id", nullable = true, updatable = false, insertable = false)
    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalAccountsEntity that = (PersonalAccountsEntity) o;

        if (id != that.id) return false;
        if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null)
            return false;
        if (apartmentId != null ? !apartmentId.equals(that.apartmentId) : that.apartmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (apartmentId != null ? apartmentId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    public ApartmentsEntity getApartmentsEntity() {
        return apartmentsEntity;
    }

    public void setApartmentsEntity(ApartmentsEntity apartmentsEntity) {
        this.apartmentsEntity = apartmentsEntity;
    }

    @ManyToMany
    @JoinTable(name = "resident_to_account",
            //foreign key for CarsEntity in employee_car table
            joinColumns = @JoinColumn(name = "account_id"),
            //foreign key for other side - EmployeeEntity in employee_car table
            inverseJoinColumns = @JoinColumn(name = "resident_id"))
    public List<ResidentsEntity> getResidentsEntity() {
        return residentsEntity;
    }

    public void setResidentsEntity(List<ResidentsEntity> residentsEntity) {
        this.residentsEntity = residentsEntity;
    }

    public void addResident(ResidentsEntity resident) {
        residentsEntity.add(resident);
    }

    public PersonalAccountsEntity() {
        residentsEntity = new ArrayList<ResidentsEntity>();
    }
}
