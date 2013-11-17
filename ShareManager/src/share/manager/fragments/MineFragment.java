package share.manager.fragments;

import share.manager.stock.R;
import share.manager.stock.ShareManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MineFragment extends Fragment {

	private View rootView;
	private ShareManager app;
	
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
		app = (ShareManager) getActivity().getApplication();
		return rootView;
	}
	
	
	public void refresh() {
		//Do refresh stuff
	}
	
}
