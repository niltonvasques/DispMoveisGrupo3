package br.ufba.matc89.botaodopanico;

import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class InicialActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
		case R.id.action_settings:
			
			break;
			
		case R.id.action_logout:
			ParseUser user = ParseUser.getCurrentUser();
			if(user != null){
				ParseUser.logOut();
			}
			startActivity(new Intent(InicialActivity.this, LoginActivity.class));
	    	Toast.makeText(InicialActivity.this, "Logout efetuado com sucesso!!!", Toast.LENGTH_LONG).show();
	    	finish();
			break;

		default:
			break;
		}
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
