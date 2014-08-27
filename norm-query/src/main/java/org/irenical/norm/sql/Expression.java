package org.irenical.norm.sql;

/**
 * Created by tgsimao on 08/05/14.
 */
public class Expression extends NormSelect {

    protected NormSelect parent;

    protected String expression;

    protected String alias;

    protected Expression(){
    }

    @Override
    public NormSelect as(String alias){
        this.alias=alias;
        return parent;
    }

}
