package entities;

import javax.persistence.*;
import java.sql.Date;

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

    @Id
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
}
