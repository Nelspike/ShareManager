package share.manager.stock;

import share.manager.adapters.MainPagerAdapter;
import share.manager.listeners.ShareTabListener;
import share.manager.listeners.SwipeListener;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class MainActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {
	
	MainPagerAdapter mMainActivity;
	private ViewPager mViewPager;
	private ShareManager app;
	private SharedPreferences prefs;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (ShareManager) getApplicationContext();
		
		app.setPeriodicity(((String) PreferenceManager.getDefaultSharedPreferences(this).getAll().get(app.KEY_PREF_PERIODICITY)).charAt(0));
		app.setDays(Integer.parseInt(((String) PreferenceManager.getDefaultSharedPreferences(this).getAll().get(app.KEY_PREF_DAYS))));
		app.setCurrency((String) PreferenceManager.getDefaultSharedPreferences(this).getAll().get(app.KEY_PREF_CURRENCY));
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		setContentView(R.layout.activity_main);
		tabHandler();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), ResultsActivity.class)));
        searchView.setIconifiedByDefault(false);
        
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            return true;
	        case R.id.action_settings:
	            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
	            startActivity(intent);
	            app.setAccessedSettings(true);
	            prefs.registerOnSharedPreferenceChangeListener(this);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void tabHandler() {
		mMainActivity = new MainPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.MainPager);
		mViewPager.setAdapter(mMainActivity);
		mViewPager.setOnPageChangeListener(new SwipeListener(mViewPager, MainActivity.this));
		mViewPager.setOffscreenPageLimit(2);
		//app.setAppViewPager(mViewPager);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new ShareTabListener(mViewPager);

		actionBar.addTab(actionBar.newTab().setText("My Shares")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Following")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Latest")
				.setTabListener(tabListener));
	}
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (key.equals(app.KEY_PREF_PERIODICITY))
			app.setPeriodicity(prefs.getString(key, "d").charAt(0));
		else if (key.equals(app.KEY_PREF_DAYS))
			app.setDays(Integer.parseInt(prefs.getString(key, "30")));
		else if (key.equals(app.KEY_PREF_CURRENCY))
			app.setCurrency(prefs.getString(key, "usd"));
	}
}
