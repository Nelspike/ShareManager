package share.manager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogAdapter {
	
	/*public static void dialogYesNoShowing(String title, String text, final Context context, final FileHandler fHandler){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		alertDialogBuilder.setTitle(title);

		alertDialogBuilder
		.setMessage(text)
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				fHandler.deleteFiles();
				((Activity) context).recreate();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
	            System.exit(0);
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}*/
	
	public static void connectionIssues(final Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		alertDialogBuilder.setTitle("Connection Expired");

		alertDialogBuilder
		.setMessage("Can't reach the server at the moment. Restart the app, and try again.")
		.setCancelable(false)
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int id) {
		        System.exit(0);
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();		
	}

}
