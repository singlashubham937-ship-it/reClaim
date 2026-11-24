package com.reclaim.repository;

import com.reclaim.model.LostReport;
import java.util.ArrayList;

public class LostReportRepository {
    private ArrayList<LostReport> reports;

    public LostReportRepository() {
        this.reports = new ArrayList<>();
    }

    public boolean addReport(LostReport report) {
        if (report == null) {
            return false;
        }
        if (getReportById(report.getReportId()) != null) {
            return false;
        }
        reports.add(report);
        return true;
    }

    public LostReport getReportById(int reportId) {
        for (LostReport report : reports) {
            if (report.getReportId() == reportId) {
                return report;
            }
        }
        return null;
    }

    public ArrayList<LostReport> getAllReports() {
        return reports;
    }

    public ArrayList<LostReport> getReportsByFinderId(int finderId) {
        ArrayList<LostReport> result = new ArrayList<>();
        for (LostReport report : reports) {
            if (report.getFinderId() == finderId) {
                result.add(report);
            }
        }
        return result;
    }

    public boolean removeReport(int reportId) {
        LostReport report = getReportById(reportId);
        if (report != null) {
            reports.remove(report);
            return true;
        }
        return false;
    }
}
