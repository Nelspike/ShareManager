package share.manager.utils;

import android.content.Context;
import share.manager.adapters.CompanyAdapter;

public class ShareUtils {

	public static String createChartLink(int initMonth, int initDay,
			int initYear, int finalMonth, int finalDay, int finalYear,
			char period, String company) {
		
		return "a="+(initMonth-1)+
				"&b="+initDay+
				"&c="+initYear+
				"&d="+(finalMonth-1)+
				"&e="+finalDay+
				"&f="+finalYear+
				"&g="+period+
				"&s="+company;
	}
	
	public static CompanyAdapter searchInDB(Context context, String query) {
		//TODO: Search in DB for company
		
		/*
		 * Retrieve all the names with that query, and respective images
		 * So that we can fill the adapter to give it to the list view with the result
		 * List of names => String[]
		 * List of images => Bitmap[]
		 */
		
		//return new CompanyAdapter(context, R.layout.company_box, names, images);
		return null;
	}
}
