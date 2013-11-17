package share.manager.stock;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsActivity extends PreferenceActivity {
	
    private PreferencesFragment prefFrag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefFrag = new PreferencesFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefFrag).commit();
	}

    public static class PreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }        
    }

}
