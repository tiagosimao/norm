package org.irenical.norm.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.irenical.norm.NormBaseOperation;
import org.irenical.norm.NormResult;
import org.irenical.norm.NormRow;
import org.irenical.norm.NormSelect;
import org.irenical.norm.NormStream;
import org.irenical.norm.query.NormParameterFetcher;
import org.irenical.norm.query.NormQueryBuilder;
import org.irenical.norm.transaction.error.NormTransactionBeginException;

public class NormTransaction<POLLER_EXCEPTION extends Throwable> {

	private NormConnectionPoller<POLLER_EXCEPTION> poller;

	private NormBaseOperation firstOperation;

	private Connection connection;

	public void setPoller(NormConnectionPoller<POLLER_EXCEPTION> poller) {
		this.poller = poller;
	}

	private void setup() throws POLLER_EXCEPTION, NormTransactionBeginException, SQLException {
		if (connection != null) {
			throw new NormTransactionBeginException("Transaction already in progress");
		}
		connection = poller.poll();
		connection.setAutoCommit(false);
		if (connection == null) {
			throw new NormTransactionBeginException("Got no connection from poller");
		}
	}

	private void addOperation(NormBaseOperation operation) {
		if (firstOperation == null) {
			firstOperation = operation;
		} else {
			NormBaseOperation current = firstOperation;
			while (current.hasNext()) {
				current = (NormBaseOperation) current.next();
			}
			current.setNext(operation);
		}
	}

	public NormTransaction<POLLER_EXCEPTION> selectForEach(NormQueryBuilder queryBuilder, NormParameterFetcher parametersFetcher, Consumer<NormRow> consumer) {
		NormSelect select = new NormSelect();
		select.setQueryBuilder(queryBuilder);
		select.setParameterFetcher(parametersFetcher);
		select.setConsumer(consumer);
		addOperation(select);
		return this;
	}
	
	protected Stream<NormRow> singleSelect(NormQueryBuilder queryBuilder, NormParameterFetcher parametersFetcher) throws NormException, POLLER_EXCEPTION {
		NormSelect select = new NormSelect();
		select.setQueryBuilder(queryBuilder);
		select.setParameterFetcher(parametersFetcher);
		addOperation(select);
		try{
    		setup();
    		NormResult got = select.execute(connection);
    		Stream<NormRow> stream = new NormStream<NormRow>(StreamSupport.stream(Spliterators.spliteratorUnknownSize(got, 0), false), transactionInterface);
    		stream=stream.onClose(()->transactionInterface.commitAndFree());
    		return stream;
		} catch(NormException e){
			transactionInterface.rollbackAndFree();
			throw e;
		} catch(Exception e){
			transactionInterface.rollbackAndFree();
			throw new NormExecuteOperationException(e);
		}
	}

	public void execute() throws NormExecuteOperationException, POLLER_EXCEPTION, NormTransactionBeginException, SQLException {
		if(firstOperation!=null){
    		setup();
    		for (NormOperation currentOperation = firstOperation; currentOperation!=null; currentOperation = currentOperation.next()) {
    			try {
    				NormResult got = currentOperation.execute(connection);
    				Stream<NormRow> stream = new NormStream<NormRow>(StreamSupport.stream(Spliterators.spliteratorUnknownSize(got, 0), false), transactionInterface);
    				Consumer<NormRow> consumer = currentOperation.getConsumer();
    				if (consumer != null) {
    					if(!currentOperation.hasNext()){
    						stream=stream.onClose(()->transactionInterface.commitAndFree());
    					}
    					stream.forEach(consumer::accept);
    				} else {
    					throw new NormExecuteOperationException("No transaction handler for operation: " + currentOperation);
    				}
    			} catch (NormExecuteOperationException e) {
    				transactionInterface.rollbackAndFree();
    				throw e;
    			}
    		}
		}
	}

	private void silentClose() {
		NormBaseOperation current = firstOperation;
		while (current.hasNext()) {
			try {
				current.close();
			} catch (NormCloseException e) {
				e.printStackTrace();
				// ignoring errors while freeing resources
			} finally {
				current = (NormBaseOperation) current.next();
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// ignoring errors while freeing resources
		}
	}

	private final NormTransactionInterface transactionInterface = new NormTransactionInterface() {

		@Override
		public void rollbackAndFree() {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
					// ignoring errors while rollbacking
				} finally {
					silentClose();
				}
			}
		}

		@Override
		public void commitAndFree() throws NormCommitException {
			if (connection != null) {
				try {
					connection.commit();
				} catch (SQLException e) {
					throw new NormCommitException(e);
				} finally {
					silentClose();
				}
			}
		}
	};

}
