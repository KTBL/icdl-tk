package de.ktbl.eikotiger.data.db.converters;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


    public class TagListConverter {
        public final Character SEPERATOR = ','; //This is the default sqlite group_concat seperator

        @TypeConverter
        public List<String> fromString(String value) {
            String[] split = value.split(SEPERATOR.toString());
            List<String> list = new ArrayList<>();
            for (String s : split) {
                list.add(s.trim()); //strip white spaces
            }
            return value.isEmpty() ? list : list;
        }

        @TypeConverter
        public String listToString(List<String> list) {
            StringBuilder tags = new StringBuilder();
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()){
                if (tags.toString().equals("")) tags = new StringBuilder(iterator.next());
                else tags.append(", ").append(iterator.next().replaceAll(SEPERATOR.toString(), "" ));  //make it human readable, but strip usage of seperator
            }
            return tags.toString();
        }
    }

