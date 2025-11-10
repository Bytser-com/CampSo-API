package com.bytser.campso.api.activities;

import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActivityScheduleTest {

    private Activity activity;

    @BeforeEach
    void setUp() {
        User owner = TestDataFactory.createUser("ScheduleOwner");
        activity = new TestActivity();
        activity.setName("Climbing");
        activity.setOwner(owner);
        activity.setLocation(TestDataFactory.createPoint(5.0, 51.0));
        activity.setColorCode("#111111");
        activity.setTargetAudience(TargetAudience.ADULTS);
        activity.setTotalSpaces(8);
    }

    @Test
    void constructorShouldCopySlotsAndValidate() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), slots);

        assertThat(schedule.getActivity()).isEqualTo(activity);
        assertThat(schedule.getValidFrom()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(schedule.getValidTo()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(schedule.getTimeSlots()).containsExactlyElementsOf(slots);
    }

    @Test
    void constructorShouldRejectNullActivity() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertThatThrownBy(() -> new ActivitySchedule(null, LocalDate.now(), LocalDate.now(), slots))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("activity is required");
    }

    @Test
    void constructorShouldRejectInvalidDateRange() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertThatThrownBy(() -> new ActivitySchedule(activity, LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 1), slots))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("validFrom after validTo");
    }

    @Test
    void constructorShouldRejectHalfOpenRange() {
        List<TimeSlot> slots = List.of(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertThatThrownBy(() -> new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), null, slots))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must both be null or both non-null");
    }

    @Test
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
    void addTimeSlotShouldAppendSlot() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        schedule.addTimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertThat(schedule.getTimeSlots())
                .hasSize(1)
                .allMatch(slot -> slot.getStartTime().equals(LocalTime.of(8, 0)) && slot.getEndTime().equals(LocalTime.of(9, 0)));
    }

    @Test
    void addExceptionShouldStoreUniqueDates() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        LocalDate exceptionDate = LocalDate.of(2024, 5, 1);

        schedule.addException(exceptionDate);

        assertThat(schedule.getExceptions()).containsExactly(exceptionDate);
    }

    @Test
    void addExceptionShouldIgnoreNull() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());

        schedule.addException(null);

        assertThat(schedule.getExceptions()).isEmpty();
    }

    @Test
    void addExceptionShouldRejectDuplicates() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        LocalDate exceptionDate = LocalDate.of(2024, 5, 1);
        schedule.addException(exceptionDate);

        assertThatThrownBy(() -> schedule.addException(exceptionDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void removeExceptionShouldBeSafeWhenDateMissing() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        schedule.addException(LocalDate.of(2024, 5, 1));

        schedule.removeException(LocalDate.of(2024, 6, 1));

        assertThat(schedule.getExceptions()).containsExactly(LocalDate.of(2024, 5, 1));
    }

    @Test
    void removeExceptionShouldRemoveExistingDate() {
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of());
        LocalDate exceptionDate = LocalDate.of(2024, 5, 1);
        schedule.addException(exceptionDate);

        schedule.removeException(exceptionDate);

        assertThat(schedule.getExceptions()).isEmpty();
    }

    @Test
    void removeTimeSlotShouldDeleteSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 0));
        ActivitySchedule schedule = new ActivitySchedule(activity, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of(slot));

        schedule.removeTimeSlot(schedule.getTimeSlots().get(0));

        assertThat(schedule.getTimeSlots()).isEmpty();
    }

    private static class TestActivity extends Activity {
    }
}
