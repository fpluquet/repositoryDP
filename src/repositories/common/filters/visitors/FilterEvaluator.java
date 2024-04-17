package repositories.common.filters.visitors;

import repositories.common.filters.*;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilterEvaluator<T> extends FiltersVisitor<T> {

    private boolean isTaken = true;
    private final T value;

    public FilterEvaluator(T value) {
        this.value = value;
    }

    public static <T> boolean match(T a, AbstractFilter<T> filter) {
        FilterEvaluator<T> filterEvaluator = new FilterEvaluator<T>(a);
        filter.accept(filterEvaluator);
        return filterEvaluator.isTaken;
    }

    public void visit(FilterEquals<T> filterEquals) {
        try {
            Field field = this.value.getClass().getDeclaredField(filterEquals.getKey());
            field.setAccessible(true); // private field can be accessed
            isTaken = isTaken && Objects.equals(field.get(this.value), filterEquals.getValue());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            isTaken = false;
            Logger.getLogger("FilterTester").log(Level.SEVERE, "Field " + filterEquals.getKey() + " not found ", e);
        }
    }

    public void visit(FilterContains<T> filterContains) {
        try {
            Field field = this.value.getClass().getDeclaredField(filterContains.getKey());
            field.setAccessible(true); // private field can be accessed
            isTaken = isTaken && field.get(this.value).toString().contains(filterContains.getText());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            isTaken = false;
            Logger.getLogger("FilterTester").log(Level.SEVERE, "Field " + filterContains.getKey() + " not found ", e);
        }
    }

    @Override
    public void visit(CompositeAndFilter<T> filter) {
        isTaken = true;
        for (AbstractFilter<T> f : filter.getFilters()) {
            isTaken = isTaken && FilterEvaluator.match(this.value, f);
            if (!isTaken) {
                break;
            }
        }
    }

    @Override
    public void visit(CompositeOrFilter<T> filter) {
        isTaken = false;
        for (AbstractFilter<T> f : filter.getFilters()) {
            isTaken = isTaken || FilterEvaluator.match(this.value, f);
            if (isTaken) {
                break;
            }
        }
    }





}
