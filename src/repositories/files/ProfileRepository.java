package repositories.files;

import models.Profile;
import repositories.common.filters.AbstractFilter;
import repositories.common.filters.visitors.FilterEvaluator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProfileRepository extends repositories.common.ProfileRepository {

    private final FilesRepositoryFactory repositoryFactory;
    private final FileLinesStream fileLinesStream;
    private int nextId = 1;

    public ProfileRepository(FilesRepositoryFactory repositoryFactory, String folder) throws IOException {
        super();
        this.repositoryFactory = repositoryFactory;
        this.fileLinesStream = new FileLinesStream(Path.of(folder, "profiles.txt"));
    }

    public static Profile profileFromStream(FileLinesStream stream) {
        final long id = Long.parseLong(stream.nextLine().split(": ")[1]);
        final String login = stream.nextLine().split(": ")[1];
        final String fullName = stream.nextLine().split(": ")[1];
        return new Profile(id, fullName, login);
    }

    public List<String> profileToLines(Profile profile) {
        List<String> lines = new ArrayList<String>();
        lines.add("ID: " + profile.getId());
        lines.add("Fullname: " + profile.getFullname());
        lines.add("Login: " + profile.getLogin());
        return lines;
    }

    @Override
    public List<Profile> findAll() throws IOException {
        fileLinesStream.reset();
        List<Profile> profiles = new ArrayList<>();
        while (fileLinesStream.hasNext()) {
            Profile profile = profileFromStream(fileLinesStream);
            profiles.add(profile);
            if (profile.getId() >= nextId)
                nextId = Math.toIntExact(profile.getId() + 1);
        }
        return profiles;
    }

    @Override
    public Profile getById(Long aLong) throws Exception {
        return this.findAll().stream().filter(profile -> profile.getId() == aLong).findFirst().orElseThrow();
    }

    @Override
    public void save(Profile profile) throws Exception {
        List<Profile> profiles = this.findAll();
        profiles.add(profile);
        profile.setId(nextId++);
        writeAll(profiles);
    }

    @Override
    public void update(Profile profile) throws Exception {
        List<Profile> profiles = this.findAll();
        Profile existingProfile = profiles.stream().filter(p -> p.getId() == profile.getId()).findFirst().orElseThrow();
        existingProfile.setFullname(profile.getFullname());
        existingProfile.setLogin(profile.getLogin());
        writeAll(profiles);
    }

    @Override
    public void delete(Profile profile) throws Exception {
        List<Profile> profiles = this.findAll();
        profiles = profiles.stream().filter(p -> p.getId() != profile.getId()).toList();
        writeAll(profiles);
    }

    @Override
    public Profile get(AbstractFilter<Profile> filter) throws Exception {
        return this.get(p -> FilterEvaluator.match(p, filter));
    }

    @Override
    public List<Profile> getAll(AbstractFilter<Profile> filter) throws Exception {
        return this.getAll(p -> FilterEvaluator.match(p, filter));
    }


    private void writeAll(List<Profile> profiles) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Profile profile : profiles) {
            lines.addAll(profileToLines(profile));
        }
        fileLinesStream.writeLines(lines);
    }

}
