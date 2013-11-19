package share.manager.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import share.manager.connection.ConnectionThread;
import share.manager.stock.R;
import share.manager.stock.ShareManager;
import share.manager.utils.CompanyGraphicsBuilder;
import share.manager.utils.FileHandler;
import share.manager.utils.RESTFunction;
import share.manager.utils.ShareUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class PortfolioFragment extends Fragment {

	private View rootView;
	private RESTFunction currentFunction;
	private ShareManager app;
	private ProgressDialog progDiag;
	private float change;
	private boolean status, resuming = false;
	private String name, region;
	private CompanyGraphicsBuilder graph;
	private Activity mActivity;
	private boolean firstTime = true;

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(currentFunction) {
				case GET_COMPANY_STOCK_PORTFOLIO:
					if(msg.what == currentFunction.toInt())
						compareChanges((ArrayList<String>) msg.obj);
					break;
				case GET_COMPANY_RANGE_STOCK_PORTFOLIO:
					if(msg.what == currentFunction.toInt()) {
						createGraph((ArrayList<String>) msg.obj);
						changeInfoTitle();
						dismissProgressDialog();
					}
					break;
				default:
					break;
			}		
		}
	};
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		rootView = inflater.inflate(R.layout.fragment_portfolio, container, false);
		app = (ShareManager) mActivity.getApplication();
		if(firstTime) {
			firstTime = false;
			startQuotas();
		}
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(resuming) {
			refresh();
		}
	}

	public void onStop() {
		super.onStop();
		this.resuming = app.isAccessedSettings() ? true : false;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
	
	public void refresh() {
		if(!firstTime) startQuotas();
	}
	
	public void startQuotas() {
		showProgressDialog("Obtaining information..");
		currentFunction = RESTFunction.GET_COMPANY_STOCK_PORTFOLIO;
		String link = app.yahooQuote;
		
		ArrayList<String> info = FileHandler.readFile();
		
		if(info.size() > 0) {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_portfolio);
			if(frame.findViewById(0xfefefefe) != null) {
				frame.removeView(frame.findViewById(0xfefefefe));
			}
			
			for(String s : info)
				link += s.split("\\|")[1] + "+";
			
			link = link.substring(0, link.length()-1);
			
			ConnectionThread dataThread = new ConnectionThread(
					link, threadConnectionHandler, mActivity, currentFunction);
			dataThread.start();
		}
		else {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_portfolio);
			if(frame.findViewById(0xfefefefe) != null) {
				TextView text = (TextView) frame.findViewById(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
			}
			else {
				TextView text = new TextView(mActivity);
				text.setId(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
				frame.addView(text, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			dismissProgressDialog();
		}
	}
	
	private void compareChanges(ArrayList<String> received) {
		change = -1000.0f;
		String tick = "";
		
		for(int i = 0; i < received.size(); i++) {
			String[] split = received.get(i).split(",");
			String name = split[0];
			String changeText = split[4];
			float ch = Math.abs(Float.parseFloat(changeText));
			if(ch > this.change) {
				this.change = ch;
				tick = name;
				this.status = Float.parseFloat(changeText) > 0 ? true : false;
			}
		}

		tick = tick.replace("\"", "");
		String info = FileHandler.getInfoFromTick(tick);
		String[] allInfo = info.split("\\|");
		this.name = allInfo[0];
		this.region = allInfo[2];
		currentFunction = RESTFunction.GET_COMPANY_RANGE_STOCK_PORTFOLIO;
		
		int daysToBacktrack = app.getDays() * -1;
		
		Calendar backtrack = Calendar.getInstance();
		backtrack.add(Calendar.DATE, daysToBacktrack);
		Calendar current = Calendar.getInstance();
				
		ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(backtrack.get(Calendar.MONTH), backtrack.get(Calendar.DAY_OF_MONTH), backtrack.get(Calendar.YEAR),
						current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.YEAR), app.getPeriodicity(), tick), 
						threadConnectionHandler, mActivity, currentFunction);
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
		
		if(!resuming) graph = new CompanyGraphicsBuilder(mActivity, values, dates, rootView);
		else graph.repaintGraph(values, dates);
	}
	
	private void changeInfoTitle() {
		TextView label = (TextView) rootView.findViewById(R.id.company_name_portfolio);
		label.setText(this.name);
		
		TextView labelRegion = (TextView) rootView.findViewById(R.id.company_region_portfolio);
		labelRegion.setText(this.region);
		
		ImageView arrow = (ImageView) rootView.findViewById(R.id.company_arrow_portfolio);
		
		if(this.change != 0.0f) {
			if(status) arrow.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.upper_arrow));
			else arrow.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.down_arrow));
		}

		TextView labelChange = (TextView) rootView.findViewById(R.id.company_change_portfolio);
		labelChange.setText(this.change+"%");
		
		if(resuming) resuming = !resuming;
	}
	
	public void showProgressDialog(CharSequence message) {
		progDiag = new ProgressDialog(mActivity);
	        if (progDiag == null)
	        	progDiag.setIndeterminate(true);

	        progDiag.setMessage(message);
	        progDiag.show();
	    }

    public void dismissProgressDialog() {
        if (progDiag != null)
        	progDiag.dismiss();
    }
}
