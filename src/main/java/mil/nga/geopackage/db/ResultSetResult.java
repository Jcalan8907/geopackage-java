package mil.nga.geopackage.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import mil.nga.geopackage.GeoPackageException;

/**
 * Result Set result implementation. The column index of the GeoPackage core is
 * 0 indexed based and ResultSets are 1 indexed based.
 * 
 * @author osbornb
 * @since 3.1.0
 */
public class ResultSetResult implements Result {

	/**
	 * Result Set
	 */
	protected ResultSet resultSet;

	/**
	 * Constructor
	 * 
	 * @param resultSet
	 *            result set
	 */
	public ResultSetResult(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * Get the Result Set
	 * 
	 * @return result set
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(int index) {
		return getValue(index, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(int index, GeoPackageDataType dataType) {

		Object value;
		try {
			value = resultSet.getObject(resultIndexToResultSetIndex(index));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get value for column index: " + index, e);
		}

		value = ResultUtils.getValue(value, dataType);

		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean moveToNext() {
		boolean next = false;
		try {
			next = resultSet.next();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to move ResultSet cursor to next", e);
		}
		return next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean moveToFirst() {
		int position = getPosition();
		boolean first = position == 1;
		if (!first) {
			if (position > 1) {
				try {
					// Throws an error for TYPE_FORWARD_ONLY
					resultSet.beforeFirst();
				} catch (SQLException e) {
					throw new GeoPackageException(
							"Failed to move ResultSet cursor to first", e);
				}
			}
			first = moveToNext();
		}
		return first;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPosition() {
		try {
			return resultSet.getRow();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to get ResultSet row", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean moveToPosition(int position) {

		boolean moved = false;

		try {
			if (resultSet.getType() != ResultSet.TYPE_FORWARD_ONLY
					|| resultSet.getRow() > position) {
				// Throws an error for TYPE_FORWARD_ONLY
				moved = resultSet.absolute(position);
			} else {
				moved = true;
				while (resultSet.getRow() < position) {
					if (!resultSet.next()) {
						moved = false;
						break;
					}
				}
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to move ResultSet cursor to position " + position,
					e);
		}

		return moved;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount() {
		int count = 0;
		try {
			count = resultSet.getMetaData().getColumnCount();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get ResultSet column count", e);
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnIndex(String columnName) {
		int index;
		try {
			index = resultSetIndexToResultIndex(
					resultSet.findColumn(columnName));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to find column index for column name: "
							+ columnName,
					e);
		}
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getType(int columnIndex) {
		int type;
		try {
			type = resultSet.getMetaData()
					.getColumnType(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get column type for column index: "
							+ columnIndex,
					e);
		}
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(int columnIndex) {
		String value;
		try {
			value = resultSet
					.getString(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get String value for column index: "
							+ columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(int columnIndex) {
		int value;
		try {
			value = resultSet.getInt(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get int value for column index: " + columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBlob(int columnIndex) {
		byte[] value;
		try {
			value = resultSet
					.getBytes(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get blob bytes for column index: " + columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(int columnIndex) {
		long value;
		try {
			value = resultSet.getLong(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get long value for column index: " + columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShort(int columnIndex) {
		short value;
		try {
			value = resultSet
					.getShort(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get short value for column index: "
							+ columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(int columnIndex) {
		double value;
		try {
			value = resultSet
					.getDouble(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get double value for column index: "
							+ columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(int columnIndex) {
		float value;
		try {
			value = resultSet
					.getFloat(resultIndexToResultSetIndex(columnIndex));
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to get float value for column index: "
							+ columnIndex,
					e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean wasNull() {
		try {
			return resultSet.wasNull();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to determine if previous value retrieved was null",
					e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			resultSet.getStatement().close();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to close ResultSet Statement",
					e);
		}
		try {
			resultSet.close();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to close ResultSet", e);
		}
	}

	/**
	 * Get the ResultSet index for the provided core Result index
	 * 
	 * @param resultIndex
	 *            result index
	 * @return result set index
	 */
	protected int resultIndexToResultSetIndex(int resultIndex) {
		return resultIndex + 1;
	}

	/**
	 * Get the core Result index for the provided ResultSet index
	 * 
	 * @param resultSetIndex
	 *            result set index
	 * @return result index
	 */
	protected int resultSetIndexToResultIndex(int resultSetIndex) {
		return resultSetIndex - 1;
	}

	/**
	 * Get the SQLite type from the ResultSetMetaData column type
	 * 
	 * @param columnType
	 *            column type
	 * @return SQLite type
	 */
	public static int resultSetTypeToSqlLite(int columnType) {

		int type;

		switch (columnType) {
		case Types.INTEGER:
		case Types.BIGINT:
		case Types.SMALLINT:
		case Types.TINYINT:
		case Types.BOOLEAN:
			type = ResultUtils.FIELD_TYPE_INTEGER;
			break;
		case Types.VARCHAR:
		case Types.DATE:
		case Types.TIMESTAMP:
		case Types.CLOB:
		case Types.CHAR:
		case Types.NUMERIC:
			type = ResultUtils.FIELD_TYPE_STRING;
			break;
		case Types.REAL:
		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.DECIMAL:
			type = ResultUtils.FIELD_TYPE_FLOAT;
			break;
		case Types.BLOB:
		case Types.BINARY:
			type = ResultUtils.FIELD_TYPE_BLOB;
			break;
		case Types.NULL:
			type = ResultUtils.FIELD_TYPE_NULL;
			break;
		default:
			throw new GeoPackageException(
					"Unsupported ResultSet Metadata Column Type: "
							+ columnType);
		}

		return type;
	}

}
