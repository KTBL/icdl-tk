package de.ktbl.eikotiger.data.db.converters;



import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.ktbl.eikotiger.data.Constants;


/* In order to use type converters, add annotation to db class @TypeConverters({DateConverter.class, TagListConverter.class})*/
public class DateStringConverter {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd",
                                                                           Locale.forLanguageTag(
                                                                                   Constants.LOCALE));

    @TypeConverter
    public Date fromString(String value) {
        Date date = new Date();
        if (value != null) {
            try {
                date = dateFormat.parse(value);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return value == null ? null : date;
    }

    @TypeConverter
    public String dateToString(Date date) {
        return date == null ? null : dateFormat.format(date);
    }
}