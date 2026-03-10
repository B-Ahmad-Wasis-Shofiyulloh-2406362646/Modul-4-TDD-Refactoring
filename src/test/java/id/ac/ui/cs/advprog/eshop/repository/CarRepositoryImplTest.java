package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CarRepositoryImplTest {

    private CarRepositoryImpl repository;
    private Car car;

    @BeforeEach
    void setUp() {
        repository = new CarRepositoryImpl();
        car = new Car();
        car.setCarId("car-1");
        car.setCarName("Tesla");
        car.setCarColor("Blue");
        car.setCarQuantity(2);
    }

    @Test
    void testCreateAndFindAll() {
        repository.create(car);

        Iterator<Car> iterator = repository.findAll();
        assertNotNull(iterator);
        assertEquals("car-1", iterator.next().getCarId());
    }

    @Test
    void testFindById() {
        repository.create(car);

        Car found = repository.findById("car-1");
        assertNotNull(found);
        assertEquals("Tesla", found.getCarName());
        assertNull(repository.findById("missing"));
    }

    @Test
    void testUpdateFound() {
        repository.create(car);

        Car updated = new Car();
        updated.setCarId("car-1");
        updated.setCarName("BMW");
        updated.setCarColor("Gray");
        updated.setCarQuantity(7);

        Car result = repository.update(updated);

        assertNotNull(result);
        assertEquals("BMW", result.getCarName());
        assertEquals("Gray", result.getCarColor());
        assertEquals(7, result.getCarQuantity());
    }

    @Test
    void testUpdateNotFound() {
        Car updated = new Car();
        updated.setCarId("missing");

        Car result = repository.update(updated);

        assertNull(result);
    }

    @Test
    void testDelete() {
        repository.create(car);

        repository.delete("car-1");

        assertNull(repository.findById("car-1"));
    }
}
