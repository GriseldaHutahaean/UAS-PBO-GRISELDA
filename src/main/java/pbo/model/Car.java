package pbo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue; //untuk menentukan bahwa nilai dari field ini akan dihasilkan secara otomatis oleh database
import jakarta.persistence.GenerationType; //utku menentukan strategi penghasil nilai untuk primary key
import jakarta.persistence.Id; 
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne; /
import jakarta.persistence.Table;

@Entity
@Table(name = "parked_vehicles")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "plate_number", length = 225, nullable = false)
    private String plateNumber;

    @Column(name = "owner", length = 225, nullable = false)
    private String owner;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "parking_area_name", nullable = false)
    private Area parkingArea;

    public Car() {
    }

    public Car(String plateNumber, String owner, String type, Area parkingArea) {
        this.plateNumber = plateNumber;
        this.owner = owner;
        this.type = type;
        this.parkingArea = parkingArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Area getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(Area parkingArea) {
        this.parkingArea = parkingArea;
    }

    @Override
    public String toString() {
        return plateNumber + " " + owner + " " + type;
    }
}