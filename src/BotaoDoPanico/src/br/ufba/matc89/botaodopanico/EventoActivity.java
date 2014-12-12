package br.ufba.matc89.botaodopanico;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EventoActivity extends ActionBarActivity {
	private static final String TAG = "[EventoActivity]";
	private int tipo, i = 0;
	private String[] meuArray;
	private Button btnTelaInicial;
	private TextView txtResposta;
	private List<ParseObject> contatos = new ArrayList<ParseObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento);
		meuArray = new String[3];
		tipo = ParseUser.getCurrentUser().getInt("eventoEscolha");

		btnTelaInicial = (Button) findViewById(R.id.btnTelaInicial);
		txtResposta = (TextView) findViewById(R.id.txtResposta);

		btnTelaInicial.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(EventoActivity.this, InicialActivity.class));
				finish();
			}
		});
		importaContatosParser();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (i <= 2){
			if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				meuArray[i] = "Baixo";
			//Toast.makeText(TestarEvento.this, "Volume de Diminuir!", Toast.LENGTH_LONG).show();

			if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				meuArray[i] = "Cima";
			//Toast.makeText(TestarEvento.this, "Volume de Aumentar!", Toast.LENGTH_LONG).show();

			i++;
			System.out.println(i);
		}
		if (i == 3){
			i++;
			if (validarEvento()){
				//Alarme correto
				System.out.println("Alarme correto!");
				txtResposta.setText("Alarme correto!");
				Toast.makeText(EventoActivity.this, "Alarme correto!", Toast.LENGTH_LONG).show();
				sendAlerta();
			}else{				
				//Alarme errado
				System.out.println("Alarme errado!");
				Toast.makeText(EventoActivity.this, "Alarme errado!", Toast.LENGTH_LONG).show();
			}
			i = 0;
		}
		return true;
		//return super.onKeyDown(keyCode, event);
	}

	public boolean validarEvento(){
		if ((tipo == 1) && (((meuArray[0].equals("Cima")) && (meuArray[1].equals("Baixo"))) && (meuArray[2].equals("Cima"))))
			return true;
		if ((tipo == 2) && (((meuArray[0].equals("Baixo")) && (meuArray[1].equals("Cima"))) && (meuArray[2].equals("Baixo"))))
			return true;
		if ((tipo == 3) && (((meuArray[0].equals("Baixo")) && (meuArray[1].equals("Baixo"))) && (meuArray[2].equals("Cima"))))
			return true;
		return false;

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro_alarme_codigo, menu);
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
	
	private void sendAlerta(){
		String msg = "Estou em perigo!";
//		String nome = ParseUser.getCurrentUser().getUsername();
		for (ParseObject contato : contatos) {
			String number = contato.getString("numero");
			sendSMS(number, msg);
		}
	}

	private void sendSMS(String phoneNumber, String message)
	{	
		Log.i(TAG,"Enviando alerta para "+phoneNumber);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
	
	private void importaContatosParser() {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Contato");
		query.whereContains("user", ParseUser.getCurrentUser().getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				for (ParseObject parseObject : arg0) {
					contatos.add(parseObject);
				}
			}
		});
	}
}
