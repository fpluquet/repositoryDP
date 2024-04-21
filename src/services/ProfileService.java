package services;

import models.Profile;
import repositories.common.AbstractRepository;
import repositories.common.filters.FilterEquals;

public class ProfileService extends AbstractService<Profile> {

    public ProfileService(AbstractRepository<Profile, Long> profileRepository) {
        super(profileRepository);
    }

    public Profile createProfile(String login, String fullName) throws Exception {
        Profile profile = new Profile(login, fullName);
        profile.checkValidity();
        checkUniqueLogin(profile);
        this.repository.save(profile);
        return profile;
    }

    private void checkUniqueLogin(Profile profile) throws Exception {
        if (this.repository.exists(new FilterEquals<>("login", profile.getLogin()))) {
            throw new Exception("Login already exists");
        }
    }

}
