package share.manager.stock;

import java.util.ArrayList;

import share.manager.connection.ConnectionThread;
import share.manager.utils.GraphicsBuilder;
import share.manager.utils.ShareUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;

public class CompanyActivity extends Activity {
	
	private ShareManager app;
	private ProgressDialog progDiag;
	
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
		
		String tick = getIntent().getStringExtra("Tick");
		
		app = (ShareManager) getApplicationContext();
		
		progDiag = ProgressDialog.show(
                CompanyActivity.this, "",
                "Loading, please wait!", true);
		
		ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(10, 11, 2013, 11, 11, 2013, 'd', tick), threadConnectionHandler, this);
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
		
		new GraphicsBuilder(CompanyActivity.this, values, dates, null);
	}
}
