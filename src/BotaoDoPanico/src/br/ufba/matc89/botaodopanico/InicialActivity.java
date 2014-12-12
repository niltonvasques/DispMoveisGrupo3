/*
 *  Copyright (c) 2014, Facebook, Inc. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Facebook.
 *
 *  As with any software that integrates with the Facebook platform, your use of
 *  this software is subject to the Facebook Developer Principles and Policies
 *  [http://developers.facebook.com/policy/]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package br.ufba.matc89.botaodopanico;

import java.util.Date;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Shows the user profile. This simple activity can only function when there is a valid
 * user, so we must protect it with SampleDispatchActivity in AndroidManifest.xml.
 */
public class InicialActivity extends ActionBarActivity {
	private TextView emailTextView;
	private TextView nameTextView;
	
	private Button btnConfigurar;
	private Button btnLogAlerta;
	private Button btnBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile);
		emailTextView = (TextView) findViewById(R.id.profile_email);
		nameTextView = (TextView) findViewById(R.id.profile_name);
		btnConfigurar = (Button) findViewById(R.id.btConfigurar);
		btnLogAlerta = (Button) findViewById(R.id.btLogAlerta);
		btnBackground = (Button) findViewById(R.id.btBackground);
		
		btnConfigurar.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(InicialActivity.this, CadastroDestinatarioActivity.class));
				
			}
		});
		
		btnLogAlerta.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseObject logObj = new ParseObject("Log");
				logObj.put("user", ParseUser.getCurrentUser().getObjectId());
				logObj.put("tipo", "AlertaEnviada");
				logObj.put("mensagem", "Uma mensagem foi enviada para os contatos desse usu�rio");
				logObj.put("dataEnvio", new Date());
				
				logObj.saveInBackground();
				
				Toast.makeText(InicialActivity.this, "Teste Feito!!!", Toast.LENGTH_LONG).show();
				
				int tipo = ParseUser.getCurrentUser().getInt("eventoEscolha");
				
				System.out.println("Evento tipo "+tipo);
				
				Intent intent = new Intent(InicialActivity.this, TestarEvento.class);
				Bundle b = new Bundle();
				b.putInt("tipo", tipo);

				intent.putExtras(b);
				startActivity(intent);
			}
		});
		
		btnBackground.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {			
				System.out.println("btnBackground click");
				Intent it = new Intent(InicialActivity.this, EventoActivity.class);
				startActivity(it);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Set up the profile page based on the current user.
		ParseUser user = ParseUser.getCurrentUser();
		showProfile(user);
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
				// FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
				// logs out on older devices, we'll just exit.
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					Intent intent = new Intent(InicialActivity.this,
							FacebookDispatchActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
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

	/**
	 * Shows the profile of the given user.
	 *
	 * @param user
	 */
	private void showProfile(ParseUser user) {
		if (user != null) {
			emailTextView.setText(user.getEmail());
			String fullName = user.getString("name");
			if (fullName != null) {
				nameTextView.setText(fullName);
			}
		}
	}
}
