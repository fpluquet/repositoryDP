package repositories.common.filters;

import repositories.common.filters.visitors.FiltersVisitor;

public class CompositeOrFilter<T> extends CompositeFilter<T> {

    @SafeVarargs
    public CompositeOrFilter(AbstractFilter<T>... filters) {
        super(filters);
    }

    public void accept(FiltersVisitor<T> visitor) {
        visitor.visit(this);
    }
}
