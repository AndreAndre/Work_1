package entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
@Entity
@Table(name = "residents", schema = "public", catalog = "work_v1")
public class ResidentsEntity {
    @ManyToMany()
    private List<PersonalAccountsEntity> personalAccountsEntities;

    private int id;
    private String name;
    private String secondName;
    private String lastName;
    private Date birthday;
    private Integer gender;
    private Integer personalAccountId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "second_name", nullable = true, length = -1)
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = -1)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "birthday", nullable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "gender", nullable = true)
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "personal_account_id", nullable = true)
    public Integer getPersonalAccountId() {
        return personalAccountId;
    }

    public void setPersonalAccountId(Integer personalAccountId) {
        this.personalAccountId = personalAccountId;
    }


    public List<PersonalAccountsEntity> getPersonalAccountsEntities() {
        return personalAccountsEntities;
    }

    public void setPersonalAccountsEntities(List<PersonalAccountsEntity> personalAccountsEntities) {
        this.personalAccountsEntities = personalAccountsEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResidentsEntity that = (ResidentsEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (secondName != null ? !secondName.equals(that.secondName) : that.secondName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (personalAccountId != null ? !personalAccountId.equals(that.personalAccountId) : that.personalAccountId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (personalAccountId != null ? personalAccountId.hashCode() : 0);
        return result;
    }


}
