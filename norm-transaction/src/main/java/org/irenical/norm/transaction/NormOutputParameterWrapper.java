package org.irenical.norm.transaction;

public class NormOutputParameterWrapper {
    
    private final Object value;
    
    public NormOutputParameterWrapper(Object parameter){
        value = parameter;
    }
    
    public Object getValue() {
        return value;
    }

}
