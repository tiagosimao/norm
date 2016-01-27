package org.irenical.norm.transaction;

public interface NormHook {

    <INPUT, OUTPUT> void transactionStarted(NormContext<INPUT, OUTPUT> context);

    <INPUT, OUTPUT> void transactionEnded(NormContext<INPUT, OUTPUT> context);

    <INPUT, OUTPUT> void operationStarted(NormContext<INPUT, OUTPUT> context);

    <INPUT, OUTPUT> void operationEnded(NormContext<INPUT, OUTPUT> context);

}
