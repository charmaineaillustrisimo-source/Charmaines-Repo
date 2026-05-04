/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.History;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author macbookairm1grey
 */
public interface IHistoryService {
    void logBrowsingActivity(int userId, int carId) throws SQLException;

    List<History> getBrowsingHistory(int userId) throws SQLException;

    List<History> getRentalHistory(int userId) throws SQLException;
}
