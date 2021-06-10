package de.ktbl.eikotiger.util;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferenceUtil {

    private final SharedPreferences preferences;

    @Inject
    SharedPreferenceUtil(SharedPreferences sharedPreferences) {
        preferences = sharedPreferences;
    }

    public void putString(@NonNull String key, @NonNull String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(@NonNull String key) {
        return preferences.getString(key, "");
    }

    public void putBoolean(@NonNull String key, @NonNull boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(@NonNull String key) {
        return preferences.getBoolean(key, false);
    }

    public void putInt(@NonNull String key, @NonNull int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(@NonNull String key) {
        return preferences.getInt(key, -1);
    }

    public void putLong(@NonNull String key, @NonNull long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public long getLong(@NonNull String key) {
        return preferences.getLong(key, -1);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
