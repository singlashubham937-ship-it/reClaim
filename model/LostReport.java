package model;

/**
 * Represents a LostReport – a report submitted by a student
 * who has lost an item and wants the system to track it.
 *
 * OOP concept demonstrated: Encapsulation
 */
public class LostReport {

    private String reportId;
    private String reportedByUserId; // The student who lost the item
    private String itemName;
    private String description;
    private String location;         // Where the item was last seen
    private String reportDate;       // Date as a formatted string

    /**
     * Full constructor.
     */
    public LostReport(String reportId, String reportedByUserId, String itemName,
                      String description, String location, String reportDate) {
        this.reportId = reportId;
        this.reportedByUserId = reportedByUserId;
        this.itemName = itemName;
        this.description = description;
        this.location = location;
        this.reportDate = reportDate;
    }

    // ---- Getters & Setters ----

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getReportedByUserId() { return reportedByUserId; }
    public void setReportedByUserId(String reportedByUserId) { this.reportedByUserId = reportedByUserId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    @Override
    public String toString() {
        return "LostReport{" +
                "reportId='" + reportId + '\'' +
                ", reportedByUserId='" + reportedByUserId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", location='" + location + '\'' +
                ", reportDate='" + reportDate + '\'' +
                '}';
    }
}
