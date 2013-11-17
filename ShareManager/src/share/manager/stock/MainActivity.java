package share.manager.stock;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import share.manager.adapters.CompanyAdapter;
import share.manager.adapters.MainPagerAdapter;
import share.manager.connection.ConnectionThread;
import share.manager.listeners.BusTabListener;
import share.manager.listeners.SwipeListener;
import share.manager.utils.FileHandler;
import share.manager.utils.RESTFunction;
import share.manager.utils.ShareUtils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	MainPagerAdapter mMainActivity;
	private ViewPager mViewPager;
	private ProgressDialog pDiag;
	private RESTFunction currentFunction;
	private JSONArray result;
	private int position = 0;
	private ShareManager app;
	private Toast toast;
	private final ArrayList<String> ticks = new ArrayList<String>(), names = new ArrayList<String>(), regions = new ArrayList<String>();
	 
	
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
					break;
				case VALIDITY_CHECK:
					String code = ((ArrayList<String>) msg.obj).get(0);
					
					if(!code.equals("404")) {
						String toSave = names.get(position) + "|" + ticks.get(position) + "|" + regions.get(position) + "|0";
						FileHandler.writeToFile(toSave);
						showAToast(names.get(position) + " successfully followed!");
					}
					else {
						showAToast("Can't follow " + names.get(position) + " right now. Try later.");
					}
					
					break;
				default:
					break;
			}		
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (ShareManager) getApplicationContext();
		
		app.setPeriodicity(((String) PreferenceManager.getDefaultSharedPreferences(this).getAll().get(app.KEY_PREF_PERIODICITY)).charAt(0));
		app.setDays(Integer.parseInt(((String) PreferenceManager.getDefaultSharedPreferences(this).getAll().get(app.KEY_PREF_DAYS))));
		app.setCurrency((String) PreferenceManager.getDefaultSharedPreferences(this).getAll().get(app.KEY_PREF_CURRENCY));
		
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			setContentView(R.layout.list_results);
			String query = intent.getStringExtra(SearchManager.QUERY);
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
        /*if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);         
        }*/
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
				
		result = null;
		
		try {
			result = resultsSet.getJSONArray("Result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		int amount = result.length();

		for (int i = 0; i < amount; i++) {
			try {
				JSONObject innerObject = result.getJSONObject(i);
				String ticker = innerObject.getString("symbol");
				
				if(ticker.contains("MX")) {
					amount--;
					result = ShareUtils.remove(i, result);
				}
			} catch (JSONException e) {
				// TODO: To handle later
			}
		}
		
		fillList();
	}
	
	private void fillList() {		 
		ListView listResults = (ListView) findViewById(R.id.list_search_results);
		
		for (int i = 0; i < result.length(); i++) {
			try {
				JSONObject innerObject = result.getJSONObject(i);			
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
				position = arg2;
				currentFunction = RESTFunction.VALIDITY_CHECK;
				
				int daysToBacktrack = app.getDays() * -1;
				
				Calendar backtrack = Calendar.getInstance();
				backtrack.add(Calendar.DATE, daysToBacktrack);
				Calendar current = Calendar.getInstance();
				
				ConnectionThread dataThread = new ConnectionThread(
						app.yahooChart+ShareUtils.createChartLink(backtrack.get(Calendar.MONTH), backtrack.get(Calendar.DAY_OF_MONTH), backtrack.get(Calendar.YEAR),
								current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.YEAR), app.getPeriodicity(), ticks.get(arg2)), 
								threadConnectionHandler, MainActivity.this);
				dataThread.start();
			}
		});
		
		listResults.setAdapter(new CompanyAdapter(MainActivity.this, R.layout.company_box, 
				names.toArray(new String[names.size()]), regions.toArray(new String[regions.size()]),
				null, null));
		
		pDiag.dismiss();
	}
	
	private void showAToast(String st) {
		try {
			if (toast != null) {
				toast.getView().isShown();
				toast.setText(st);
			} else {
				toast = Toast.makeText(MainActivity.this, st,
						Toast.LENGTH_SHORT);
			}
		} catch (Exception e) {
			toast = Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT);
		}
		toast.show();
	}
}
