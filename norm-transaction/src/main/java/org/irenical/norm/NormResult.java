package org.irenical.norm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.irenical.norm.transaction.NormResultException;

public class NormResult implements Iterator<NormRow> {

	private final ResultSet resultSet;

	public NormResult(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	@Override
	public boolean hasNext() {
		boolean result = false;
		try {
			result = !resultSet.isLast();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public NormRow next() {
		NormRow result = null;
		try {
			if (resultSet.next()) {
				result = new NormRow(resultSet);
			} else {
				throw new NormResultException("Reached end of ResultSet");
			}
		} catch (SQLException e) {
			throw new NormResultException(e);
		}
		return result;
	}
	
}
