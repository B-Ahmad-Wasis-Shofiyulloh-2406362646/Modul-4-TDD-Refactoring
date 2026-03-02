package id.ac.ui.cs.advprog.eshop.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Car {
    private String carId;
    private String carName;
    private String carColor;
    private int carQuantity;

    public Car() {
        this.carId = UUID.randomUUID().toString();
    }

    public void updateFrom(Car other) {
        this.carName = other.getCarName();
        this.carColor = other.getCarColor();
        this.carQuantity = other.getCarQuantity();
    }
}