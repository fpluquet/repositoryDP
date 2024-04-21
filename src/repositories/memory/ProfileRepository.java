package repositories.memory;

import models.Profile;
import repositories.common.filters.AbstractFilter;
import repositories.common.filters.visitors.FilterEvaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ProfileRepository extends repositories.common.ProfileRepository {

    List<Profile> profiles = new ArrayList<>();
    int nextId = 1;

    @Override
    public List<Profile> getAll() {
        return profiles;
    }

    @Override
    public Profile getById(Long aLong) throws NoSuchElementException {
        return profiles.stream().filter(profile -> profile.getId() == aLong).findFirst().orElseThrow();
    }

    @Override
    public void save(Profile profile) throws IOException {
        if (this.exists(profile.getId())) {
            this.update(profile);
        } else {
            profiles.add(profile);
            profile.setId(nextId++);
        }
    }

    private boolean exists(long id) {
        return profiles.stream().anyMatch(p -> p.getId() == id);
    }

    @Override
    public void update(Profile profile) throws IOException {
        final Optional<Profile> existingProfile = profiles.stream().filter(p -> p.getId() == profile.getId()).findFirst();
        if (existingProfile.isEmpty()) {
            throw new IOException("Profile not found");
        }
        existingProfile.get().setFullname(profile.getFullname());
        existingProfile.get().setLogin(profile.getLogin());
    }

    @Override
    public void delete(Profile profile) throws IOException {
        if (profiles.stream().noneMatch(p -> p.getId() == profile.getId())) {
            throw new IOException("Profile not found");
        }
        profiles = profiles.stream().filter(p -> p.getId() != profile.getId()).toList();
    }


    @Override
    public List<Profile> getAll(AbstractFilter<Profile> filter) throws Exception {
        return profiles.stream().filter(p -> FilterEvaluator.evaluate(p, filter)).toList();
    }


}
