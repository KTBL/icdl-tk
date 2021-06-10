package de.ktbl.eikotiger.data.db.converters;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class StringArrayConverter {
    public final Character SEPERATOR = ','; //This is the default sqlite group_concat seperator

    @TypeConverter
    public String[] fromString(String value) {
        String[] split = value.split(SEPERATOR.toString());
        return value.isEmpty() ? split : split;
    }

    @TypeConverter
    public String arrayToString(String[] strings) {
        if (strings != null) {
            StringBuilder tags = new StringBuilder();
            for (int i=0; i<strings.length;i++){
             if (tags.toString().equals("")) tags = new StringBuilder(strings[i]);
                else tags.append(", ").append(strings[i].replaceAll(SEPERATOR.toString(), "" ));  //make it human readable, but strip usage of seperator
        }
        return tags.toString();
       } else return "";
    }
}

