package org.irenical.norm;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.function.Consumer;

import org.irenical.norm.transaction.NormOperation;

public abstract class NormBaseOperation implements NormOperation {

	private NormOperation next;

	private Consumer<NormRow> consumer;

	public void setNext(NormOperation operation) {
		this.next = operation;
	}

	public void setConsumer(Consumer<NormRow> consumer) {
		this.consumer = consumer;
	}

	@Override
	public Consumer<NormRow> getConsumer() {
		return consumer;
	}

	@Override
	public NormOperation next() {
		return next;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}
	
	protected static void prepareInput(int [] ipointer,PreparedStatement preparedStatement, Object... parameters) throws SQLException {
		if (preparedStatement != null && parameters != null && parameters.length > 0) {
			for (Object param : parameters) {
				setInput(preparedStatement, ipointer, param);
				ipointer[0]++;
			}
		}
	}

	private static void setInput(PreparedStatement preparedStatement, int [] ipointer, Object value) throws SQLException {
		if (value instanceof Timestamp) {
			preparedStatement.setTimestamp(ipointer[0], (Timestamp) value);
		} else if (value instanceof Time) {
			preparedStatement.setTime(ipointer[0], (Time) value);
		} else if (value instanceof Date) {
			preparedStatement.setDate(ipointer[0], (Date) value);
		} else if (value instanceof Enum<?>) {
			preparedStatement.setString(ipointer[0], value.toString());
		} else if(value instanceof Collection<?>){
			prepareInput(ipointer, preparedStatement, ((Collection<?>) value).toArray());
		} else {
			preparedStatement.setObject(ipointer[0], value);
		}
	}

}
