package pbo.f01;

import java.util.List;
import java.util.Scanner;
import jakarta.persistence.EntityManager; //untuk mengelola entitas dan melakukan operasi CRUD pada database
import jakarta.persistence.EntityManagerFactory; //untuk membuat instance EntityManager yang terhubung ke database
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import pbo.model.Vehicle;
import pbo.model.Area;
import pbo.model.Car;

/**
 * Driver class utama
 * Nama: Griselda Tabitha Nathania Hutahaean
 * Nim: 12S24026
 */

public class App {
    private static EntityManagerFactory entityManagerFactory; //untuk mengelola EntityManager
    private static EntityManager entityManager; //untuk melakukan operasi CRUD pada database

    public static void main(String[] args) {

        entityManagerFactory = Persistence.createEntityManagerFactory("parkit-pu"); 
        entityManager = entityManagerFactory.createEntityManager(); 

        Scanner scanner = new Scanner(System.in);

        // Membaca input baris per baris hingga tidak ada token atau teks lagi
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            
            if (input == null || input.trim().isEmpty()) {
                break;
            }
            
            String[] data = input.split("#");
            String command = data[0];

            switch (command) {

                case "area-add":
                    handleAreaAdd(data);
                    break;

                case "vehicle-add":
                    handleVehicleAdd(data);
                    break;

                case "park":
                    handlePark(data);
                    break;

                case "display-all":
                    displayAll();
                    break;

                default:
                    // Silently ignore invalid commands
                    break;
            }
        }

        scanner.close();
        entityManager.close();
        entityManagerFactory.close();
    }

    // Method untuk menangani penambahan area parkir
    private static void handleAreaAdd(String[] data) {
        // Format: area-add#<name>#<capacity>#<allowed_type>
        if (data.length < 4) return;
        
        String name = data[1];
        int capacity = Integer.parseInt(data[2]);
        String allowedType = data[3];
        
        entityManager.getTransaction().begin();
        try {
           // cek apakah area dengan nama yang sama sudah ada
            Query query = entityManager.createQuery("SELECT a FROM Area a WHERE a.name = :name");
            query.setParameter("name", name); 
            List<Area> existingAreas = query.getResultList();// jika tidak ada area dengan nama yang sama, buat area baru
            
            if (existingAreas.isEmpty()) {
                Area area = new Area(name, capacity, allowedType);
                entityManager.persist(area);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    // Method untuk menangani penambahan kendaraan
    private static void handleVehicleAdd(String[] data) {
        // Format: vehicle-add#<plate_number>#<owner>#<type>
        if (data.length < 4) return;
        
        String plateNumber = data[1];
        String owner = data[2];
        String type = data[3];
        
        entityManager.getTransaction().begin();
        try {
            // cek apakah kendaraan dengan nomor plat yang sama sudah ada
            Query query = entityManager.createQuery("SELECT v FROM Vehicle v WHERE v.plateNumber = :plateNumber");
            query.setParameter("plateNumber", plateNumber);
            List<Vehicle> existingVehicles = query.getResultList();
            
            if (existingVehicles.isEmpty()) { 
                Vehicle vehicle = new Vehicle(plateNumber, owner, type);
                entityManager.persist(vehicle);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    // Method untuk menangani proses parkir kendaraan
    private static void handlePark(String[] data) {
        // Format: park#<plate_number>#<area_name>
        if (data.length < 3) return;
        
        String plateNumber = data[1];
        String areaName = data[2];
        
        entityManager.getTransaction().begin();
        try {
            //menemukan kendaraan berdasarkan nomor plat
            Query vehicleQuery = entityManager.createQuery("SELECT v FROM Vehicle v WHERE v.plateNumber = :plateNumber");
            vehicleQuery.setParameter("plateNumber", plateNumber);
            List<Vehicle> vehicles = vehicleQuery.getResultList();
            
            if (vehicles.isEmpty()) {
                entityManager.getTransaction().rollback();
                return; // Kendaraan tidak ditemukan
            }
            
            Vehicle vehicle = vehicles.get(0); // Ambil kendaraan pertama yang ditemukan 
            
            //menemukan area parkir berdasarkan nama area
            Query areaQuery = entityManager.createQuery("SELECT a FROM Area a WHERE a.name = :name");
            areaQuery.setParameter("name", areaName);
            List<Area> areas = areaQuery.getResultList();
            
            if (areas.isEmpty()) {
                entityManager.getTransaction().rollback();
                return; // Area tidak ditemukan
            }
            
            Area area = areas.get(0);
            
            //Validasi: jenis kendaraan sesuai dengan jenis yang diizinkan di area parkir
            if (!vehicle.getType().equals(area.getAllowedType())) {
                entityManager.getTransaction().rollback();
                return; 
            }
            
            // Validasi: kapasitas area parkir belum penuh
            if (area.getParkedCount() >= area.getCapacity()) {
                entityManager.getTransaction().rollback();
                return;
            }
            
            // Validasi: kendaraan belum diparkir di area manapun
            Query parkQuery = entityManager.createQuery("SELECT c FROM Car c WHERE c.plateNumber = :plateNumber");
            parkQuery.setParameter("plateNumber", plateNumber);
            List<Car> parkedVehicles = parkQuery.getResultList();
            
            
            if (!parkedVehicles.isEmpty()) { //
                entityManager.getTransaction().rollback();
                return; // Kendaraan sudah diparkir di area lain
            }
            
            Car parkedCar = new Car(vehicle.getPlateNumber(), vehicle.getOwner(), vehicle.getType(), area);
            entityManager.persist(parkedCar);
            area.getParkedVehicles().add(parkedCar);
            
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    private static void displayAll() {
        //menampilkan semua area beserta kendaraan yang diparkir di dalamnya
        //diurutkan berdasarkan nama area secara ascending. Untuk setiap area, tampilkan informasi area tersebut diikuti dengan daftar kendaraan yang diparkir di dalamnya, diurutkan berdasarkan nomor plat secara ascending.
        try {
            Query query = entityManager.createQuery("SELECT a FROM Area a ORDER BY a.name ASC");
            List<Area> areas = query.getResultList();
            
            for (Area area : areas) {
                //menampilkan informasi area
                System.out.println(area);
                
                //menampilkan daftar kendaraan yang diparkir di dalam area tersebut, diurutkan berdasarkan nomor plat secara ascending
                Query carQuery = entityManager.createQuery(
                    "SELECT c FROM Car c WHERE c.parkingArea.name = :areaName ORDER BY c.plateNumber ASC"
                );
                carQuery.setParameter("areaName", area.getName()); //mengambil daftar kendaraan yang diparkir di area tersebut
                List<Car> cars = carQuery.getResultList();
                
                for (Car car : cars) {
                    System.out.println(car);
                }
            }
        } catch (Exception e) {
            // Silently handle errors
        }
    }
}