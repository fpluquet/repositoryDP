package repositories.common.filters.visitors;

import repositories.common.filters.*;

public class FiltersVisitor<T> {

    public void visit(AbstractFilter<T> filter) {}
    public void visit(FilterEquals<T> filter) {}
    public void visit(FilterContains<T> filter) {}
    public void visit(CompositeFilter<T> filter) {}
    public void visit(CompositeAndFilter<T> filter) {}
    public void visit(CompositeOrFilter<T> filter) {}

}
