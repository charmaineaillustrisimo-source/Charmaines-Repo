/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.services;
import carrentalsystem.core.DBConnection;
import carrentalsystem.interfaces.ICarService;
import carrentalsystem.models.Car;
import java.sql.*;
import java.util.*;

/**
 *
 * @author macbookairm1grey
 */
public class CarService implements ICarService{
    @Override
    public List<Car> getAvailableCars(String keyword) throws SQLException {
        String sql = "SELECT * FROM cars WHERE status = 'ACTIVE' "
                + "ORDER BY is_priority DESC, created_at ASC";
        List<Car> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCar(rs));
            }
        }
        return list;
    }

    @Override
    public List<Car> searchCars(String keyword) throws SQLException {
        String sql = "SELECT * FROM cars WHERE status = 'ACTIVE' "
                + "AND (model LIKE ? OR brand LIKE ?) "
                + "ORDER BY is_priority DESC, created_at ASC";
        List<Car> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCar(rs));
            }
        }
        return list;
    }

    @Override
    public int addCar(Car car) throws SQLException {
        // Exactly 14 columns to be inserted
        String sql = "INSERT INTO cars (owner_id, brand, model, type, seats, "
                + "fuel_type, transmission, car_condition, description, base_price, "
                + "image_path, views_count, is_priority, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // FIXED: Indices are now sequential 1 through 14
            ps.setInt(1, car.getOwnerId());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setString(4, car.getType());
            ps.setInt(5, car.getSeats());
            ps.setString(6, car.getFuelType());
            ps.setString(7, car.getTransmission());
            ps.setString(8, car.getCondition()); // Maps to car_condition
            ps.setString(9, car.getDescription());
            ps.setDouble(10, car.getBasePrice());
            ps.setString(11, car.getImagePath());
            ps.setInt(12, 0); // views_count defaults to 0
            ps.setBoolean(13, car.isPriority());
            ps.setString(14, "PENDING_APPROVAL"); // Default status upon creation

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean updateCar(Car car) throws SQLException {
        // Removed year, color, plate_number, mileage_limit, etc.
        String sql = "UPDATE cars SET brand=?, model=?, type=?, seats=?, fuel_type=?, "
                + "transmission=?, car_condition=?, description=?, base_price=?, "
                + "image_path=?, status=? WHERE car_id=?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setString(3, car.getType());
            ps.setInt(4, car.getSeats());
            ps.setString(5, car.getFuelType());
            ps.setString(6, car.getTransmission());
            ps.setString(7, car.getCondition());
            ps.setString(8, car.getDescription());
            ps.setDouble(9, car.getBasePrice());
            ps.setString(10, car.getImagePath());
            ps.setString(11, car.getStatus());
            ps.setInt(12, car.getCarId());
            ps.executeUpdate();
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void archiveCar(int carId) throws SQLException {
        String sql = "UPDATE cars SET status = 'ARCHIVED' WHERE car_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.executeUpdate();
        }
    }

    @Override
    public Car getCarById(int carId) throws SQLException {
        String sql = "SELECT * FROM cars WHERE car_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCar(rs);
            }
        }
        return null;
    }

    @Override
    public void incrementViews(int carId) throws SQLException {
        String sql = "UPDATE cars SET views_count = views_count + 1 WHERE car_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Car> getCarsByOwner(int ownerId) throws SQLException {
        String sql = "SELECT * FROM cars WHERE owner_id = ? "
                + "AND status != 'ARCHIVED' ORDER BY created_at DESC";
        List<Car> list = new ArrayList<>();
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCar(rs));
            }
        }
        return list;
    }

    // ── Helper ───────────────────────────────────────────────
    private Car mapCar(ResultSet rs) throws SQLException {
        Car c = new Car();
        c.setCarId(rs.getInt("car_id"));
        c.setOwnerId(rs.getInt("owner_id"));
        c.setBrand(rs.getString("brand"));
        c.setModel(rs.getString("model"));
        c.setType(rs.getString("type"));
        c.setSeats(rs.getInt("seats"));
        c.setFuelType(rs.getString("fuel_type"));
        c.setTransmission(rs.getString("transmission"));
        c.setCondition(rs.getString("car_condition")); // Correctly mapping to DB column
        c.setDescription(rs.getString("description"));
        c.setBasePrice(rs.getDouble("base_price"));
        c.setImagePath(rs.getString("image_path"));
        c.setViewsCount(rs.getInt("views_count"));
        c.setPriority(rs.getBoolean("is_priority"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
