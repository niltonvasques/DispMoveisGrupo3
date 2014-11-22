package br.ufba.matc89.botaodopanico;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CadastroAlarmeEventoActivity extends ActionBarActivity {

	private Button btnOp1, btnOp2, btnOp3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_alarme_evento);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		btnOp1 = (Button) findViewById(R.id.BtnEventoOpcao1);
		btnOp2 = (Button) findViewById(R.id.BtnEventoOpcao2);
		btnOp3 = (Button) findViewById(R.id.BtnEventoOpcao3);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro_alarme_evento, menu);
		
		btnOp1.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CadastroAlarmeEventoActivity.this, TestarEvento.class);
				Bundle b = new Bundle();
				b.putInt("tipo", 1);

				intent.putExtras(b);
				startActivity(intent);				
			}
		});
		
		btnOp2.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CadastroAlarmeEventoActivity.this, TestarEvento.class);
				Bundle b = new Bundle();
				b.putInt("tipo", 2);

				intent.putExtras(b);
				startActivity(intent);				
			}
		});
		
		btnOp3.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CadastroAlarmeEventoActivity.this, TestarEvento.class);
				Bundle b = new Bundle();
				b.putInt("tipo", 3);

				intent.putExtras(b);
				startActivity(intent);				
			}
		});
		
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
