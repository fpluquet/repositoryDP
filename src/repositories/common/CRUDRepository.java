package repositories.common;

import repositories.common.filters.AbstractFilter;

import java.util.List;

public interface CRUDRepository<T, ID> {

    // CRUD operations

    List<T> getAll() throws Exception;

    T getById(ID id) throws Exception;

    void save(T t) throws Exception;

    void update(T t) throws Exception;

    void delete(T t) throws Exception;



    default T get(AbstractFilter<T> filter) throws Exception {
        return this.getAll(filter).stream().findFirst().orElseThrow();
    }

    List<T> getAll(AbstractFilter<T> filter) throws Exception;


    default boolean exists(AbstractFilter<T> criteria) throws Exception {
        try {
            this.get(criteria);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
