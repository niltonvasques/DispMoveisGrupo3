package br.ufba.matc89.botaodopanico;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

public class PushNotificationReceiver extends ParsePushBroadcastReceiver{
	
	private static final String TAG = "[PushNotificationReceiver]";
	
	@Override
	protected void onPushOpen(Context context, Intent intent) {
		Log.i(TAG, "onPushOpen");
		ParseAnalytics.trackAppOpened(intent);

        String uriString = null;
        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            uriString = pushData.optString("uri");
        } catch (JSONException e) {
            Log.v("com.parse.ParsePushReceiver", "Unexpected JSONException when receiving push data: ", e);
        }
        Class<? extends Activity> cls = getActivity(context, intent);
        Intent activityIntent;
        if (uriString != null && !uriString.isEmpty()) {
            activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        } else {
            activityIntent = new Intent(context, FacebookDispatchActivity.class);
        }
        activityIntent.putExtras(intent.getExtras());
        if (Build.VERSION.SDK_INT >= 16) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(cls);
            stackBuilder.addNextIntent(activityIntent);
            stackBuilder.startActivities();
        } else {
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(activityIntent);
        }
	}
	
	@Override
	protected void onPushReceive(Context arg0, Intent arg1) {
		super.onPushReceive(arg0, arg1);
		Log.i(TAG, "onPushReceive");
	}
	
	@Override
	protected void onPushDismiss(Context context, Intent intent) {
		super.onPushDismiss(context, intent);
		Log.i(TAG, "onPushDismiss");
	}

}
