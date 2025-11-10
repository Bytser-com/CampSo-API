package com.bytser.campso.api.activities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeSlotTest {

    @Test
    @DisplayName("constructor stores start and end times and renders them in toString")
    void constructorShouldStoreStartAndEndTimes() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);

        TimeSlot slot = new TimeSlot(start, end);

        assertThat(slot.getStartTime()).isEqualTo(start);
        assertThat(slot.getEndTime()).isEqualTo(end);
        assertThat(slot.toString()).contains("09:00").contains("10:00");
    }

    @Test
    @DisplayName("constructor rejects end times that are not after the start time")
    void constructorShouldRejectEndBeforeStart() {
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(9, 0);

        assertThatThrownBy(() -> new TimeSlot(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("end <= start");
    }
}
