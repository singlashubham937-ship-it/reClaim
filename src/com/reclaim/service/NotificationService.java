package com.reclaim.service;

import com.reclaim.model.Notification;

import java.util.ArrayList;

/**
 * Service managing user notifications for claims, verifications, and handovers.
 */
public class NotificationService {
    private final ArrayList<Notification> notifications;
    private int nextNotificationId = 1;

    public NotificationService() {
        this.notifications = new ArrayList<>();
    }

    /**
     * Sends a new notification to a recipient user.
     *
     * @param recipientId ID of the student or finder.
     * @param message     Notification text.
     * @return The created Notification object.
     */
    public synchronized Notification sendNotification(int recipientId, String message) {
        Notification notification = new Notification(nextNotificationId++, recipientId, message);
        notifications.add(notification);
        return notification;
    }

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param recipientId The user ID.
     * @return List of notifications for that user.
     */
    public ArrayList<Notification> getNotificationsByUser(int recipientId) {
        ArrayList<Notification> userNotifications = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientId() == recipientId) {
                userNotifications.add(n);
            }
        }
        return userNotifications;
    }

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId The notification ID.
     * @return true if found and marked as read, false otherwise.
     */
    public boolean markNotificationAsRead(int notificationId) {
        for (Notification n : notifications) {
            if (n.getNotificationId() == notificationId) {
                n.markAsRead();
                return true;
            }
        }
        return false;
    }

    /**
     * Displays all notifications for a given user in a formatted console view.
     *
     * @param recipientId The user ID.
     */
    public void displayNotifications(int recipientId) {
        ArrayList<Notification> userNotifications = getNotificationsByUser(recipientId);
        if (userNotifications.isEmpty()) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("No notifications found for User ID " + recipientId + ".");
            System.out.println("--------------------------------------------------------------------------------");
            return;
        }

        System.out.println("================================================================================");
        System.out.println("                    NOTIFICATIONS (User ID: " + recipientId + ")                        ");
        System.out.println("================================================================================");
        for (Notification n : userNotifications) {
            n.displayNotification();
        }
        System.out.println("================================================================================");
    }

    /**
     * Returns all notifications across the system (useful for testing and admin auditing).
     */
    public ArrayList<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }
}
