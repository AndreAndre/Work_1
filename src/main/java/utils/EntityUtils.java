package utils;

import entities.HousesEntity;

import java.util.List;

/**
 * Created by fedyu on 04.11.2016.
 */
public interface EntityUtils {
    public void add(Object house);
    public Object get(Class objClass, int id);
    public void remove(Class objClass, int id);
    public List list(Class objClass);
}
