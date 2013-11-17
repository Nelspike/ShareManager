package share.manager.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShareUtils {

	public static String createChartLink(int initMonth, int initDay,
			int initYear, int finalMonth, int finalDay, int finalYear,
			char period, String company) {
		
		return "a="+initMonth+
				"&b="+initDay+
				"&c="+initYear+
				"&d="+finalMonth+
				"&e="+finalDay+
				"&f="+finalYear+
				"&g="+period+
				"&s="+company;
	}
	
	public static String buildSearchLink(String query) {
		return "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query="+query+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
	}
	
	public static JSONArray remove(final int idx, final JSONArray from) {
	    final List<JSONObject> objs = asList(from);
	    objs.remove(idx);

	    final JSONArray ja = new JSONArray();
	    for (final JSONObject obj : objs) {
	        ja.put(obj);
	    }

	    return ja;
	}

	public static List<JSONObject> asList(final JSONArray ja) {
	    final int len = ja.length();
	    final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
	    for (int i = 0; i < len; i++) {
	        final JSONObject obj = ja.optJSONObject(i);
	        if (obj != null) {
	            result.add(obj);
	        }
	    }
	    return result;
	}
}
