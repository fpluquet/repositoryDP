package repositories;

import java.util.List;

public interface CRUDRepository<T, ID> {

    // CRUD operations

    List<T> findAll() throws Exception;

    T getById(ID id) throws Exception;

    void save(T t) throws Exception;

    void update(T t) throws Exception;

    void delete(T t) throws Exception;


    default T get(SearchCriteria<T> criteria) throws Exception {
        return this.getAll(criteria).stream().findFirst().orElseThrow();
    }

    default List<T> getAll(SearchCriteria<T> criteria) throws Exception {
        return this.findAll().stream().filter(criteria::match).toList();
    }

    default boolean exists(SearchCriteria<T> criteria) throws Exception {
        return !this.getAll(criteria).isEmpty();
    }
}
