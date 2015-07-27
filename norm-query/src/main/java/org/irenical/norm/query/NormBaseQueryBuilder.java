package org.irenical.norm.query;

import java.util.LinkedList;
import java.util.List;

public abstract class NormBaseQueryBuilder<BUILDER_CLASS extends NormQueryBuilder<BUILDER_CLASS>> implements NormQueryBuilder<BUILDER_CLASS> {

    private static final char VALUE = '?';

    private final List<Object> parameters = new LinkedList<Object>();

    private final StringBuilder sb = new StringBuilder();

    public NormBaseQueryBuilder() {
    }

    @Override
    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public String getQuery() {
        return sb.toString();
    }

    @Override
    public BUILDER_CLASS literal(String sql) {
        sb.append(sql);
        return (BUILDER_CLASS) this;
    }

    @Override
    public BUILDER_CLASS literals(Iterable<String> sql, String prefix, String suffix, String separator) {
        if (sql != null) {
            boolean first = true;
            for (Object s : sql) {
                if (first && prefix != null) {
                    sb.append(prefix);
                } else if (separator != null) {
                    sb.append(separator);
                }
                sb.append(s);
                first = false;
            }
            if (!first && suffix != null) {
                sb.append(suffix);
            }
        }
        return (BUILDER_CLASS) this;
    }

    @Override
    public BUILDER_CLASS value(Object value) {
        sb.append(VALUE);
        parameters.add(value);
        return (BUILDER_CLASS) this;
    }

    @Override
    public BUILDER_CLASS values(Iterable<Object> values, String prefix, String suffix, String separator) {
        if (values != null) {
            boolean first = true;
            for (Object value : values) {
                if (first && prefix != null) {
                    sb.append(prefix);
                } else if (separator != null) {
                    sb.append(separator);
                }
                sb.append(VALUE);
                parameters.add(value);
                first = false;
            }
            if (!first && suffix != null) {
                sb.append(suffix);
            }
        }
        return (BUILDER_CLASS) this;
    }

}
