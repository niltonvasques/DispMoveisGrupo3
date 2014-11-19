package br.ufba.matc89.botaodopanico;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.*;

public class CadastroActivity extends ActionBarActivity {

	private EditText editTxtUsuario;
	private EditText editTxtpassword;
	private EditText editTxtConfirmaPassword;
	private EditText editTxtemail;
	private Button btnCadastrar;
	private ArrayList<ParseObject> log = new ArrayList<ParseObject>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);
		
		loadComponentsFromXML();
		
		btnCadastrar.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String usuario 		= editTxtUsuario.getText().toString();
				String pass 		= editTxtpassword.getText().toString();
				String confirmPass 	= editTxtConfirmaPassword.getText().toString();
				String email 		= editTxtemail.getText().toString();
				
				if( usuario.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || email.isEmpty() ){
					Toast.makeText(CadastroActivity.this, "Por favor! Preencha todos os campos!", Toast.LENGTH_LONG).show();
					return;
				}
				
				if( !pass.equals(confirmPass) ){
					Toast.makeText(CadastroActivity.this, "Senhas não conferem!", Toast.LENGTH_LONG).show();
					return;
				}
				
				if( !email.contains("@") ){
					Toast.makeText(CadastroActivity.this, "Email inválido!", Toast.LENGTH_LONG).show();
					return;
				}

				
				ParseUser user = new ParseUser();
				user.setUsername(usuario);
				user.setPassword(pass);
				user.setEmail(email);
				
				// other fields can be set just like with ParseObject
				//user.put("phone", "650-555-0000");
				  
				user.signUpInBackground(new SignUpCallback() {
				  public void done(ParseException e) {
				    if (e == null) {
				      // Hooray! Let them use the app now.
				    	startActivity(new Intent(CadastroActivity.this, InicialActivity.class));
				    	Toast.makeText(CadastroActivity.this, "Cadastro efetuado com sucesso!!!", Toast.LENGTH_LONG).show();
				    } else {
				      Toast.makeText(CadastroActivity.this, "Desculpe! Não foi possível criar sua conta!", Toast.LENGTH_LONG).show();
				      e.printStackTrace();
				    }
				  }
				});
				
				createLogParse(user);
				
			}
			
		});
	}
	
	private void createLogParse(ParseUser user){
		
		ParseObject logObj = new ParseObject("Log");
		logObj.put("logUser", "");
		logObj.put("idUser", user.getObjectId());
		
		log.add(logObj);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Log");
		query.whereContains("idUser", user.getObjectId());
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				for (ParseObject parseObject : arg0) {
					log.add(parseObject);
				}
			}
		});
	
	}
	
	private void loadComponentsFromXML() {
		editTxtUsuario = (EditText) findViewById(R.id.edNomeUsuario);
		editTxtpassword = (EditText) findViewById(R.id.edSenha);
		editTxtConfirmaPassword = (EditText) findViewById(R.id.edConfirmarSenha);
		editTxtemail = (EditText) findViewById(R.id.edEmail);
		btnCadastrar = (Button) findViewById(R.id.btSalvarCadastro);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
