package share.manager.connection;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ConnectionRunnable implements Runnable {

	private String link;
	private ArrayList<String> resultString = new ArrayList<String>();
	private int readTimeout = 10000, connectionTimeout = 15000;

	public ConnectionRunnable(String link) {
		this.link = link;
	}

	@Override
	public void run() {
		connect();
	}

	private void connect() {
		HttpURLConnection con = null;
		String line = "";
		try {
			URL url = new URL(link);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(readTimeout);
			con.setConnectTimeout(connectionTimeout);
			con.setRequestMethod("GET");
			con.setDoInput(true);

			// Start the connection
			con.connect();

			// Read results from the query
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));

			while ((line = reader.readLine()) != null)
				resultString.add(line);
			
			reader.close();
		} catch (IOException e) {
			try {
				resultString.add(con.getResponseCode()+"");
			} catch (IOException e1) {
				//TODO: Can't figure response code
			}
		} finally {
			if (con != null)
				con.disconnect();
		}
	}

	public ArrayList<String> getResultObject() {
		return resultString;
	}

}
