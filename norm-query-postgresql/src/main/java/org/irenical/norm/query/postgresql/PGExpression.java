package org.irenical.norm.query.postgresql;


public interface PGExpression {

    PGExpression eq();
    
    PGExpression gt();
    
    PGExpression gte();
    
    PGExpression lt();
    
    PGExpression lte();
    
    PGExpression in();
    
    PGExpression like();
    
    PGExpression ilike();
    
}
