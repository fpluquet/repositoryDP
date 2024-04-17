package repositories.common;

public interface SearchCriteria<T> {

    boolean match(T data);
}
