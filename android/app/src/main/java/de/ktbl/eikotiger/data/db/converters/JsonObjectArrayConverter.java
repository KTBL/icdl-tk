package de.ktbl.eikotiger.data.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

import timber.log.Timber;


//In order to use type converters, add annotation to db class @TypeConverters({DateConverter.class, TagListConverter.class})
public class JsonObjectArrayConverter {

        private static final String TAG = JsonObjectArrayConverter.class.getSimpleName();

        @TypeConverter
        public JsonObject[] fromString(String json) { // assumes this is formatted correctly as
            Gson gson = new Gson();
            //if(json != null && !json.isEmpty()) Timber.tag(TAG).v("Array from String %s",json);
            return gson.fromJson(json, JsonObject[].class);
        }

        @TypeConverter
        public String toString(JsonObject[] objects) {
            Gson gson = new Gson();
           // if(objects != null) Timber.tag(TAG).v("Array to String %s", Arrays.toString(objects));
            if(objects == null) {
                objects = new JsonObject[1];
                objects[0] = new JsonObject();
            }
            return gson.toJson(objects);
        }

}
