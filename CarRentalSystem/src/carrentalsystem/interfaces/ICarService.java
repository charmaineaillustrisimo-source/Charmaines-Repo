/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.Car;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public interface ICarService {
    List<Car> getAvailableCars(String keyword) throws SQLException; // ORDER BY is_priority DESC

    List<Car> searchCars(String keyword) throws SQLException; // for search bar

    int addCar(Car car) throws SQLException;

    void updateCar(Car car) throws SQLException;

    void archiveCar(int carId) throws SQLException;

    Car getCarById(int carId) throws SQLException; // for car detail page

    void incrementViews(int carId) throws SQLException;

    List<Car> getCarsByOwner(int ownerId) throws SQLException;
}
