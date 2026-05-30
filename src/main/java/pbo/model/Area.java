package pbo.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "parking_areas")
public class Area {
    
    @Id
    @Column(name = "name", length = 225, nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "allowed_type", length = 50, nullable = false)
    private String allowedType;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL)
    private List<Car> parkedVehicles = new ArrayList<>();

    public Area() {
    }

    public Area(String name, int capacity, String allowedType) {
        this.name = name;
        this.capacity = capacity;
        this.allowedType = allowedType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAllowedType() {
        return allowedType;
    }

    public void setAllowedType(String allowedType) {
        this.allowedType = allowedType;
    }

    public List<Car> getParkedVehicles() {
        return parkedVehicles;
    }

    public void setParkedVehicles(List<Car> parkedVehicles) {
        this.parkedVehicles = parkedVehicles;
    }

    public int getParkedCount() {
        return parkedVehicles.size();
    }

    @Override
    public String toString() {
        return name + " " + allowedType + " " + capacity + "|" + getParkedCount();
    }
}