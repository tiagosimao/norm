package org.irenical.norm.query.postgresql.builders;

import java.util.Arrays;

import org.irenical.norm.query.NormBaseQueryBuilder;
import org.irenical.norm.query.postgresql.PGExpression;

public class PGExpressionBuilder extends NormBaseQueryBuilder<PGExpression> implements PGExpression {

  @Override
  public PGExpression not() {
    return literal(" not");
  }

  @Override
  public PGExpression in(Object... that) {
    if (that == null || that.length == 0) {
      return literal(" in");
    } else {
      return eq(that);
    }
  }

  @Override
  public PGExpression eq(Object... that) {
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
  public PGExpression notEq(Object... that) {
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

  private PGExpression binaryOperation(String op, Object... that) {
    if (that == null || that.length == 0) {
      return literal(op);
    } else {
      return literal(op).value(that[0]);
    }
  }

  @Override
  public PGExpression gt(Object... that) {
    return binaryOperation(">", that);
  }

  @Override
  public PGExpression lte(Object... that) {
    return binaryOperation("<=", that);
  }

  @Override
  public PGExpression lt(Object... that) {
    return binaryOperation("<", that);
  }

  @Override
  public PGExpression gte(Object... that) {
    return binaryOperation(">=", that);
  }

  @Override
  public PGExpression ilike(Object... that) {
    return binaryOperation(" ilike ", that);
  }

  @Override
  public PGExpression like(Object... that) {
    return binaryOperation(" like ", that);
  }

  @Override
  public PGExpression divide(Object... that) {
    return binaryOperation("/", that);
  }

  @Override
  public PGExpression multiply(Object... that) {
    return binaryOperation("*", that);
  }

  @Override
  public PGExpression minus(Object... that) {
    return binaryOperation("-", that);
  }

  @Override
  public PGExpression plus(Object... that) {
    return binaryOperation("+", that);
  }

  @Override
  public PGExpression or(Object... that) {
    for (Object object : that) {
      literal(" or ");
      literal(object);
    }
    return this;
  }

  @Override
  public PGExpression and(Object... that) {
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
