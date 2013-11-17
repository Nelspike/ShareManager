package share.manager.fragments;

import java.util.ArrayList;

import share.manager.connection.ConnectionThread;
import share.manager.stock.R;
import share.manager.stock.ShareManager;
import share.manager.utils.GraphicsBuilder;
import share.manager.utils.RESTFunction;
import share.manager.utils.ShareUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PortfolioFragment extends Fragment {

	private View rootView;
	private RESTFunction currentFunction;
	private ShareManager app;
	private ProgressDialog progDiag;
	private float change;
	private String name;

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(currentFunction) {
				case GET_COMPANY_STOCK:
					compareChanges((ArrayList<String>) msg.obj);
					break;
				case GET_COMPANY_RANGE_STOCK:
					createGraph((ArrayList<String>) msg.obj);
					changeInfoTitle();
					progDiag.dismiss();	
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
		app = (ShareManager) getActivity().getApplication();
		
		progDiag = ProgressDialog.show(getActivity(), "",
                "Loading, please wait!", true);
		
		startQuotas();
		
		return rootView;
	}
	
	
	public void refresh() {
		//Do refresh stuff
	}
	
	public void startQuotas() {
		currentFunction = RESTFunction.GET_COMPANY_STOCK;
		String link = app.yahooQuote;
		
		for(String s : app.names)
			link += s + "+";
		
		link = link.substring(0, link.length()-1);
		
		ConnectionThread dataThread = new ConnectionThread(
				link, threadConnectionHandler, getActivity());
		dataThread.start();
	}
	
	private void compareChanges(ArrayList<String> received) {
		change = -1000.0f;
		name = "";
		
		for(int i = 1; i < received.size(); i++) {
			String[] split = received.get(i).split(",");
			String name = split[0];
			String changeText = split[4];
			float ch = Math.abs(Float.parseFloat(changeText));
			if(ch > this.change) {
				this.change = ch;
				this.name = name;
			}
		}

		this.name = this.name.replace("\"", "");
		
		currentFunction = RESTFunction.GET_COMPANY_RANGE_STOCK;
		ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(10, 10, 2013, 11, 10, 2013, 'd', this.name),
				threadConnectionHandler, getActivity());
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
		
		new GraphicsBuilder(getActivity(), values, dates, rootView);
	}
	
	private void changeInfoTitle() {
		TextView label = (TextView) rootView.findViewById(R.id.company_name_portfolio);
		label.setText(this.name);
		
		TextView labelRegion = (TextView) rootView.findViewById(R.id.company_region_portfolio);
		labelRegion.setText("NASDAQ");
		
		/*ImageView arrow = (ImageView) rootView.findViewById(R.id.company_arrow_box);
		
		if(status[position]) {
			//TODO: Set Positive arrow
		}
		else {
			//TODO: Set Negative Arrow
		}*/
		
		System.out.println("Change - " + this.change);

		TextView labelChange = (TextView) rootView.findViewById(R.id.company_change_portfolio);
		labelChange.setText(this.change+"%");
	}
}
