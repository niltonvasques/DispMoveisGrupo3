package br.ufba.matc89.botaodopanico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

public class TestarEvento extends ActionBarActivity {
	private int tipo, i;
	private String[] meuArray;
	private Button btnCancela, btnSalvar, btnTelaInicial;
	private TextView txtResposta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testar_evento);
		meuArray = new String[3];
		i = 0;
		Bundle b = getIntent().getExtras();
		tipo = b.getInt("tipo");
		
		btnCancela = (Button) findViewById(R.id.btnCancela);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);
		btnTelaInicial = (Button) findViewById(R.id.btnTelaInicial);
		txtResposta = (TextView) findViewById(R.id.txtResposta);
		
		btnCancela.setEnabled(false);
		btnSalvar.setEnabled(false);
		
		btnCancela.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(TestarEvento.this, CadastroAlarmeEventoActivity.class));		
				finish();
			}
		});
		
		btnSalvar.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				//ParseUser user = ParseUser.getCurrentUser();
				ParseUser.getCurrentUser().put("eventoEscolha", tipo);
				 
				try {
					ParseUser.getCurrentUser().save();
					Toast.makeText(TestarEvento.this, "Alarme salvo com sucesso!", Toast.LENGTH_LONG).show();
					startActivity(new Intent(TestarEvento.this, InicialActivity.class));
					finish();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Toast.makeText(TestarEvento.this, "Erro ao salvar alarme!", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}
		});
		btnTelaInicial.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(TestarEvento.this, InicialActivity.class));
				finish();
			}
		});
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
			btnCancela.setEnabled(true);
			
			i++;
			if (validarEvento()){
				//Alarme correto
				System.out.println("Alarme correto!");
				txtResposta.setText("Alarme correto!");
				Toast.makeText(TestarEvento.this, "Alarme correto!", Toast.LENGTH_LONG).show();
				btnSalvar.setEnabled(true);
			}else{
				//Alarme errado
				System.out.println("Alarme errado!");
				Toast.makeText(TestarEvento.this, "Alarme errado!", Toast.LENGTH_LONG).show();
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
}
