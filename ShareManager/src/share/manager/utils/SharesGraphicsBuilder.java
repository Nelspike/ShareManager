package share.manager.utils;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import share.manager.stock.R;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class SharesGraphicsBuilder {

	private static int[] COLORS = new int[] { Color.argb(100,48,55,255), Color.argb(100,36,99,4), Color.argb(100,250,133,50),
												Color.argb(100,255,51,51), Color.argb(100,252,189,0), Color.argb(100,108,1,122)};
	private CategorySeries mSeries = new CategorySeries("");
	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView mChartView;	
	private Activity context;
	private View view;

	public SharesGraphicsBuilder(final Activity context, ArrayList<Integer> stockValues, ArrayList<String> names, View view) {
		this.context = context;
		this.view = view;
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(0, 50, 50, 50));
		mRenderer.setChartTitleTextSize(25);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setLegendTextSize(25);
		mRenderer.setStartAngle(90);
		mRenderer.setPanEnabled(false);

		for (int i = 0; i < stockValues.size(); i++) {
			mSeries.add(names.get(i) + " " + stockValues.get(i) + "%", stockValues.get(i));
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);
		}
		
		LinearLayout layout = (LinearLayout) this.view.findViewById(R.id.chart_mine);
		mChartView = ChartFactory.getPieChartView(this.context, mSeries, mRenderer);
		layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mChartView.repaint();
	}
	
	public void repaintGraph(ArrayList<Integer> stockValues, ArrayList<String> names) {
		mRenderer.removeAllRenderers();
		mSeries.clear();
		for (int i = 0; i < stockValues.size(); i++) {
			mSeries.add(names.get(i) + " " + stockValues.get(i) + "%", stockValues.get(i));
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);
		}
		LinearLayout layout = (LinearLayout) this.view.findViewById(R.id.chart_mine);
		layout.removeAllViews();
		mChartView = ChartFactory.getPieChartView(this.context, mSeries, mRenderer); 
		layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mChartView.repaint();
	}
}
