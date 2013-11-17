package share.manager.stock;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import share.manager.adapters.MainPagerAdapter;
import share.manager.connection.ConnectionThread;
import share.manager.listeners.BusTabListener;
import share.manager.listeners.SwipeListener;
import share.manager.utils.RESTFunction;
import share.manager.utils.ShareUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
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
	
	MainPagerAdapter mMainActivity;
	private ViewPager mViewPager;
	private ProgressDialog pDiag;

	private RESTFunction currentFunction;
	//private ShareManager app;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(currentFunction) {
				case GET_COMPANY_NAMES:
					String rec = ((ArrayList<String>) msg.obj).get(0);
					int start = rec.indexOf("(");
					rec = rec.substring(start, rec.length()-2);
					
					System.out.println(rec);
					
					JSONObject json = null;
					try {
						json = new JSONObject(rec);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					pDiag.dismiss();
					break;
				default:
					break;
			}		
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//app = (ShareManager) getApplicationContext();
		
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      
	      //TODO: create a connection to this later
	      searchNew(query);
	    }
	    else {
			setContentView(R.layout.activity_main);
			tabHandler();
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
		mMainActivity = new MainPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.MainPager);
		mViewPager.setAdapter(mMainActivity);
		mViewPager.setOnPageChangeListener(new SwipeListener(mViewPager, MainActivity.this));
		//app.setAppViewPager(mViewPager);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new BusTabListener(mViewPager);

		actionBar.addTab(actionBar.newTab().setText("Greatest Change")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Following")
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
	
	private void searchNew(String query) {
		String link = ShareUtils.buildSearchLink(query);
		     
		System.out.println("Link - " + link);
		
		pDiag = ProgressDialog.show(MainActivity.this, "",
				"Fetching results...", true);
		
		currentFunction = RESTFunction.GET_COMPANY_NAMES;
		ConnectionThread dataThread = new ConnectionThread(
			link, threadConnectionHandler, MainActivity.this);
		dataThread.start();
	}
}
