package repositories.filters;

import repositories.filters.visitors.FiltersVisitor;

public class FilterEquals<T> extends AbstractFilter<T> {

        private final String key;
        private final String value;

        public FilterEquals(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public void accept(FiltersVisitor<T> visitor) {
            visitor.visit(this);
        }
}
