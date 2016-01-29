package org.irenical.norm.query.postgresql.builders;

import java.util.Arrays;

import org.irenical.norm.query.NormBaseQueryBuilder;
import org.irenical.norm.query.postgresql.templates.ExpressionTemplate;

public class ExpressionBuilder extends NormBaseQueryBuilder<ExpressionTemplate> implements ExpressionTemplate {

  @Override
  public ExpressionTemplate not() {
    return literal(" not");
  }

  @Override
  public ExpressionTemplate in(Object... that) {
    if (that == null || that.length == 0) {
      return literal(" in");
    } else {
      return eq(that);
    }
  }

  @Override
  public ExpressionTemplate eq(Object... that) {
    if (that != null) {
      if (that.length == 1) {
        if (that[0] == null) {
          return literal(" is null");
        } else {
          return literal("=").value(that[0]);
        }
      } else {
        return values(Arrays.asList(that), " in(", ")", ",");
      }
    } else {
      return literal("=");
    }
  }

  @Override
  public ExpressionTemplate notEq(Object... that) {
    if (that != null) {
      if (that.length == 1) {
        if (that[0] == null) {
          return literal(" is not null");
        } else {
          return literal("!=").value(that[0]);
        }
      } else {
        literal(" not");
        return in(that);
      }
    } else {
      return literal("!=");
    }
  }

  private ExpressionTemplate binaryOperation(String op, Object... that) {
    if (that == null || that.length == 0) {
      return literal(op);
    } else {
      return literal(op).value(that[0]);
    }
  }

  @Override
  public ExpressionTemplate gt(Object... that) {
    return binaryOperation(">", that);
  }

  @Override
  public ExpressionTemplate lte(Object... that) {
    return binaryOperation("<=", that);
  }

  @Override
  public ExpressionTemplate lt(Object... that) {
    return binaryOperation("<", that);
  }

  @Override
  public ExpressionTemplate gte(Object... that) {
    return binaryOperation(">=", that);
  }

  @Override
  public ExpressionTemplate ilike(Object... that) {
    return binaryOperation(" ilike ", that);
  }

  @Override
  public ExpressionTemplate like(Object... that) {
    return binaryOperation(" like ", that);
  }

  @Override
  public ExpressionTemplate divide(Object... that) {
    return binaryOperation("/", that);
  }

  @Override
  public ExpressionTemplate multiply(Object... that) {
    return binaryOperation("*", that);
  }

  @Override
  public ExpressionTemplate minus(Object... that) {
    return binaryOperation("-", that);
  }

  @Override
  public ExpressionTemplate plus(Object... that) {
    return binaryOperation("+", that);
  }

  @Override
  public ExpressionTemplate or(Object... that) {
    for (Object object : that) {
      literal(" or ");
      literal(object);
    }
    return this;
  }

  @Override
  public ExpressionTemplate and(Object... that) {
    for (Object object : that) {
      literal(" and ");
      literal(object);
    }
    return this;
  }

  @Override
  public String toString() {
    return getQuery();
  }

}
