package com.example.eklavya.rollingdice;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;

/**
 * Created by eklavya on 26/8/16.
 */
public class prefs extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.


        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();



    }
    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.preference_player1_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.preference_player2_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.preference_bot)));
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            // Trigger the listener immediately with the preference's
            // current value.
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if(preference instanceof CheckBoxPreference)
            {
                Log.d("prefs",newValue.toString());
            }
            else
            {
                String stringValue = newValue.toString();
                if(!stringValue.equals(""))
                {
                    preference.setSummary(stringValue);
                    return true;
                }
            }
                return  false;
        }
    }
}
