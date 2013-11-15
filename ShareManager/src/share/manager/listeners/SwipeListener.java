package share.manager.listeners;

import share.manager.adapters.MainPagerAdapter;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

public class SwipeListener extends ViewPager.SimpleOnPageChangeListener {
	
	private ViewPager mViewPager;
	private Context context;
	//private ShareManager app;
	
	public SwipeListener(ViewPager mViewPager, Context context) {
		this.mViewPager = mViewPager;
		this.context = context;
	}
	
	@Override
	public void onPageSelected(int position) {
		//this.app = (ShareManager) context.getApplicationContext();
		MainPagerAdapter adapter = (MainPagerAdapter) mViewPager.getAdapter();

		/*if(position == 0) {
				((PortfolioFragment) adapter.instantiateItem(mViewPager, position)).refresh();
		}
		else if(position == 0) {
			((BuyTicketsFragment) adapter.instantiateItem(mViewPager, position)).refresh();
		}
		else {
			
		}*/
	
		((Activity) context).getActionBar().setSelectedNavigationItem(position);

	}
}
