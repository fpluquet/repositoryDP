package services;

import models.AbstractElement;
import repositories.common.CRUDRepository;
import repositories.common.SearchCriteria;
import repositories.common.filters.AbstractFilter;

import java.util.List;

public class AbstractService<T extends AbstractElement> {

    CRUDRepository<T, Long> repository;

    public AbstractService(CRUDRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T get(SearchCriteria<T> criteria) throws Exception {
        return this.repository.get(criteria);
    }

    public List<T> getAll(SearchCriteria<T> criteria) throws Exception {
        return this.repository.getAll(criteria);
    }

    public T get(AbstractFilter<T> criteria) throws Exception {
        return this.repository.get(criteria);
    }

    public List<T> getAll(AbstractFilter<T> criteria) throws Exception {
        return this.repository.getAll(criteria);
    }

    public boolean exists(SearchCriteria<T> criteria) throws Exception {
        return this.repository.exists(criteria);
    }

    public boolean exists(AbstractFilter<T> criteria) throws Exception {
        return this.repository.exists(criteria);
    }

    public List<T> findAll() throws Exception {
        return this.repository.findAll();
    }

    public void update(T element) throws Exception {
        element.checkValidity();
        this.repository.update(element);
    }

    public void delete(T element) throws Exception {
        this.repository.delete(element);
    }


}
