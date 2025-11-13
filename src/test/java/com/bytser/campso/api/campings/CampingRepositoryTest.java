package com.bytser.campso.api.campings;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bytser.campso.api.RepositoryTestSupport;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
import com.bytser.campso.api.users.UserRepository;

@DataJpaTest
class CampingRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private CampingRepository campingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByNameSupportsMultipleResults() {
        User owner = persistUser("camping-owner");
        Camping lakeview = buildCamping(owner, "Lakeside Retreat");
        Camping hilltop = buildCamping(owner, "Hilltop Haven");
        campingRepository.save(lakeview);
        campingRepository.saveAndFlush(hilltop);

        List<Camping> results = campingRepository.findByName("Lakeside Retreat");

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting(Camping::getTotalSpaces)
                .isEqualTo(lakeview.getTotalSpaces());
    }

    @Test
    void findByOwnerIdReturnsOnlyOwnedCampings() {
        User owner = persistUser("camping-owner-2");
        campingRepository.save(buildCamping(owner, "Forest Base"));
        campingRepository.save(buildCamping(owner, "Mountain Hideout"));

        User otherOwner = persistUser("camping-owner-3");
        campingRepository.saveAndFlush(buildCamping(otherOwner, "Sea Breeze"));

        List<Camping> results = campingRepository.findByOwnerId(owner.getId());

        assertThat(results)
                .hasSize(2)
                .extracting(Camping::getName)
                .containsExactlyInAnyOrder("Forest Base", "Mountain Hideout");
    }

    private User persistUser(String suffix) {
        User user = new User();
        user.setUserName("user-" + suffix);
        user.setFirstName("First" + suffix);
        user.setLastName("Last" + suffix);
        user.setEmail("user-" + suffix + "@example.com");
        user.setPasswordHash("hash-" + suffix);
        user.setCountryCode(CountryCode.NL);
        user.setLanguage(Language.EN);
        return userRepository.saveAndFlush(user);
    }

    private Camping buildCamping(User owner, String name) {
        Camping camping = new Camping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setLocation(point(4.8, 50.9));
        camping.setColorCode("#1188FF");
        camping.setTargetAudience(TargetAudience.MIXED);
        camping.setTotalSpaces(60);
        return camping;
    }
}
