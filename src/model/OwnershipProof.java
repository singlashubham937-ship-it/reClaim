package model;

import java.time.LocalDateTime;

/**
 * Represents the ownership proof submitted by a student to verify an item claim.
 * Applies encapsulation using private fields and public getters/setters.
 */
public class OwnershipProof {
    private int proofId;
    private int itemId;
    private int studentId;
    private String itemColour;
    private String brand;
    private String identifyingDescription;
    private String additionalOwnershipDetails;
    private LocalDateTime submissionDate;

    // Default constructor
    public OwnershipProof() {
        this.submissionDate = LocalDateTime.now();
    }

    // Constructor with core proof details
    public OwnershipProof(String itemColour, String brand, String identifyingDescription, String additionalOwnershipDetails) {
        this.itemColour = itemColour;
        this.brand = brand;
        this.identifyingDescription = identifyingDescription;
        this.additionalOwnershipDetails = additionalOwnershipDetails;
        this.submissionDate = LocalDateTime.now();
    }

    // Full constructor
    public OwnershipProof(int proofId, int itemId, int studentId, String itemColour, String brand,
                          String identifyingDescription, String additionalOwnershipDetails) {
        this.proofId = proofId;
        this.itemId = itemId;
        this.studentId = studentId;
        this.itemColour = itemColour;
        this.brand = brand;
        this.identifyingDescription = identifyingDescription;
        this.additionalOwnershipDetails = additionalOwnershipDetails;
        this.submissionDate = LocalDateTime.now();
    }

    // Full constructor including custom submissionDate
    public OwnershipProof(int proofId, int itemId, int studentId, String itemColour, String brand,
                          String identifyingDescription, String additionalOwnershipDetails, LocalDateTime submissionDate) {
        this.proofId = proofId;
        this.itemId = itemId;
        this.studentId = studentId;
        this.itemColour = itemColour;
        this.brand = brand;
        this.identifyingDescription = identifyingDescription;
        this.additionalOwnershipDetails = additionalOwnershipDetails;
        this.submissionDate = submissionDate != null ? submissionDate : LocalDateTime.now();
    }

    // Getters and Setters
    public int getProofId() {
        return proofId;
    }

    public void setProofId(int proofId) {
        this.proofId = proofId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // Alias for ownerId compatibility
    public int getOwnerId() {
        return studentId;
    }

    public void setOwnerId(int ownerId) {
        this.studentId = ownerId;
    }

    public String getItemColour() {
        return itemColour;
    }

    public void setItemColour(String itemColour) {
        this.itemColour = itemColour;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Alias for itemBrand compatibility
    public String getItemBrand() {
        return brand;
    }

    public void setItemBrand(String itemBrand) {
        this.brand = itemBrand;
    }

    public String getIdentifyingDescription() {
        return identifyingDescription;
    }

    public void setIdentifyingDescription(String identifyingDescription) {
        this.identifyingDescription = identifyingDescription;
    }

    // Alias for specialIdentificationDetails
    public String getSpecialIdentificationDetails() {
        return identifyingDescription;
    }

    public void setSpecialIdentificationDetails(String specialDetails) {
        this.identifyingDescription = specialDetails;
    }

    public String getAdditionalOwnershipDetails() {
        return additionalOwnershipDetails;
    }

    public void setAdditionalOwnershipDetails(String additionalOwnershipDetails) {
        this.additionalOwnershipDetails = additionalOwnershipDetails;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Validates that sufficient identifying information is provided (such as colour and description).
     * @return true if valid, false otherwise.
     */
    public boolean validateProof() {
        return itemColour != null && !itemColour.trim().isEmpty()
                && identifyingDescription != null && !identifyingDescription.trim().isEmpty();
    }

    /**
     * Displays ownership proof details.
     */
    public void displayProof() {
        System.out.println("----- Ownership Proof Details -----");
        if (proofId > 0) System.out.println("Proof ID                 : " + proofId);
        if (itemId > 0) System.out.println("Item ID                  : " + itemId);
        if (studentId > 0) System.out.println("Student ID               : " + studentId);
        System.out.println("Item Colour              : " + (itemColour != null ? itemColour : "N/A"));
        System.out.println("Brand                    : " + (brand != null ? brand : "N/A"));
        System.out.println("Identifying Description  : " + (identifyingDescription != null ? identifyingDescription : "N/A"));
        System.out.println("Additional Details       : " + (additionalOwnershipDetails != null ? additionalOwnershipDetails : "None"));
        System.out.println("Submission Date          : " + submissionDate);
    }

    public void displayProofDetails() {
        displayProof();
    }

    @Override
    public String toString() {
        return "OwnershipProof [colour=" + itemColour + ", brand=" + brand 
                + ", identifyingDescription=" + identifyingDescription 
                + ", additionalDetails=" + additionalOwnershipDetails + "]";
    }
}
