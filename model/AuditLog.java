package model;

import java.time.LocalDateTime;

/**
 * Simple audit log entry for actions performed in the application.
 */
public class AuditLog {
    private String logId;
    private String userId;
    private String action;
    private String description;
    private LocalDateTime dateTime;

    public AuditLog(String logId, String userId, String action, String description, LocalDateTime dateTime) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    @Override
    public String toString() {
        return "AuditLog{" +
                "logId='" + logId + '\'' +
                ", userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
