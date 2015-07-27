package org.irenical.norm.transaction;

public interface NormTransactionHook {

    <INPUT> void transactionStarted(NormTransaction<INPUT> transaction, INPUT in);

    <INPUT> void transactionEnded(NormTransaction<INPUT> transaction, INPUT in);

    <INPUT> void operationStarted(NormTransaction<INPUT> transaction, NormOperation<INPUT> operation, INPUT in);

    <INPUT> void operationEnded(NormTransaction<INPUT> transaction, NormOperation<INPUT> operation, INPUT in);

}
