package com.bytser.campso.api.support;

import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.plans.Plan;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public final class TestDataFactory {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    private TestDataFactory() {
    }

    public static Geometry createPoint(double longitude, double latitude) {
        return GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }

    public static User createUser(String identifier) {
        User user = instantiate(User.class);
        user.setUserName("user" + identifier);
        user.setFirstName("First" + identifier);
        user.setLastName("Last" + identifier);
        user.setEmail("user" + identifier + "@example.com");
        user.setPasswordHash("hash" + identifier);
        user.setLanguage(Language.EN);
        user.setCountryCode(CountryCode.UK);
        return user;
    }

    public static Activity newActivity() {
        return instantiate(Activity.class);
    }

    public static Camping newCamping() {
        return instantiate(Camping.class);
    }

    public static Facility newFacility() {
        return instantiate(Facility.class);
    }

    public static Plan newPlan() {
        return instantiate(Plan.class);
    }

    public static Review newReview() {
        return instantiate(Review.class);
    }

    private static <T> T instantiate(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException exception) {
            throw new IllegalStateException("Failed to instantiate " + type.getName(), exception);
        }
    }
}
