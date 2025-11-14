package com.bytser.campso.api.support;

import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.plans.Plan;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
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
        TestUser user = new TestUser();
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
        return new TestActivity();
    }

    public static Camping newCamping() {
        return new TestCamping();
    }

    public static Facility newFacility() {
        return new TestFacility();
    }

    public static Plan newPlan() {
        return new TestPlan();
    }

    public static Review newReview() {
        return new TestReview();
    }

    private static class TestUser extends User {
        // Uses the protected no-arg constructor from User
    }

    private static class TestActivity extends Activity {
        // Exposes the protected no-arg constructor for tests
    }

    private static class TestCamping extends Camping {
        // Exposes the protected no-arg constructor for tests
    }

    private static class TestFacility extends Facility {
        // Exposes the protected no-arg constructor for tests
    }

    private static class TestPlan extends Plan {
        // Exposes the protected no-arg constructor for tests
    }

    private static class TestReview extends Review {
        // Exposes the protected no-arg constructor for tests
    }
}
