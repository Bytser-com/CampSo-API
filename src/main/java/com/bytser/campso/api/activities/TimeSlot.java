package com.bytser.campso.api.activities;

import java.time.LocalTime;
import jakarta.persistence.*;

@Embeddable
public class TimeSlot {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    protected TimeSlot() {
        // JPA requirement
    }
    public TimeSlot(LocalTime start, LocalTime end) {
        if (!end.isAfter(start)) throw new IllegalArgumentException("end <= start");
        this.startTime = start;
        this.endTime = end;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    // getters/setters
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
}
