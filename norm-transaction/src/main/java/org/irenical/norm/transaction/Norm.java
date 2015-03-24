package org.irenical.norm.transaction;

import java.util.stream.Stream;

import org.irenical.norm.NormRow;
import org.irenical.norm.query.NormQueryBuilder;

public class Norm {

    public static <EXCEPTION extends Throwable> NormTransaction<EXCEPTION> begin(NormConnectionPoller<EXCEPTION> poller) {
	NormTransaction<EXCEPTION> result = new NormTransaction<>();
	result.setPoller(poller);
	return result;
    }

	public static <POLLER_EXCEPTION extends Throwable> Stream<NormRow> streamSelect(NormConnectionPoller<POLLER_EXCEPTION> poller, NormQueryBuilder queryBuilder, Object ... parameters) throws NormException, POLLER_EXCEPTION {
		NormTransaction<POLLER_EXCEPTION> tran = begin(poller);
		return tran.singleSelect(queryBuilder, ()->parameters);
    }
	
}
