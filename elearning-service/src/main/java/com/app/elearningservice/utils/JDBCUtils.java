package com.app.elearningservice.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class JDBCUtils {
    public static <T> T getValueResultSet(ResultSet rs, Class<T> tClass, T defaultValue, String key) {
        try {
            return ObjectUtils.isEmpty(rs.getObject(key)) ? defaultValue : rs.getObject(key, tClass);
        } catch (SQLException e) {
            return defaultValue;
        }
    }
}
