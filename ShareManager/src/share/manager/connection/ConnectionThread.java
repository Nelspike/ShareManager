
package share.manager.connection;

import java.util.ArrayList;

import share.manager.utils.RESTFunction;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ConnectionThread extends Thread {

	private ConnectionRunnable runConnection;
	private Handler mHandler;
	private RESTFunction current;

	public ConnectionThread(String link, Handler handler, Activity context,
			RESTFunction current) {
		this.runConnection = new ConnectionRunnable(link);
		this.mHandler = handler;
		this.current = current;
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
		msgObj.what = current.toInt();
		mHandler.sendMessage(msgObj);
	}

}
