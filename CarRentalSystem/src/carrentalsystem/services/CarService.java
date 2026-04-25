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
        String sql = "INSERT INTO cars (owner_id, model, brand, year, plate_number, "
                + "color, seats, fuel_type, description, base_price, mileage_limit, "
                + "fuel_policy, house_rules, image_path, is_priority) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, car.getOwnerId());
            ps.setString(2, car.getModel());
            ps.setString(3, car.getBrand());
            ps.setInt(4, car.getYear());
            ps.setString(5, car.getPlateNumber());
            ps.setString(6, car.getColor());
            ps.setInt(7, car.getSeats());
            ps.setString(8, car.getFuelType());
            ps.setString(9, car.getDescription());
            ps.setDouble(10, car.getBasePrice());
            ps.setInt(11, car.getMileageLimit());
            ps.setString(12, car.getFuelPolicy());
            ps.setString(13, car.getHouseRules());
            ps.setString(14, car.getImagePath());
            ps.setBoolean(15, car.isPriority());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public void updateCar(Car car) throws SQLException {
        String sql = "UPDATE cars SET model=?, brand=?, year=?, color=?, seats=?, "
                + "fuel_type=?, description=?, base_price=?, mileage_limit=?, "
                + "fuel_policy=?, house_rules=?, image_path=? WHERE car_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, car.getModel());
            ps.setString(2, car.getBrand());
            ps.setInt(3, car.getYear());
            ps.setString(4, car.getColor());
            ps.setInt(5, car.getSeats());
            ps.setString(6, car.getFuelType());
            ps.setString(7, car.getDescription());
            ps.setDouble(8, car.getBasePrice());
            ps.setInt(9, car.getMileageLimit());
            ps.setString(10, car.getFuelPolicy());
            ps.setString(11, car.getHouseRules());
            ps.setString(12, car.getImagePath());
            ps.setInt(13, car.getCarId());
            ps.executeUpdate();
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
        c.setModel(rs.getString("model"));
        c.setBrand(rs.getString("brand"));
        c.setYear(rs.getInt("year"));
        c.setPlateNumber(rs.getString("plate_number"));
        c.setColor(rs.getString("color"));
        c.setSeats(rs.getInt("seats"));
        c.setFuelType(rs.getString("fuel_type"));
        c.setDescription(rs.getString("description"));
        c.setBasePrice(rs.getDouble("base_price"));
        c.setMileageLimit(rs.getInt("mileage_limit"));
        c.setFuelPolicy(rs.getString("fuel_policy"));
        c.setHouseRules(rs.getString("house_rules"));
        c.setImagePath(rs.getString("image_path"));
        c.setViewsCount(rs.getInt("views_count"));
        c.setPriority(rs.getBoolean("is_priority"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
