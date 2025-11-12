package com.bytser.campso.api.activities;

import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActivityScheduleTest {

    private Activity activity;

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
    void setUp() {
        User owner = TestDataFactory.createUser("ScheduleOwner");
        activity = new TestActivity();
        activity.setName("Climbing");
        activity.setOwner(owner);
        activity.setLocation(TestDataFactory.createPoint(5.0, 51.0));
        activity.setColorCode("#111111");
        activity.setTargetAudience(TargetAudience.STUDENTS);
        activity.setTotalSpaces(8);
    }

    @Test
    @DisplayName("constructor copies slots and validates schedule fields")
    void constructorShouldCopySlotsAndValidate() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), slots);

        assertThat(schedule.getActivity()).isEqualTo(activity);
        assertThat(schedule.getValidFrom()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(schedule.getValidTo()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(schedule.getTimeSlots()).containsExactlyElementsOf(slots);
    }

    @Test
    @DisplayName("constructor rejects schedules without an activity")
    void constructorShouldRejectNullActivity() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertThatThrownBy(() -> new ActivitySchedule(null, LocalDate.now(), LocalDate.now(), slots))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("activity is required");
    }

    @Test
    @DisplayName("constructor rejects schedules where validFrom is after validTo")
    void constructorShouldRejectInvalidDateRange() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertThatThrownBy(() -> new ActivitySchedule(activity, LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 1), slots))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("validFrom after validTo");
    }

    @Test
    @DisplayName("constructor rejects half open date ranges")
    void constructorShouldRejectHalfOpenRange() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertThatThrownBy(() -> new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), null, slots))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must both be null or both non-null");
    }

    @Test
    @DisplayName("constructor rejects overlapping time slots")
    void constructorShouldRejectOverlappingSlots() {
        List<TimeSlot> slots = List.of(
                new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)),
                new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 30))
        );
        assertThatThrownBy(() -> new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), slots))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("overlapping timeslots");
    }

    @Test
    @DisplayName("addTimeSlot appends a new slot with provided start and end times")
    void addTimeSlotShouldAppendSlot() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        schedule.addTimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertThat(schedule.getTimeSlots())
                .hasSize(1)
                .allMatch(slot -> slot.getStartTime().equals(LocalTime.of(8, 0)) && slot.getEndTime().equals(LocalTime.of(9, 0)));
    }

    @Test
    @DisplayName("addException stores unique exception dates")
    void addExceptionShouldStoreUniqueDates() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        LocalDate exceptionDate = LocalDate.of(2024, 5, 1);

        schedule.addException(exceptionDate);

        assertThat(schedule.getExceptions()).containsExactly(exceptionDate);
    }

    @Test
    @DisplayName("addException ignores null values")
    void addExceptionShouldIgnoreNull() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());

        schedule.addException(null);

        assertThat(schedule.getExceptions()).isEmpty();
    }

    @Test
    @DisplayName("addException rejects duplicate dates")
    void addExceptionShouldRejectDuplicates() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        LocalDate exceptionDate = LocalDate.of(2024, 5, 1);
        schedule.addException(exceptionDate);

        assertThatThrownBy(() -> schedule.addException(exceptionDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("removeException safely ignores dates that were not stored")
    void removeExceptionShouldBeSafeWhenDateMissing() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        schedule.addException(LocalDate.of(2024, 5, 1));

        schedule.removeException(LocalDate.of(2024, 6, 1));

        assertThat(schedule.getExceptions()).containsExactly(LocalDate.of(2024, 5, 1));
    }

    @Test
    @DisplayName("removeException removes stored exception dates")
    void removeExceptionShouldRemoveExistingDate() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        LocalDate exceptionDate = LocalDate.of(2024, 5, 1);
        schedule.addException(exceptionDate);

        schedule.removeException(exceptionDate);

        assertThat(schedule.getExceptions()).isEmpty();
    }

    @Test
    @DisplayName("removeTimeSlot removes the provided time slot instance")
    void removeTimeSlotShouldDeleteSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 0));
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of(slot));

        schedule.removeTimeSlot(schedule.getTimeSlots().get(0));

        assertThat(schedule.getTimeSlots()).isEmpty();
    }

    private static class TestActivity extends Activity {
    }
}
