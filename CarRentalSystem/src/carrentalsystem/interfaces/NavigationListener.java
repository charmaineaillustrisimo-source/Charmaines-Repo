/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carrentalsystem.interfaces;

/**
 *
 * @author macbookairm1grey
 */
public interface NavigationListener {
    /**
     * Triggered when a menu item is clicked.
     * @param cardName The unique name of the card to show (e.g., "discovery", "addListing")
     */
    void onNavigate(String cardName);
}
