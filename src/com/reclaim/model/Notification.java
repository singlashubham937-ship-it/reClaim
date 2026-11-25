package com.reclaim.model;

import java.time.LocalDateTime;

/**
 * Represents a user notification in the reClaim system.
 * Keeps students and finders informed about claim updates, verifications, and approvals.
 */
public class Notification {
    private int notificationId;
    private int recipientId;
    private String message;
    private LocalDateTime date;
    private boolean isRead;

    public Notification(int notificationId, int recipientId, String message) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.message = message;
        this.date = LocalDateTime.now();
        this.isRead = false;
    }

    public Notification(int notificationId, int recipientId, String message, LocalDateTime date, boolean isRead) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.message = message;
        this.date = date != null ? date : LocalDateTime.now();
        this.isRead = isRead;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void displayNotification() {
        String statusLabel = isRead ? "[READ]" : "[NEW] ";
        System.out.printf("%s #%-4d | %-19s | %s%n",
                statusLabel, notificationId, date.toString().replace('T', ' ').substring(0, 19), message);
    }

    @Override
    public String toString() {
        return "Notification [notificationId=" + notificationId + ", recipientId=" + recipientId 
                + ", message=" + message + ", date=" + date + ", isRead=" + isRead + "]";
    }
}
