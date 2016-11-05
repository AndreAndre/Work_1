package views;

import org.junit.Test;

/**
 * Created by fedyu on 05.11.2016.
 * Простейший тест-пример для того, чтобы папка tests прикрепилась к гитхабу
 */
public class HousesViewTest {

    @Test
    public void test_1_plus_3_equal_4() {
        int a = 1;
        int b = 3;
        int e = a + b;
        org.junit.Assert.assertEquals("Тест 1 + 3",4,e);
    }
}
