package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl service;

    @Mock
    private CarRepository repository;

    @Test
    void testCreate() {
        Car car = new Car();
        when(repository.create(car)).thenReturn(car);

        Car result = service.create(car);

        assertSame(car, result);
        verify(repository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        Car car1 = new Car();
        Car car2 = new Car();
        Iterator<Car> iterator = Arrays.asList(car1, car2).iterator();
        when(repository.findAll()).thenReturn(iterator);

        List<Car> result = service.findAll();

        assertEquals(2, result.size());
        assertSame(car1, result.get(0));
        assertSame(car2, result.get(1));
    }

    @Test
    void testFindById() {
        Car car = new Car();
        when(repository.findById("car-1")).thenReturn(car);

        Car result = service.findById("car-1");

        assertSame(car, result);
    }

    @Test
    void testUpdate() {
        Car car = new Car();

        service.update(car);

        verify(repository, times(1)).update(car);
    }

    @Test
    void testDelete() {
        service.delete("car-1");

        verify(repository, times(1)).delete("car-1");
    }
}
