package com.reclaim.service;

import com.reclaim.model.LostReport;
import com.reclaim.model.User;
import com.reclaim.enums.UserRole;
import com.reclaim.repository.ItemRepository;
import com.reclaim.repository.LostReportRepository;
import com.reclaim.repository.UserRepository;
import java.util.ArrayList;

public class LostReportService {

    private LostReportRepository lostReportRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    public LostReportService(LostReportRepository lostReportRepository,
                             ItemRepository itemRepository,
                             UserRepository userRepository) {
        this.lostReportRepository = lostReportRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    // Create a new LostReport
    public boolean createReport(int reportId, int itemId, int finderId, String reportDescription) {
        // Check if item exists
        if (itemRepository.getItemById(itemId) == null) {
            System.out.println("Cannot create report: No item found with ID " + itemId);
            return false;
        }

        // Check if finder exists and has FINDER role
        User finder = userRepository.getUserById(finderId);
        if (finder == null) {
            System.out.println("Cannot create report: No user found with ID " + finderId);
            return false;
        }
        if (finder.getRole() != UserRole.FINDER) {
            System.out.println("Cannot create report: User ID " + finderId + " is not a Finder.");
            return false;
        }

        // Check description
        if (reportDescription == null || reportDescription.trim().isEmpty()) {
            System.out.println("Cannot create report: Description cannot be empty.");
            return false;
        }

        LostReport report = new LostReport(reportId, itemId, finderId, reportDescription);
        boolean added = lostReportRepository.addReport(report);

        if (added) {
            System.out.println("Report created successfully. Report ID: " + reportId);
        } else {
            System.out.println("Failed to create report. Report ID " + reportId + " already exists.");
        }
        return added;
    }

    // Find a report by ID
    public LostReport getReportById(int reportId) {
        LostReport report = lostReportRepository.getReportById(reportId);
        if (report == null) {
            System.out.println("No report found with ID: " + reportId);
        }
        return report;
    }

    // Get all reports
    public ArrayList<LostReport> getAllReports() {
        return lostReportRepository.getAllReports();
    }

    // Get all reports submitted by a specific Finder
    public ArrayList<LostReport> getReportsByFinder(int finderId) {
        ArrayList<LostReport> result = lostReportRepository.getReportsByFinderId(finderId);
        if (result.isEmpty()) {
            System.out.println("No reports found for Finder ID: " + finderId);
        }
        return result;
    }
}
