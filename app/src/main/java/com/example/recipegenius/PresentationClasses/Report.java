package com.example.recipegenius.PresentationClasses;

public class Report {
    private String reportId;            // Unique ID for the report
    private String reportedUserId;      // ID of the user who is being reported
    private String reportedUserName;    // Name of the user who is being reported
    private String reportedUserImage;   // Profile image URL of the user who is being reported
    private String reporterUserId;      // ID of the user who is reporting
    private String reportReason;        // Reason for reporting the user (optional)
    private long reportTimestamp;       // Timestamp of when the report was made

    // Default constructor required for calls to DataSnapshot.getValue(Report.class)
    public Report() {
    }

    // Constructor to initialize the report
    public Report(String reportId, String reportedUserId, String reportedUserName, String reportedUserImage, String reporterUserId, String reportReason, long reportTimestamp) {
        this.reportId = reportId;
        this.reportedUserId = reportedUserId;
        this.reportedUserName = reportedUserName;
        this.reportedUserImage = reportedUserImage;
        this.reporterUserId = reporterUserId;
        this.reportReason = reportReason;
        this.reportTimestamp = reportTimestamp;
    }

    // Getters and Setters
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(String reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public String getReportedUserName() {
        return reportedUserName;
    }

    public void setReportedUserName(String reportedUserName) {
        this.reportedUserName = reportedUserName;
    }

    public String getReportedUserImage() {
        return reportedUserImage;
    }

    public void setReportedUserImage(String reportedUserImage) {
        this.reportedUserImage = reportedUserImage;
    }

    public String getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(String reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public long getReportTimestamp() {
        return reportTimestamp;
    }

    public void setReportTimestamp(long reportTimestamp) {
        this.reportTimestamp = reportTimestamp;
    }
}
