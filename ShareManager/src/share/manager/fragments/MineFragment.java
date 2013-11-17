package share.manager.fragments;

import java.util.ArrayList;

import share.manager.adapters.SharesAdapter;
import share.manager.stock.R;
import share.manager.utils.FileHandler;
import share.manager.utils.SharesGraphicsBuilder;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class MineFragment extends Fragment {

	private View rootView;
	private SharesGraphicsBuilder graph;
	//private ShareManager app;
	
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
		//app = (ShareManager) getActivity().getApplication();
		makeGraph();
		startFiles();
		return rootView;
	}
	
	
	public void refresh() {
		//Do refresh stuff
	}
	
	private void makeGraph() {
		ArrayList<Integer> values = FileHandler.getSharePercentages();
		ArrayList<String> names = FileHandler.getNamesString();
		graph = new SharesGraphicsBuilder(getActivity(), values, names, rootView);
	}
	
	private void startFiles() {
		ListView listMyShares = (ListView) rootView.findViewById(R.id.list_my_shares);
		String[] names = FileHandler.getNames(), regions = FileHandler.getRegions(), shares = FileHandler.getShares(), ticks = FileHandler.getTicks();
		
		listMyShares.setAdapter(new SharesAdapter(getActivity(), R.layout.shares_box, names, regions, shares, ticks, graph));
	}
}
