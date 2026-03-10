package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarTest {

    @Test
    void testCreateCar() {
        Car car = new Car();
        car.setCarName("Toyota");
        car.setCarColor("Black");
        car.setCarQuantity(3);

        assertNotNull(car.getCarId());
        assertEquals("Toyota", car.getCarName());
        assertEquals("Black", car.getCarColor());
        assertEquals(3, car.getCarQuantity());
    }

    @Test
    void testUpdateFrom() {
        Car base = new Car();
        base.setCarName("Old");
        base.setCarColor("White");
        base.setCarQuantity(1);

        Car updated = new Car();
        updated.setCarName("New");
        updated.setCarColor("Red");
        updated.setCarQuantity(5);

        base.updateFrom(updated);

        assertEquals("New", base.getCarName());
        assertEquals("Red", base.getCarColor());
        assertEquals(5, base.getCarQuantity());
    }
}
