package repositories.filters;
import repositories.filters.visitors.FiltersVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeFilter<T> extends AbstractFilter<T> {

    private List<AbstractFilter<T>> filters = new ArrayList<>();

    @SafeVarargs
    public CompositeFilter(AbstractFilter<T>... filters) {
        Collections.addAll(this.filters, filters);
    }

    public void add(AbstractFilter<T> filter) {
        filters.add(filter);
    }

    public List<AbstractFilter<T>> getFilters() {
        return filters;
    }

    @Override
    public void accept(FiltersVisitor<T> visitor) {
        visitor.visit(this);
    }

}
