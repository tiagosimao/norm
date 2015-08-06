package org.irenical.norm.transaction;

public interface NTHook {

    <INPUT, OUTPUT> void transactionStarted(NTContext<INPUT, OUTPUT> context);

    <INPUT, OUTPUT> void transactionEnded(NTContext<INPUT, OUTPUT> context);

    <INPUT, OUTPUT> void operationStarted(NTContext<INPUT, OUTPUT> context);

    <INPUT, OUTPUT> void operationEnded(NTContext<INPUT, OUTPUT> context);

}
