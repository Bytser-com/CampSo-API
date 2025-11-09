package com.bytser.campso.api.activities;

import java.time.LocalTime;
import jakarta.persistence.*;

@Embeddable
public class TimeSlot {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    protected TimeSlot() {}
    public TimeSlot(LocalTime start, LocalTime end) {
        if (!end.isAfter(start)) throw new IllegalArgumentException("end <= start");
        this.startTime = start;
        this.endTime = end;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
}
