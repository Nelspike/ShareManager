package share.manager.adapters;

import share.manager.stock.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SharesAdapter extends ArrayAdapter<String> {

	private String[] strings, regions, shares;
	private Context context;
	
	public SharesAdapter(Context context, int textViewResourceId, String[] objects, String[] regions,
			String[] shares) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.strings = objects;
		this.regions = regions;
		this.shares = shares; //x% (absolute value)
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View row = inflater.inflate(R.layout.company_box, parent, false);
		
		TextView label = (TextView) row.findViewById(R.id.shares_name_box);
		label.setText(strings[position]);
		
		TextView labelRegion = (TextView) row.findViewById(R.id.shares_region_box);
		labelRegion.setText(regions[position]);

		TextView labelChange = (TextView) row.findViewById(R.id.shares_num_box);
		labelChange.setText(shares[position]);
		
		Button minus = (Button) row.findViewById(R.id.minus_share);
		minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: Subtract one share
			}
			
		});
		
		Button plus = (Button) row.findViewById(R.id.plus_share);
		plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: Add one share
			}
			
		});
		
		return row;
	}
}
