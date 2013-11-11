package share.manager.utils;

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
}
