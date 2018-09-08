package com.example.dervis.theguardian;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
    }

    public static class myFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        private SharedPreferences mSharedPreferences;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefrences_main);

            mSharedPreferences = getPreferenceManager().getSharedPreferences();

            Preference headlinesLimit = findPreference(getString(R.string.settings_headlines_limit_key));
            bindSummeryToValue(headlinesLimit, getString(R.string.settings_headlines_limit_default_value));

            Preference section = findPreference(getString(R.string.settings_section_key));
            bindSummeryToValue(section, getString(R.string.settings_headlines_limit_default_value));

            Preference orderBy = findPreference(getString(R.string.order_by_key));
            bindSummeryToValue(orderBy, getString(R.string.order_by_default_value));
        }

        private void bindSummeryToValue(Preference preference, String defaultValue) {
            preference.setOnPreferenceChangeListener(this);

            String key = preference.getKey();

            String value = mSharedPreferences.getString(key, defaultValue);

            onPreferenceChange(preference, value);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;

                CharSequence[] labels = listPreference.getEntries();

                int indexOfValue = listPreference.findIndexOfValue(value);

                preference.setSummary(labels[indexOfValue]);
            } else {
                preference.setSummary(value);
            }
            return true;
        }
    }
}
