package services;

import models.Profile;
import repositories.CRUDRepository;
import repositories.SearchCriteria;

import java.util.List;

public class ProfileService extends AbstractService<Profile> {

    public ProfileService(CRUDRepository<Profile, Long> profileRepository) {
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
        if (this.repository.exists(p -> p.getLogin().equals(profile.getLogin()))) {
            throw new Exception("Login already exists");
        }
    }

}
