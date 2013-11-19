package share.manager.fragments;

import java.util.ArrayList;

import share.manager.adapters.SharesAdapter;
import share.manager.stock.R;
import share.manager.utils.FileHandler;
import share.manager.utils.SharesGraphicsBuilder;
import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.TextView;


public class MineFragment extends Fragment {

	private View rootView;
	private SharesGraphicsBuilder graph;
	
	@SuppressLint("HandlerLeak")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
				
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		rootView = inflater.inflate(R.layout.fragment_mine, container, false);
		if(FileHandler.readFile().size() > 0) {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_mine);
			if(frame.findViewById(0xfefefefe) != null) {
				frame.removeView(frame.findViewById(0xfefefefe));
			}
			
			makeGraph();
			startFiles();
		}
		else {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_mine);
			if(frame.findViewById(0xfefefefe) != null) {
				TextView text = (TextView) frame.findViewById(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
			}
			else {
				TextView text = new TextView(getActivity());
				text.setId(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
				frame.addView(text, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
		}
		return rootView;
	}
	
	public void refresh() {
		if(FileHandler.readFile().size() > 0) {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_mine);
			if(frame.findViewById(0xfefefefe) != null) {
				frame.removeView(frame.findViewById(0xfefefefe));
			}
			makeGraph();
			startFiles();
		}
		else {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_mine);
			if(frame.findViewById(0xfefefefe) != null) {
				TextView text = (TextView) frame.findViewById(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
			}
			else {
				TextView text = new TextView(getActivity());
				text.setId(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
				frame.addView(text, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
		}
	}
	
	private void makeGraph() {
		ArrayList<Integer> values = FileHandler.getSharePercentages();
		ArrayList<String> names = FileHandler.getNamesString();
		graph = new SharesGraphicsBuilder(getActivity(), values, names, rootView);
	}
	
	private void startFiles() {
		ListView listMyShares = (ListView) rootView.findViewById(R.id.list_my_shares);
		final String[] names = FileHandler.getNames();
		String[] regions = FileHandler.getRegions(), shares = FileHandler.getShares();
		final String[] ticks = FileHandler.getTicks();

		listMyShares.setAdapter(new SharesAdapter(getActivity(), R.layout.shares_box, names, regions, shares, ticks, graph));
	}
}
