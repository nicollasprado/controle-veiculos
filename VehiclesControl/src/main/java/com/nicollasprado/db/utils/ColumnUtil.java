package com.nicollasprado.db.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ColumnUtil {

    // Not the best way to do this but the easiest for now
    public static Object getColumnValueWithCorrespondentType(ResultSet queryResult, int columnType, int columnIndex){
        try{
            return switch (columnType) {
                case Types.VARCHAR -> queryResult.getString(columnIndex);
                case Types.INTEGER -> queryResult.getInt(columnIndex);
                case Types.DOUBLE -> queryResult.getDouble(columnIndex);
                case Types.FLOAT -> queryResult.getFloat(columnIndex);
                case Types.TIMESTAMP -> queryResult.getTimestamp(columnIndex);
                case Types.DATE -> queryResult.getDate(columnIndex);
                case Types.BOOLEAN -> queryResult.getBoolean(columnIndex);
                default -> null;
            };
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to get column value: ", e);
        }
    }
}
