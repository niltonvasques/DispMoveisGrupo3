package br.ufba.matc89.botaodopanico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	private String[] meuArray = { "", "", "" };
	private Button btnTelaInicial;
	private TextView txtResposta;
	private TextView txtCount;
	private List<ParseObject> contatos = new ArrayList<ParseObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento);
		tipo = ParseUser.getCurrentUser().getInt("eventoEscolha");

		btnTelaInicial = (Button) findViewById(R.id.btnTelaInicial);
		txtResposta = (TextView) findViewById(R.id.txtResposta);
		txtCount = (TextView) findViewById(R.id.txtCount);
		btnTelaInicial.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(EventoActivity.this, InicialActivity.class));
				finish();
			}
		});
		importaContatosParser();
	}

	CountDownTimer timer = new CountDownTimer(10000, 1000){
		@Override
		public void onFinish() {
			reiniciarDeteccao();
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			long seconds = millisUntilFinished / 1000;
			txtCount.setText(""+seconds);
		}
	};
	

	private void reiniciarDeteccao() {
		i = 0;
		Log.i(TAG, "Contador de detecção reiniciado");
		System.out.println(i);
		txtCount.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (i <= 2){
			if(i == 0){
				txtCount.setText("9");
				txtCount.setVisibility(View.VISIBLE);
				txtResposta.setText("");
				timer.start();			
			}
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
				txtResposta.setText("Alerta enviado!");
				Toast.makeText(EventoActivity.this, "Alerta enviado!", Toast.LENGTH_LONG).show();
				sendAlerta();
				timer.cancel();
				reiniciarDeteccao();
			}else{				
				//Alarme errado
				System.out.println("Evento incorreto!");
				Toast.makeText(EventoActivity.this, "Evento incorreto!", Toast.LENGTH_LONG).show();
			}
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
		
		ParseObject logObj = new ParseObject("Log");
		logObj.put("user", ParseUser.getCurrentUser().getObjectId());
		logObj.put("tipo", "AlertaEnviada");
		logObj.put("mensagem", "Uma mensagem foi enviada para os contatos desse usuário");
		logObj.put("dataEnvio", new Date());
		
		logObj.saveInBackground();
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
