package repositories.filters;

import repositories.filters.visitors.FiltersVisitor;

public class AbstractFilter<T> {

    public void accept(FiltersVisitor<T> visitor) {
        visitor.visit(this);
    }

    public AbstractFilter<T> and(AbstractFilter<T> filter) {
        return new CompositeAndFilter<T>(this, filter);
    }

    public AbstractFilter<T> or(AbstractFilter<T> filter) {
        return new CompositeOrFilter<T>(this, filter);
    }
}
