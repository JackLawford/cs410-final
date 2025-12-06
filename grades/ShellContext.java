package edu.boisestate.grades;

import java.sql.Connection;

public class ShellContext {

    private final Connection conn;

    private Integer activeClassId;
    private String activeCourseNum;
    private String activeTerm;
    private Integer activeSection;
    private String activeDescription;

    public ShellContext(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return conn;
    }

    public boolean hasActiveClass() {
        return activeClassId != null;
    }

    public Integer getActiveClassId() {
        return activeClassId;
    }

    public void setActiveClassId(Integer activeClassId) {
        this.activeClassId = activeClassId;
    }

    public void setActiveClass(int id, String courseNum, String term, Integer section, String description) {
        this.activeClassId = id;
        this.activeCourseNum = courseNum;
        this.activeTerm = term;
        this.activeSection = section;
        this.activeDescription = description;
    }

    public void clearActiveClass() {
        this.activeClassId = null;
        this.activeCourseNum = null;
        this.activeTerm = null;
        this.activeSection = null;
        this.activeDescription = null;
    }

    public String getActiveCourseNum() {
        return activeCourseNum;
    }

    public String getActiveTerm() {
        return activeTerm;
    }

    public Integer getActiveSection() {
        return activeSection;
    }

    public String getActiveDescription() {
        return activeDescription;
    }

    public int requireActiveClass() {
        if (activeClassId == null) {
            throw new IllegalStateException("No active class selected. Use select-class first.");
        }
        return activeClassId;
    }
}

