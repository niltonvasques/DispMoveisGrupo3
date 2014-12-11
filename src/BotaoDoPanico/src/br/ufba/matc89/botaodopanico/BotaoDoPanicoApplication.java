package br.ufba.matc89.botaodopanico;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class BotaoDoPanicoApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "pU5pZWL3KWOHTWWk2E0V1Ej54gvABbWHDdk31SQy", "i3FvaOGnXq3IvWJSwkxpdaBT7FyWGL3cvRfK0ih8");
		ParseFacebookUtils.initialize("348131288692341");
		
		System.out.println("Subscribing...");

		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});
	}

}
