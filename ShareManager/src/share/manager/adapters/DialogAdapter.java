
package share.manager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogAdapter {

	public static void connectionIssues(final Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		alertDialogBuilder.setTitle("Connection Expired");

		alertDialogBuilder
				.setMessage(
						"Can't reach the server at the moment. Restart the app, and try again.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						System.exit(0);
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
