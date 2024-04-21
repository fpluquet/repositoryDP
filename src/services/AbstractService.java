package services;

import models.AbstractElement;
import repositories.common.AbstractRepository;
import repositories.common.filters.AbstractFilter;

import java.util.List;

public class AbstractService<T extends AbstractElement> {

    AbstractRepository<T, Long> repository;

    public AbstractService(AbstractRepository<T, Long> repository) {
        this.repository = repository;
    }


    public T get(AbstractFilter<T> criteria) throws Exception {
        return this.repository.get(criteria);
    }

    public List<T> getAll(AbstractFilter<T> criteria) throws Exception {
        return this.repository.getAll(criteria);
    }

    public boolean exists(AbstractFilter<T> criteria) throws Exception {
        return this.repository.exists(criteria);
    }

    public List<T> getAll() throws Exception {
        return this.repository.getAll();
    }

    public void update(T element) throws Exception {
        element.checkValidity();
        this.repository.update(element);
    }

    public void delete(T element) throws Exception {
        this.repository.delete(element);
    }

    public void delete(AbstractFilter<T> criteria) throws Exception {
        this.repository.delete(this.get(criteria));
    }

    public void deleteAll(AbstractFilter<T> criteria) throws Exception {
        for (T element : this.repository.getAll(criteria)) {
            this.delete(element);
        }
    }


}
