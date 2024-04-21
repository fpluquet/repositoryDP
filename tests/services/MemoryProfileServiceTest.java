package services;

import repositories.memory.MemoryRepositoryFactory;

public class MemoryProfileServiceTest extends ProfileServiceTest {

        @Override
        protected ProfileService getProfileService() {
            MemoryRepositoryFactory factory = new MemoryRepositoryFactory();
            return new ProfileService(factory.getProfileRepository());
        }

}
