package share.manager.connection;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ConnectionThread extends Thread {

	private ConnectionRunnable runConnection;
	private Handler mHandler;
	/*private View view;
	private ShareManager app;*/
	
	public ConnectionThread(String link, Handler handler,
			/*, View view*/ Activity context) {
		runConnection = new ConnectionRunnable(link);
		mHandler = handler;
		//this.view = view;
		//this.app = ((ShareManager) context.getApplicationContext());
	}

	@Override
	public void run() {
		Looper.prepare();
		runConnection.run();
		threadMsg();
		
		Looper.loop();
	}

	public ArrayList<String> getResult() {
		return runConnection.getResultObject();
	}
	
	private void threadMsg() {
		Message msgObj = mHandler.obtainMessage();
		msgObj.obj = getResult();
		mHandler.sendMessage(msgObj);
	}
	
	
	/*private void handleView() {
		switch(currentFunction) {
			case GET_CLIENT_TICKETS:
				view.post(new ShowRunnable(app, context, view));
				break;
			case BUY_CLIENT_TICKETS:
				view.post(new BuyRunnable(app, view));				
				break;
			default:
				break;
		}
	}*/

}
