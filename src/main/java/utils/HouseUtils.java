package utils;

import entities.HousesEntity;

import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
public interface HouseUtils {
    public void addHouse(Object house);
    public HousesEntity getHouse(int id);
    public void removeHouse(int id);
    public List<HousesEntity> listHouse();

}
