package share.manager.utils;

import java.util.ArrayList;
import java.util.Collections;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import share.manager.stock.R;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class GraphicsBuilder {
	
	  /** The main dataset that includes all the series that go into a chart. */
	  private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	  /** The main renderer that includes all the renderers customizing a chart. */
	  private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	  /** The most recently added series. */  
	  private XYSeries mCurrentSeries;
	  /** The chart view that displays the data. */  
	  private GraphicalView mChartView;
	  
	  public GraphicsBuilder(final Activity context, ArrayList<Float> stockValues, ArrayList<String> dates) {
	    // set some properties on the main renderer
	    mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setAxisTitleTextSize(25);
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(20);
	    mRenderer.setLegendTextSize(25);
	    mRenderer.setMargins(new int[] { 50, 50, 50, 50 });
	    mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setPointSize(0);
	    mRenderer.setPanEnabled(false, false);
	    mRenderer.setZoomRate(0.1f);
	    mRenderer.setZoomEnabled(false, false);
	    mRenderer.setYLabelsAlign(Align.RIGHT);
	    mRenderer.setXLabelsPadding(30);
		  
		mChartView = ChartFactory.getLineChartView(context, mDataset, mRenderer);

		LinearLayout layout = (LinearLayout) context.findViewById(R.id.chart);
		layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		XYSeries series = new XYSeries("Current Stock");
        mDataset.addSeries(series);
        mCurrentSeries = series;
        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // set some renderer properties
        renderer.setPointStyle(PointStyle.DIAMOND);
        renderer.setFillPoints(true);
        
        Collections.reverse(stockValues);
        Collections.reverse(dates);
        
        for(int i = 0; i < stockValues.size(); i++) {
	        double x = i;
	        double y = stockValues.get(i);
	        mCurrentSeries.add(x, y);
	        if(i % 5 == 0) {
	        	mRenderer.addXTextLabel(i, dates.get(i));
	        	mRenderer.setXLabels(0);
	        }
        }

	    mRenderer.setXTitle("From " + dates.get(0) + " to " + dates.get(dates.size()-1));
        
        Collections.sort(stockValues);
	    mRenderer.setYAxisMin(stockValues.get(0)-20.0f);
	    mRenderer.setYAxisMax(stockValues.get(stockValues.size()-1)+50.0f);
        
        mChartView.repaint();
	  }
}
