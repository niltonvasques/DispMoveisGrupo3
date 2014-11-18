package br.ufba.matc89.botaodopanico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private EditText editTxtUsuario;
	private EditText editTxtpassword;
	private Button btnLogin;
	private Button btnCadastrar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loadComponentsFromXML();
		
		ParseUser user = ParseUser.getCurrentUser();
		
		ParseFacebookUtils.initialize("348131288692341");
		
		if(user != null){
	    	startActivity(new Intent(LoginActivity.this, InicialActivity.class));
	    	Toast.makeText(LoginActivity.this, "Bem vindo "+user.getUsername(), Toast.LENGTH_LONG).show();
	    	finish();
		}
		
		btnLogin.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseUser.enableAutomaticUser();
				ParseUser.logInInBackground(editTxtUsuario.getText().toString(), editTxtpassword.getText().toString(), new LogInCallback() {
					  public void done(ParseUser user, ParseException e) {
					    if (user != null) {
					    	startActivity(new Intent(LoginActivity.this, InicialActivity.class));
					    	Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!!!", Toast.LENGTH_LONG).show();
					    	finish();
					    } else {
					      // Signup failed. Look at the ParseException to see what happened.
					    	Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos!!!", Toast.LENGTH_LONG).show();
					    	e.printStackTrace();
					    }
					  }
					});
			}
		});
		
		btnCadastrar.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
			}
		});
	}
	
	private void loadComponentsFromXML() {
		editTxtUsuario 	= (EditText) findViewById(R.id.edNomeUsuario);
		editTxtpassword = (EditText) findViewById(R.id.edSenha);
		btnLogin 		= (Button) findViewById(R.id.btLogin);
		btnCadastrar 	= (Button) findViewById(R.id.btCadastrar);
	}
}
