package share.manager.stock;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	
    private PreferencesFragment prefFrag;
    private ShareManager app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefFrag = new PreferencesFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefFrag).commit();
		app = (ShareManager) getApplication();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		  @Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			  if(key.equals(app.KEY_PREF_PERIODICITY))
				  app.setPeriodicity(prefs.getString(key, "d").charAt(0));
			  else if(key.equals(app.KEY_PREF_DAYS))
				  app.setDays(Integer.parseInt(prefs.getString(key, "30")));
			  else if(key.equals(app.KEY_PREF_CURRENCY))
				  app.setCurrency(prefs.getString(key, "usd"));			  
		  }
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

    public static class PreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }        
    }

}
