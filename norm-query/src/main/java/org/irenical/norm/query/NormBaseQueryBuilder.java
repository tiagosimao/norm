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
    public BUILDER_CLASS builder(NormQueryBuilder<?>... builders) {
        if (builders != null) {
            for (NormQueryBuilder<?> builder : builders) {
                if (builder != null) {
                    sb.append(builder.getQuery());
                    List<Object> builderParams = builder.getParameters();
                    if (builderParams != null) {
                        parameters.addAll(builderParams);
                    }
                }
            }
        }
        return (BUILDER_CLASS) this;
    }

    @Override
    public BUILDER_CLASS literal(Object sql) {
        return smartLiteral(sql);
    }
    
    private BUILDER_CLASS smartLiteral(Object sql) {
        if (sql instanceof NormQueryBuilder<?>) {
            return builder((NormQueryBuilder<?>) sql);
        } else {
            sb.append(sql);
            return (BUILDER_CLASS) this;
        }
    }

    @Override
    public BUILDER_CLASS literals(Iterable<Object> sql, String prefix, String suffix, String separator) {
        if (sql != null) {
            boolean first = true;
            for (Object s : sql) {
                if (first) {
                    if (prefix != null) {
                        sb.append(prefix);
                    }
                } else if (separator != null) {
                    sb.append(separator);
                }
                smartLiteral(s);
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
        return smartValue(value);
    }
    
    private BUILDER_CLASS smartValue(Object value) {
        if (value instanceof NormQueryBuilder<?>) {
            return builder((NormQueryBuilder<?>) value);
        } else {
            sb.append(VALUE);
            parameters.add(value);
            return (BUILDER_CLASS) this;
        }
    }

    @Override
    public BUILDER_CLASS values(Iterable<Object> values, String prefix, String suffix, String separator) {
        if (values != null) {
            boolean first = true;
            for (Object value : values) {
                if (first) {
                    if (prefix != null) {
                        sb.append(prefix);
                    }
                } else if (separator != null) {
                    sb.append(separator);
                }
                smartValue(value);
                first = false;
            }
            if (!first && suffix != null) {
                sb.append(suffix);
            }
        }
        return (BUILDER_CLASS) this;
    }

}
