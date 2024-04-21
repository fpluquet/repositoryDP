package services;

import models.Profile;
import org.junit.jupiter.api.*;
import repositories.common.AbstractRepository;
import repositories.common.RepositoryFactory;
import repositories.common.filters.FilterContains;
import repositories.common.filters.FilterEquals;
import repositories.db.DBRepositoryFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class ProfileServiceTest {


    Profile profile1;
    Profile profile2;
    static Connection connection;

    ProfileService profileService;

    @BeforeEach
    void setUp() throws Exception {
        profileService = getProfileService();
        profile1 = profileService.createProfile("fred", "Fred Flintstone");
        profile2 = profileService.createProfile("barney", "Barney Rubble");
        System.out.println("ProfileServiceTest.setUp");
        System.out.println("profile1: " + profile1);
        System.out.println("profile2: " + profile2);
    }

    protected abstract ProfileService getProfileService() throws SQLException, IOException;

    @AfterEach
    void tearDown()  {
        try {
            profileService.delete(profile1);
            profileService.delete(profile2);
        } catch (Exception e) {}
    }

    @Test
    void get() throws Exception {
        Profile profile = profileService.get(new FilterEquals<>("login", "fred"));
        assertEquals("Fred Flintstone", profile.getFullname());
        profile = profileService.get(new FilterEquals<>("login", "barney"));
        assertEquals("Barney Rubble", profile.getFullname());
    }

    @Test
    void getAll() throws Exception {
        List<Profile> profiles = profileService.getAll();
        assertEquals(2, profiles.size());

        Profile profile = profiles.get(0);
        assertEquals("Fred Flintstone", profile.getFullname());
        profile = profiles.get(1);
        assertEquals("Barney Rubble", profile.getFullname());
    }

    @Test
    void getAll_withFilters() throws Exception {
        List<Profile> profiles = profileService.getAll(new FilterContains<>("fullname", "Fred"));
        assertEquals(1, profiles.size());
        assertEquals("Fred Flintstone", profiles.get(0).getFullname());
    }

    @Test
    void getAll_noResult() throws Exception {
        List<Profile> profiles = profileService.getAll(new FilterEquals<>("login", "nonexistent"));
        assertEquals(0, profiles.size());
    }

    @Test
    void exists() throws Exception {
        assertTrue(profileService.exists(new FilterEquals<>("login", "fred")));
        assertFalse(profileService.exists(new FilterEquals<>("login", "nonexistent")));
    }

    @Test
    void update() throws Exception {
        Profile profile = profileService.get(new FilterEquals<>("login", "fred"));
        profile.setFullname("Fred Flintstone Jr.");
        profileService.update(profile);
        profile = profileService.get(new FilterEquals<>("login", "fred"));
        assertEquals("Fred Flintstone Jr.", profile.getFullname());
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> profileService.delete(new FilterEquals<>("login", "fred")));
        assertDoesNotThrow(() -> profileService.delete(new FilterEquals<>("login", "barney")));

        assertThrows(Exception.class, () -> profileService.get(new FilterEquals<>("login", "fred")));
    }

    @Test
    void createProfile() {

    }
}
