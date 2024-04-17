package repositories;

public interface SearchCriteria<T> {

    boolean match(T data);
}
