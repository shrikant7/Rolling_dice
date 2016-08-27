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
public class Prefs extends Activity {

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
            Preference player1 = findPreference(getString(R.string.preference_player1_key));
            final Preference player2 = findPreference(getString(R.string.preference_player2_key));

            CheckBoxPreference bot = (CheckBoxPreference) findPreference(getString(R.string.preference_bot));
            bot.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    Boolean bool = (Boolean) o;
                    if(bool) {
                        player2.getEditor().putString(getString(R.string.preference_player2_key), "BOT").apply();
                        player2.setSummary("BOT");
                        player2.setEnabled(false);
                    }
                    else
                    {
                        player2.getEditor().putString(getString(R.string.preference_player2_key), "Player2").apply();
                        player2.setSummary("Player2");
                        player2.setEnabled(true);
                    }
                    return true;
                }
            });

            //update summary according to its value.
            bindPreferenceSummaryToValue(player1);
            bindPreferenceSummaryToValue(player2);
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
                String stringValue = newValue.toString();
                if(!stringValue.equals(""))
                {
                    preference.setSummary(stringValue);
                    return true;
                }
           return false;
        }
    }
}
