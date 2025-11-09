package com.bytser.campso.api.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

import jakarta.persistence.*;

@Entity
@Table(
  name = "activity_schedules",
  indexes = @Index(name = "idx_activity_valid", columnList = "activity_id, validFrom, validTo")
)
public class ActivitySchedule {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private final UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    // Schedule is valid in [validFrom, validTo]; null = open-ended
    @Column(nullable = false, updatable = true, unique = false)
    private LocalDate validFrom;

    @Column(nullable = false, updatable = true, unique = false)
    private LocalDate validTo;

    @ElementCollection
    @CollectionTable(
        name = "activity_timeslots",
        joinColumns = @JoinColumn(name = "schedule_id")
    )
    @OrderColumn(name = "slot_order")
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "activity_schedule_exceptions",
        joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Column(name = "exception_date", nullable = true, unique=true, updatable = true)
    private List<LocalDate> exceptions;

    public ActivitySchedule(Activity activity, LocalDate validFrom, LocalDate validTo, List<TimeSlot> timeSlots) { 
        this.activity = activity; 
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.timeSlots = timeSlots;
        validate();
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (activity == null) throw new IllegalStateException("activity is required");
        if (validFrom == null && validTo != null || validFrom != null && validTo == null) {
            throw new IllegalArgumentException("validFrom and validTo must be both null or both non-null");
        }
        if (validFrom.isAfter(validTo)) {
            throw new IllegalArgumentException("validFrom after validTo");
        }
        // validate slots: non-empty times and no overlaps
        var sorted = new ArrayList<>(timeSlots);
        sorted.sort((Comparator.comparing(TimeSlot::getStartTime)));
        for (int i = 0; i < sorted.size(); i++) {
            TimeSlot s = sorted.get(i);
            if (s.getStartTime() == null || s.getEndTime() == null) {
                throw new IllegalArgumentException("timeslot has null time");
            }
            if (!s.getEndTime().isAfter(s.getStartTime())) {
                throw new IllegalArgumentException("timeslot end <= start");
            }
            if (i > 0) {
                TimeSlot prev = sorted.get(i - 1);
                if (!s.getStartTime().isAfter(prev.getEndTime())) {
                    throw new IllegalArgumentException("overlapping timeslots");
                }
            }
        }
    }

    // Getters/setters
    public UUID getId() {
        return id;
    }
    public String getIdString() {
        return id.toString();
    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }
    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }
    public void addTimeSlot(LocalTime start, LocalTime end) {
        this.timeSlots.add(new TimeSlot(start, end));
    }
    public void removeTimeSlot(TimeSlot slot) {
        this.timeSlots.remove(slot);
    }

    public List<LocalDate> getExceptions() {
        return exceptions;
    }
    public void addException(LocalDate date) {
        if (this.exceptions.contains(date)) throw new IllegalArgumentException("exception date already exists");
        this.exceptions.add(date);
    }
    public void removeException(LocalDate date) {
        this.exceptions.remove(date);
    }

}