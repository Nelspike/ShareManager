package share.manager.adapters;

import share.manager.stock.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CompanyAdapter extends ArrayAdapter<String> {

	private String[] strings;
	private Context context;
	
	public CompanyAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.strings = objects;
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
		TextView label = (TextView) row.findViewById(R.id.company);
		label.setText(strings[position]);

		return row;
	}
}
