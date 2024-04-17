package repositories.filters;

import repositories.filters.visitors.FiltersVisitor;

public class CompositeAndFilter<T> extends CompositeFilter<T> {

    @SafeVarargs
    public CompositeAndFilter(AbstractFilter<T>... filters) {
        super(filters);
    }

    public void accept(FiltersVisitor<T> visitor) {
        visitor.visit(this);
    }
}
