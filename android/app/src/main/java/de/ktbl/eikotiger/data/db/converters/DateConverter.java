package de.ktbl.eikotiger.data.db.converters;

import java.util.Date;

import androidx.room.TypeConverter;

/* In order to use type converters, add annotation to db class @TypeConverters({DateConverter.class, TagListConverter.class})*/
public class DateConverter {

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}