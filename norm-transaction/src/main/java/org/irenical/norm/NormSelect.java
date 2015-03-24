package org.irenical.norm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.irenical.norm.query.NormParameterFetcher;
import org.irenical.norm.query.NormQueryBuilder;
import org.irenical.norm.transaction.NormCloseException;
import org.irenical.norm.transaction.NormSelectException;

public class NormSelect extends NormBaseOperation {

	private NormParameterFetcher parameterFetcher;

	private NormQueryBuilder queryBuilder;

	private PreparedStatement ps;

	private ResultSet rs;

	public void setParameterFetcher(NormParameterFetcher parameterFetcher) {
		this.parameterFetcher = parameterFetcher;
	}

	public void setQueryBuilder(NormQueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	@Override
	public NormResult execute(Connection connection) throws NormSelectException {
		try {
			ps = connection.prepareStatement(queryBuilder.render());
			Object[] parameters = parameterFetcher == null ? null : parameterFetcher.fetch();
			prepareInput(new int[]{1},ps, parameters);
			rs = ps.executeQuery();
			NormResult result = new NormResult(rs);
			return result;
		} catch (SQLException e) {
			throw new NormSelectException(e);
		}
	}

	@Override
	public void close() throws NormCloseException {
		try {
			ps.close();
			rs.close();
		} catch (SQLException e) {
			throw new NormCloseException(e);
		}
	}

}
