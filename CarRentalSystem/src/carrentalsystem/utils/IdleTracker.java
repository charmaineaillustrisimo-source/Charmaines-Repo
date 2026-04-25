/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrentalsystem.utils;
import carrentalsystem.core.AppConstants;
import carrentalsystem.core.SessionManager;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.Timer;

/**
 * Tracks user inactivity. If no mouse or keyboard event is detected
 * within IDLE_TIMEOUT_MS (10 minutes), marks the session as IDLE in the DB.
 * Any activity resets the timer and marks the session ONLINE again.
 *
 * HOW TO USE — call this right after SessionManager.startSession():
 *   new IdleTracker().start();
 */

public class IdleTracker {
    // Swing Timer — runs on the Event Dispatch Thread (safe for DB calls)
    private Timer idleTimer;
    private boolean isIdle = false;

    public void start() {
        // ── Timer: fires every 60 seconds to check elapsed time ──
        idleTimer = new Timer(60_000, e -> checkIdle());
        idleTimer.setRepeats(true);
        idleTimer.start();

        // ── AWT Listener: catches ALL mouse + keyboard events globally ──
        long eventMask = AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK
                | AWTEvent.KEY_EVENT_MASK;

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                onActivity();
            }
        }, eventMask);

        System.out.println("[IdleTracker] Started. Timeout = "
                + (AppConstants.IDLE_TIMEOUT_MS / 60000) + " minutes.");
    }

    /**
     * Called every 60 seconds by the Timer. Checks if the idle timer has
     * exceeded IDLE_TIMEOUT_MS.
     */
    private void checkIdle() {
        if (!isIdle) {
            isIdle = true;
            try {
                SessionManager.setIdle();
                System.out.println("[IdleTracker] Session marked IDLE.");
            } catch (Exception e) {
                System.err.println("[IdleTracker] Failed to set IDLE: " + e.getMessage());
            }
        }
    }

    /**
     * Called on any mouse or keyboard activity. Resets the idle timer and marks
     * session ONLINE if it was IDLE.
     */
    private void onActivity() {
        if (isIdle) {
            isIdle = false;
            try {
                SessionManager.setOnline();
                System.out.println("[IdleTracker] Session marked ONLINE.");
            } catch (Exception e) {
                System.err.println("[IdleTracker] Failed to set ONLINE: " + e.getMessage());
            }
        }
        // Reset the countdown
        idleTimer.restart();
    }

    /**
     * Stop tracking — call this when the user logs out. Called automatically by
     * SessionManager.endSession() flow.
     */
    public void stop() {
        if (idleTimer != null) {
            idleTimer.stop();
        }
        System.out.println("[IdleTracker] Stopped.");
    }
}
