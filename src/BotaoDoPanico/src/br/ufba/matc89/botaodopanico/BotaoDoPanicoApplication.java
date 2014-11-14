package br.ufba.matc89.botaodopanico;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class BotaoDoPanicoApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "pU5pZWL3KWOHTWWk2E0V1Ej54gvABbWHDdk31SQy", "i3FvaOGnXq3IvWJSwkxpdaBT7FyWGL3cvRfK0ih8");
		
	}

}
