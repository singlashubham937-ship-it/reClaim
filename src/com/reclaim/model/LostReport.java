package com.reclaim.model;

import java.time.LocalDate;

public class LostReport {
    private int reportId;
    private int itemId;
    private int finderId;
    private LocalDate dateReported;
    private String reportDescription;

    public LostReport(int reportId, int itemId, int finderId, LocalDate dateReported, String reportDescription) {
        this.reportId = reportId;
        this.itemId = itemId;
        this.finderId = finderId;
        this.dateReported = (dateReported != null) ? dateReported : LocalDate.now();
        this.reportDescription = reportDescription;
    }

    public LostReport(int reportId, int itemId, int finderId, String reportDescription) {
        this(reportId, itemId, finderId, LocalDate.now(), reportDescription);
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        if (reportId > 0) {
            this.reportId = reportId;
        }
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        if (itemId > 0) {
            this.itemId = itemId;
        }
    }

    public int getFinderId() {
        return finderId;
    }

    public void setFinderId(int finderId) {
        if (finderId > 0) {
            this.finderId = finderId;
        }
    }

    public LocalDate getDateReported() {
        return dateReported;
    }

    public void setDateReported(LocalDate dateReported) {
        if (dateReported != null) {
            this.dateReported = dateReported;
        }
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        if (reportDescription != null && !reportDescription.trim().isEmpty()) {
            this.reportDescription = reportDescription;
        }
    }

    @Override
    public String toString() {
        return "LostReport [reportId=" + reportId + ", itemId=" + itemId 
                + ", finderId=" + finderId + ", dateReported=" + dateReported 
                + ", reportDescription=" + reportDescription + "]";
    }
}
