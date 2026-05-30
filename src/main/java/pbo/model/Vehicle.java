package pbo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @Column(name = "plate_number", length = 225, nullable = false)
    private String plateNumber;

    @Column(name = "owner", length = 225, nullable = false)
    private String owner;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    public Vehicle() {
    }

    public Vehicle(String plateNumber, String owner, String type) {
        this.plateNumber = plateNumber;
        this.owner = owner;
        this.type = type;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return plateNumber + " " + owner + " " + type;
    }
}