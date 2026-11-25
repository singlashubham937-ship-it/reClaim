package repository;

import model.LostReport;
import java.util.ArrayList;

/**
 * Stores LostReport records submitted by students.
 *
 * OOP concept demonstrated: Encapsulation (data hidden inside the class)
 */
public class LostReportRepository {

    private ArrayList<LostReport> reports;

    public LostReportRepository() {
        this.reports = new ArrayList<>();
    }

    /**
     * Adds a new lost report. Returns false if the report ID already exists.
     */
    public boolean addLostReport(LostReport report) {
        if (report == null || report.getReportId() == null) {
            return false;
        }
        if (getLostReportById(report.getReportId()) != null) {
            System.out.println("Report ID already exists: " + report.getReportId());
            return false;
        }
        reports.add(report);
        return true;
    }

    /**
     * Finds a lost report by its ID.
     */
    public LostReport getLostReportById(String reportId) {
        if (reportId == null) return null;
        for (LostReport r : reports) {
            if (reportId.equals(r.getReportId())) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns all lost reports submitted by a specific user.
     */
    public ArrayList<LostReport> getReportsByUserId(String userId) {
        ArrayList<LostReport> result = new ArrayList<>();
        if (userId == null) return result;
        for (LostReport r : reports) {
            if (userId.equals(r.getReportedByUserId())) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Returns all lost reports.
     */
    public ArrayList<LostReport> getAllLostReports() {
        return new ArrayList<>(reports);
    }
}
