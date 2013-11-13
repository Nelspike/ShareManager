package share.manager.adapters;

import share.manager.stock.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyAdapter extends ArrayAdapter<String> {

	private String[] strings, regions, changes;
	private boolean[] status;
	private Context context;
	
	public CompanyAdapter(Context context, int textViewResourceId, String[] objects, String[] regions,
			boolean[] status, String[] changes) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.strings = objects;
		this.regions = regions;
		this.status = status;
		this.changes = changes; //x% (absolute value)
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
		
		TextView label = (TextView) row.findViewById(R.id.company_name_box);
		label.setText(strings[position]);
		
		TextView labelRegion = (TextView) row.findViewById(R.id.company_region_box);
		labelRegion.setText(regions[position]);
		
		ImageView arrow = (ImageView) row.findViewById(R.id.company_arrow_box);
		
		if(status[position]) {
			//TODO: Set Positive arrow
		}
		else {
			//TODO: Set Negative Arrow
		}

		TextView labelChange = (TextView) row.findViewById(R.id.company_change_box);
		labelChange.setText(changes[position]);
		
		return row;
	}
}
