package share.manager.stock;

import java.util.ArrayList;
import java.util.Calendar;

import share.manager.connection.ConnectionThread;
import share.manager.utils.CompanyGraphicsBuilder;
import share.manager.utils.ShareUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class CompanyActivity extends Activity {
	
	private ShareManager app;
	private ProgressDialog progDiag;
	private String name, tick, region, stock;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			createGraph((ArrayList<String>) msg.obj);
			progDiag.dismiss();
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
		this.stock = splitUp[3];
		
		setTitle(this.name);
		
		app = (ShareManager) getApplicationContext();
		
		progDiag = ProgressDialog.show(
                CompanyActivity.this, "",
                "Loading, please wait!", true);
		
		int daysToBacktrack = app.getDays() * -1;
		
		Calendar backtrack = Calendar.getInstance();
		backtrack.add(Calendar.DATE, daysToBacktrack);
		Calendar current = Calendar.getInstance();
		
		ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(backtrack.get(Calendar.MONTH), backtrack.get(Calendar.DAY_OF_MONTH), backtrack.get(Calendar.YEAR),
						current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.YEAR), app.getPeriodicity(), this.tick), 
						threadConnectionHandler, this);
		dataThread.start();
	}
	
	private void createGraph(ArrayList<String> received) {
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Float> values = new ArrayList<Float>();
		
		for(int i = 1; i < received.size(); i++) {
			String[] split = received.get(i).split(",");
			String close = split[4];
			dates.add(split[0].split("-")[2]+"/"+split[0].split("-")[1]);
			values.add(Float.parseFloat(close));
		}
		
		new CompanyGraphicsBuilder(CompanyActivity.this, values, dates, null);
	}
}
