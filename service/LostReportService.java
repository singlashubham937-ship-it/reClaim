package service;

import model.*;
import repository.*;
import util.IdGenerator;

import java.util.ArrayList;

/**
 * LostReportService handles students reporting items they have lost.
 *
 * OOP concept demonstrated: Service layer, encapsulation
 */
public class LostReportService {

    private LostReportRepository lostReportRepository;

    public LostReportService(LostReportRepository lostReportRepository) {
        this.lostReportRepository = lostReportRepository;
    }

    /**
     * A student reports that they have lost an item.
     */
    public LostReport reportLostItem(String reportedByUserId, String itemName,
                                     String description, String location) {
        String reportId = IdGenerator.nextLostReportId();
        String today    = util.DateTimeUtil.todayAsString();
        LostReport report = new LostReport(reportId, reportedByUserId,
                                           itemName, description, location, today);
        lostReportRepository.addLostReport(report);
        System.out.println("Lost report submitted! Report ID: " + reportId);
        return report;
    }

    /**
     * Returns all lost reports submitted by a specific student.
     */
    public ArrayList<LostReport> getLostReportsByUser(String userId) {
        return lostReportRepository.getReportsByUserId(userId);
    }
}
