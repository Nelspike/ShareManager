package share.manager.stock;

import android.app.Application;

public class ShareManager extends Application {

	public String yahooChart = "http://ichart.finance.yahoo.com/table.txt?";
	public String yahooQuote = "http://finance.yahoo.com/d/quotes?f=sl1d1t1c1&s=";
	
	private char periodicity;
	private int days;
	private String currency, graph;

	public char getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(char periodicity) {
		this.periodicity = periodicity;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}
	
}
