package repositories.filters;

import repositories.filters.visitors.FiltersVisitor;

public class FilterContains<T> extends AbstractFilter<T> {

        private final String key;
        private final String text;

        public FilterContains(String key, String text) {
            this.key = key;
            this.text = text;
        }

        public String getKey() {
            return key;
        }

        public String getText() {
            return text;
        }

        @Override
        public void accept(FiltersVisitor<T> visitor) {
            visitor.visit(this);
        }
}
