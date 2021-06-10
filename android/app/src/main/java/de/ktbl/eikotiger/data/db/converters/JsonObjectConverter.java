package de.ktbl.eikotiger.data.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import timber.log.Timber;

/* In order to use type converters, add annotation to db class @TypeConverters({DateConverter.class, TagListConverter.class})*/
public class JsonObjectConverter {

        private static final String TAG = JsonObjectConverter.class.getSimpleName();

        @TypeConverter
        public JsonObject fromString(String json) {
            Gson gson = new Gson();
            if(json == null || json.trim().isEmpty()){
                json = "{}"; //the empty json object
            }
            //Timber.tag(TAG).v("Object from String %s",json);
            JsonElement element = gson.fromJson(json, JsonElement.class);
            return element.getAsJsonObject();
        }

        @TypeConverter
        public String toString(JsonObject obj) {
            //Timber.tag(TAG).v("Object to String %s",obj);
            return obj == null ? "{}" : obj.toString();
        }

}
