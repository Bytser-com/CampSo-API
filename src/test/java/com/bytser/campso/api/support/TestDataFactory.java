package com.bytser.campso.api.support;

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

    private static class TestUser extends User {
        // Uses the protected no-arg constructor from User
    }
}
