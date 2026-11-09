package training.restapi.demo1.models;

import java.time.LocalDate;

public class ProjectFilter {
    private Boolean isActive;
    private LocalDate deadlineBefore;
    private LocalDate deadlineAfter;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getDeadlineBefore() {
        return deadlineBefore;
    }

    public void setDeadlineBefore(LocalDate deadlineBefore) {
        this.deadlineBefore = deadlineBefore;
    }

    public LocalDate getDeadlineAfter() {
        return deadlineAfter;
    }

    public void setDeadlineAfter(LocalDate deadlineAfter) {
        this.deadlineAfter = deadlineAfter;
    }
}
