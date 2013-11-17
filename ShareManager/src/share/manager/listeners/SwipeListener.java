package share.manager.listeners;

import share.manager.adapters.MainPagerAdapter;
import share.manager.fragments.MineFragment;
import share.manager.fragments.PortfolioFragment;
import share.manager.fragments.SharesFragment;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

public class SwipeListener extends ViewPager.SimpleOnPageChangeListener {
	
	private ViewPager mViewPager;
	private Context context;
	
	public SwipeListener(ViewPager mViewPager, Context context) {
		this.mViewPager = mViewPager;
		this.context = context;
	}
	
	@Override
	public void onPageSelected(int position) {
		MainPagerAdapter adapter = (MainPagerAdapter) mViewPager.getAdapter();

		if(position == 0) {
			((MineFragment) adapter.instantiateItem(mViewPager, position)).refresh();
		}
		else if(position == 1) {
			((SharesFragment) adapter.instantiateItem(mViewPager, position)).refresh();
		}
		else {
			((PortfolioFragment) adapter.instantiateItem(mViewPager, position)).refresh();
		}
	
		((Activity) context).getActionBar().setSelectedNavigationItem(position);

	}

}
