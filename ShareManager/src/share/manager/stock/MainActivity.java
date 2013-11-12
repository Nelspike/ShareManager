package share.manager.stock;

import share.manager.adapters.MainPagerAdapter;
import share.manager.connection.ConnectionThread;
import share.manager.listeners.BusTabListener;
import share.manager.listeners.SwipeListener;
import share.manager.utils.ShareUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class MainActivity extends FragmentActivity {


	/*	ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(10, 10, 2013, 11, 10, 2013, 'w', "DELL"), threadConnectionHandler, null, this);
		dataThread.start();
	 */
	
	MainPagerAdapter mCentralActivity;
	private ViewPager mViewPager;
	private ShareManager app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		app = (ShareManager) getApplicationContext();
		tabHandler();
		
		/*
		 * DELL
		 * HP
		 * GOOD
		 * FB
		 * MSFT
		 */
		
		/*ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(10, 10, 2013, 11, 10, 2013, 'w', "GOOG"), threadConnectionHandler, null, this);
		dataThread.start();*/
		
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      
	      //This will return an adapter to place on a listview that we shall create later
	      ShareUtils.searchInDB(MainActivity.this, query);
	    }
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);         
        }
	}
	
	public void tabHandler() {
		mCentralActivity = new MainPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.MainPager);
		mViewPager.setAdapter(mCentralActivity);
		mViewPager.setOnPageChangeListener(new SwipeListener(mViewPager, MainActivity.this));
		//app.setAppViewPager(mViewPager);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new BusTabListener(mViewPager);

		actionBar.addTab(actionBar.newTab().setText("Portfolio")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("My Shares")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Overall Shares")
				.setTabListener(tabListener));
	}
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), MainActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        
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
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
