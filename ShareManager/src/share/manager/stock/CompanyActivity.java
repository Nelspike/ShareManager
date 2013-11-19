
package share.manager.stock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import share.manager.connection.ConnectionThread;
import share.manager.utils.CompanyGraphicsBuilder;
import share.manager.utils.RESTFunction;
import share.manager.utils.ShareUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CompanyActivity extends Activity implements
		OnSharedPreferenceChangeListener {

	private ShareManager app;
	private ProgressDialog progDiag;
	private String name, tick, region;
	private SharedPreferences prefs;
	private RESTFunction currentFunc;
	private float monthlyHigh, monthlyLow, highestOpen, lowestOpen, highestClose,
			lowestClose;

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (currentFunc) {
				case GET_COMPANY_RANGE_STOCK:
					createGraph((ArrayList<String>) msg.obj);
					getQuota();
					break;
				case GET_COMPANY_STOCK:
					handleQuota((ArrayList<String>) msg.obj);
					progDiag.dismiss();
					break;
				default:
					break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);

		String info = getIntent().getStringExtra("Tick");
		String[] splitUp = info.split("\\|");
		this.name = splitUp[0];
		this.tick = splitUp[1];
		this.region = splitUp[2];

		setTitle(this.name);

		TextView regionText = (TextView) findViewById(R.id.region_company_text);
		regionText.setText("Financial region: " + this.region);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar()
				.setDisplayHomeAsUpEnabled(true);

		app = (ShareManager) getApplicationContext();

		initCompany();
	}

	private void initCompany() {
		progDiag = ProgressDialog.show(CompanyActivity.this, "",
				"Loading, please wait!", true);

		((TextView) findViewById(R.id.last_days_label)).setText("Last "
				+ app.getDays() + " days");

		int daysToBacktrack = app.getDays() * -1;

		Calendar backtrack = Calendar.getInstance();
		backtrack.add(Calendar.DATE, daysToBacktrack);
		Calendar current = Calendar.getInstance();

		currentFunc = RESTFunction.GET_COMPANY_RANGE_STOCK;

		ConnectionThread dataThread = new ConnectionThread(app.yahooChart
				+ ShareUtils.createChartLink(backtrack.get(Calendar.MONTH),
						backtrack.get(Calendar.DAY_OF_MONTH), backtrack.get(Calendar.YEAR),
						current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH),
						current.get(Calendar.YEAR), app.getPeriodicity(), this.tick),
				threadConnectionHandler, this, RESTFunction.NONE);
		dataThread.start();
	}

	private void createGraph(ArrayList<String> received) {
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Float> values = new ArrayList<Float>(), highs = new ArrayList<Float>(), lows = new ArrayList<Float>(), closes = new ArrayList<Float>(), opens = new ArrayList<Float>();

		for (int i = 1; i < received.size(); i++) {
			String[] split = received.get(i).split(",");
			String close = split[4];
			highs.add(Float.parseFloat(split[2].equals("N/A") ? "0.0" : split[2]));
			lows.add(Float.parseFloat(split[3].equals("N/A") ? "0.0" : split[3]));
			closes.add(Float.parseFloat(split[4].equals("N/A") ? "0.0" : split[4]));
			opens.add(Float.parseFloat(split[1].equals("N/A") ? "0.0" : split[1]));
			dates.add(split[0].split("-")[2] + "/" + split[0].split("-")[1]);
			values.add(Float.parseFloat(close));
		}

		Collections.sort(highs);
		Collections.sort(lows);
		Collections.sort(closes);
		Collections.sort(opens);

		monthlyHigh = highs.get(highs.size() - 1);
		monthlyLow = lows.get(lows.size() - 1);
		highestOpen = opens.get(opens.size() - 1);
		lowestOpen = opens.get(0);
		highestClose = closes.get(closes.size() - 1);
		lowestClose = closes.get(0);

		new CompanyGraphicsBuilder(CompanyActivity.this, values, dates, null);
	}

	private void getQuota() {
		String link = app.yahooQuote + this.tick;

		currentFunc = RESTFunction.GET_COMPANY_STOCK;

		ConnectionThread dataThread = new ConnectionThread(link,
				threadConnectionHandler, CompanyActivity.this, currentFunc);

		dataThread.start();
	}

	private void handleQuota(ArrayList<String> received) {
		String[] split = received.get(0).split(",");
		((TextView) findViewById(R.id.today_high_value)).setText(split[8]
				.equals("N/A") ? "-" : split[8] + " $");
		((TextView) findViewById(R.id.today_low_value)).setText(split[7]
				.equals("N/A") ? "-" : split[7] + " $");
		((TextView) findViewById(R.id.today_open_value)).setText(split[5]
				.equals("N/A") ? "-" : split[5] + " $");
		((TextView) findViewById(R.id.today_close_value)).setText(split[6]
				.equals("N/A") ? "-" : split[6] + " $");
		((TextView) findViewById(R.id.today_volume_value)).setText(split[9]
				.equals("N/A") ? "-" : split[9]);
		((TextView) findViewById(R.id.today_current_value)).setText(split[1]
				.equals("N/A") ? "-" : split[1] + " $");
		((TextView) findViewById(R.id.today_current_date_value)).setText(split[2]
				.replace("\"", ""));

		((TextView) findViewById(R.id.last_days_highest_value)).setText(String
				.format("%.2f", monthlyHigh) + " $");
		((TextView) findViewById(R.id.last_days_lowest_value)).setText(String
				.format("%.2f", monthlyLow) + " $");
		((TextView) findViewById(R.id.last_days_highest_open_value)).setText(String
				.format("%.2f", highestOpen) + " $");
		((TextView) findViewById(R.id.last_days_highest_close_value))
				.setText(String.format("%.2f", highestClose) + " $");
		((TextView) findViewById(R.id.last_days_lowest_open_value)).setText(String
				.format("%.2f", lowestOpen) + " $");
		((TextView) findViewById(R.id.last_days_lowest_close_value)).setText(String
				.format("%.2f", lowestClose) + " $");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.company, menu);
		return true;
	}

	// Response to the Up button in the action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_settings:
				Intent intent = new Intent(CompanyActivity.this, SettingsActivity.class);
				startActivity(intent);
				app.setAccessedSettings(true);
				prefs.registerOnSharedPreferenceChangeListener(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (key.equals(app.KEY_PREF_PERIODICITY)) app.setPeriodicity(prefs
				.getString(key, "d").charAt(0));
		else if (key.equals(app.KEY_PREF_DAYS)) app.setDays(Integer.parseInt(prefs
				.getString(key, "30")));
		else if (key.equals(app.KEY_PREF_CURRENCY)) app.setCurrency(prefs
				.getString(key, "usd"));
	}
}
