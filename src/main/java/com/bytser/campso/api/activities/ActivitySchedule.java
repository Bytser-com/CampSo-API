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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    // Schedule is valid in [validFrom, validTo]; null = open-ended
    @Column(nullable = true, updatable = true, unique = false)
    private LocalDate validFrom;

    @Column(nullable = true, updatable = true, unique = false)
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
        joinColumns = @JoinColumn(name = "schedule_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"schedule_id", "exception_date"})
    )
    @Column(name = "exception_date", nullable = true)
    private List<LocalDate> exceptions = new ArrayList<>();
    protected ActivitySchedule() {
        // JPA requirement
    }

    public ActivitySchedule(Activity activity, LocalDate validFrom, LocalDate validTo, List<TimeSlot> timeSlots) {
        this.activity = activity;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.timeSlots = timeSlots != null ? new ArrayList<>(timeSlots) : new ArrayList<>();
        validate();
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (activity == null) throw new IllegalStateException("activity is required");
        if (timeSlots == null) {
            throw new IllegalStateException("timeSlots cannot be null");
        }
        if (validFrom != null && validTo != null) {
            if (validFrom.isAfter(validTo)) {
                throw new IllegalArgumentException("validFrom after validTo");
            }
        } else if (validFrom == null ^ validTo == null) {
            throw new IllegalArgumentException("validFrom and validTo must both be null or both non-null");
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
        return id != null ? id.toString() : null;
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
        if (this.timeSlots == null) {
            this.timeSlots = new ArrayList<>();
        }
        this.timeSlots.add(new TimeSlot(start, end));
    }
    public void removeTimeSlot(TimeSlot slot) {
        this.timeSlots.remove(slot);
    }

    public List<LocalDate> getExceptions() {
        return exceptions;
    }
    public void addException(LocalDate date) {
        if (date == null) {
            return;
        }
        if (this.exceptions == null) {
            this.exceptions = new ArrayList<>();
        }
        if (this.exceptions.contains(date)) throw new IllegalArgumentException("exception date already exists");
        this.exceptions.add(date);
    }
    public void removeException(LocalDate date) {
        if (this.exceptions != null) {
            this.exceptions.remove(date);
        }
    }

}
