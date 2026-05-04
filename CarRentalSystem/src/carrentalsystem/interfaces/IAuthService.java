/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;
import carrentalsystem.models.User;
import java.sql.SQLException;
/**
 *
 * @author macbookairm1grey
 */
public interface IAuthService {
    User login(String email, String password) throws SQLException;

    void register(String fullName, String username, String email, String password, String role) throws SQLException;

    void sendPasswordReset(String email) throws SQLException;

    void banUser(String email) throws SQLException;
    
    boolean usernameExists(String username) throws SQLException;
    
    boolean resetPassword(String username, String newPassword) throws SQLException;
}
