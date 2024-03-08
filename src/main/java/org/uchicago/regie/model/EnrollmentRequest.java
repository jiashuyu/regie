package org.uchicago.regie.model;

public class EnrollmentRequest {
    private long studentId;
    private long courseId;
    private String newStatus;
    private String quarter;

    public EnrollmentRequest(long studentId, long courseId, String newStatus, String quarter) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.newStatus = newStatus;
        this.quarter = quarter;
    }

    public long getStudentId() { return studentId; }

    public void setStudentId(long studentId) { this.studentId = studentId; }

    public long getCourseId() { return courseId; }

    public void setCourseId(long courseId) { this.courseId = courseId; }

    public String getNewStatus() { return newStatus; }

    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String getQuarter() { return quarter; }

    public void setQuarter(String quarter) {this.quarter = quarter;}

}
