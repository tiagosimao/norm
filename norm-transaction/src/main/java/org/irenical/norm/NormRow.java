package org.irenical.norm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

import org.irenical.norm.transaction.NormResultException;

public class NormRow {

	private final ResultSet rs;

	public NormRow(ResultSet rs) {
		this.rs = rs;
	}

	public ResultSet getResultSet() {
		return rs;
	}

	public <OUTPUT> OUTPUT get(String key, OUTPUT defaultValue, Function<Object, OUTPUT> transform) {
		OUTPUT result = null;
		try {
			Object got = rs.getObject(key);
			result = transform.apply(got);
		} catch (SQLException e) {
			throw new NormResultException(e);
		}
		return result == null ? defaultValue : result;
	}

	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String defaultValue) {
		return get(key, defaultValue, from -> from == null ? null : from.toString());
	}
	
	public Integer getInteger(String key) {
		return getInteger(key,null);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return get(key, defaultValue, from -> { 
			try{
				return from == null ? null : Integer.parseInt(from.toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
				return defaultValue;
			}
		});
	}
	
	public LocalDateTime getLocalDateTime(String key){
		return getLocalDateTime(key,null);
	}
	
	public LocalDateTime getLocalDateTime(String key, LocalDateTime defaultValue) {
		return get(key, defaultValue, from -> { 
				if(from instanceof LocalDateTime) {
					return (LocalDateTime) from;
				} else if(from instanceof Timestamp){
					return ((Timestamp)from).toLocalDateTime();
				} else if(from instanceof Date){
					return LocalDateTime.ofInstant(Instant.ofEpochMilli(((Date)from).getTime()), ZoneId.systemDefault());
				} else {
					return defaultValue;
				}
		});
	}
	
}
