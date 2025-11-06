package com.bytser.campso.api.campings;

public enum Rating {
    HALF_STAR(0.5),
    ONE_STAR(1),
    ONE_HALF_STAR(1.5),
    TWO_STAR(2),
    TWO_HALF_STAR(2.5),
    THREE_STAR(3),
    THREE_HALF_STAR(3.5),
    FOUR_STAR(4),
    FOUR_HALF_STAR(4.5),
    FIVE_STAR(5);

    private final double value;

    Rating(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static Rating fromValue(double value) {
        for (Rating rating : Rating.values()) {
            if (rating.value == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}
