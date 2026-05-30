package pbo.model;

public class MotorCycle extends Vehicle {
    private static final String VEHICLE_TYPE = "Motor";

    public MotorCycle(String plate_number, String owner, String type) {
        super(plate_number, owner, type);
    }

    public String getVehicleType() {
        return VEHICLE_TYPE;
    }

    @Override
    public String toString() {
        return getPlateNumber() + " " + getOwner() + " " + getType();
    }

}