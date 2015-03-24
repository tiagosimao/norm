package org.irenical.norm.transaction;


public interface NormTransactionInterface {
    
    public void rollbackAndFree();

    public void commitAndFree() throws NormCommitException;

}
