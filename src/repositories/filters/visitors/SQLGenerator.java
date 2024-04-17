package repositories.filters.visitors;

import repositories.filters.*;

public class SQLGenerator<T> extends FiltersVisitor<T> {

    private StringBuilder sql = new StringBuilder();

    public String generateSQL(AbstractFilter<T> filter) {
        sql = new StringBuilder();
        filter.accept(this);
        return sql.toString();
    }

    private String escapeString(String str) {
        return str.replace("'", "''");
    }

    @Override
    public void visit(FilterEquals<T> filterEquals) {
        sql.append(filterEquals.getKey())
                .append(" = '")
                .append(escapeString(filterEquals.getValue()))
                .append("'");
    }

    @Override
    public void visit(FilterContains<T> filterContains) {
        sql.append(filterContains.getKey())
                .append(" LIKE '%")
                .append(escapeString(filterContains.getText()))
                .append("%'");
    }

                      @Override
    public void visit(CompositeAndFilter<T> filter) {
        sql.append("(");
        for (int i = 0; i < filter.getFilters().size(); i++) {
            filter.getFilters().get(i).accept(this);
            if (i < filter.getFilters().size() - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(")");
    }

    @Override
    public void visit(CompositeOrFilter<T> filter) {
        sql.append("(");
        for (int i = 0; i < filter.getFilters().size(); i++) {
            filter.getFilters().get(i).accept(this);
            if (i < filter.getFilters().size() - 1) {
                sql.append(" OR ");
            }
        }
        sql.append(")");
    }
}
