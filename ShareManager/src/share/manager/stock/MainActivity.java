package share.manager.stock;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import share.manager.adapters.CompanyAdapter;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
					rec = rec.substring(start+1, rec.length()-1);
					
					JSONObject json = null;
					try {
						json = new JSONObject(rec);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					parseJSON(json);
					
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
		setContentView(R.layout.list_results);
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
		actionBar.addTab(actionBar.newTab().setText("My Shares")
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
		
		pDiag = ProgressDialog.show(MainActivity.this, "",
				"Fetching results...", true);
		
		currentFunction = RESTFunction.GET_COMPANY_NAMES;
		ConnectionThread dataThread = new ConnectionThread(
			link, threadConnectionHandler, MainActivity.this);
		dataThread.start();
	}
	
	private void parseJSON(JSONObject query) {
		JSONObject resultsSet = null;
		
		try {
			resultsSet = query.getJSONObject("ResultSet");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
				
		JSONArray results = null;
		ListView listResults = (ListView) findViewById(R.id.list_search_results);
		try {
			results = resultsSet.getJSONArray("Result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> ticks = new ArrayList<String>(), 
				names = new ArrayList<String>(), 
				regions = new ArrayList<String>();

		for (int i = 0; i < results.length(); i++) {
			try {
				JSONObject innerObject = results.getJSONObject(i);
				ticks.add(innerObject.getString("symbol"));
				names.add(innerObject.getString("name"));
				regions.add(innerObject.getString("exchDisp"));
			} catch (JSONException e) {
				// TODO: To handle later
			}
		}
		
		listResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//arg2 == position (0 -->)
				//TODO: Save to file corresponding name, tick, region and a 0
			}
			
		});
		
		listResults.setAdapter(new CompanyAdapter(MainActivity.this, R.layout.company_box, 
				names.toArray(new String[names.size()]), regions.toArray(new String[regions.size()]),
				null, null));
		
	}
}
