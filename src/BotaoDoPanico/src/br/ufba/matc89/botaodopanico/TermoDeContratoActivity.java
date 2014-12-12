package br.ufba.matc89.botaodopanico;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class TermoDeContratoActivity extends ActionBarActivity {
	private Button botaoProx;
	private CheckBox cbAceito;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_termo_de_contrato);
		botaoProx = (Button) findViewById(R.id.button1);
		cbAceito = (CheckBox) findViewById(R.id.checkBoxTermoServ);
		
		botaoProx.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cbAceito.isChecked())
					startActivity(new Intent(TermoDeContratoActivity.this, InicialActivity.class));
				else{
					System.out.println("Para utilizar nossos serviços, você deve concordar com os Termos de Serviço");
					Toast.makeText(TermoDeContratoActivity.this, "Para utilizar nossos serviços, você deve concordar com os Termos de Serviço", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modo_funcionamento, menu);
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
