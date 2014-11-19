package br.ufba.matc89.botaodopanico;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class CadastroDestinatarioActivity extends ActionBarActivity {
	private static final String TAG = "[CadastroDestinatarioActivity]";
	private static final int REQUEST_SELECT_CONTACT = 1001;
	
	private ArrayList<ParseObject> contatos = new ArrayList<ParseObject>();
	private ListView listViewContatos;
	private ContatoAdapter contatoAdapter = new ContatoAdapter();
	
	private ArrayList<ParseObject> contatosDeletados = new ArrayList<ParseObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_destinatario);
		
		listViewContatos = (ListView) findViewById(R.id.listViewContatos);
		listViewContatos.setAdapter(contatoAdapter);
		
		findViewById(R.id.btImportarAgenda).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doLaunchContactPicker();
			}
		});
		
		///DELETANDO
		listViewContatos.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				deletaContato(position);
				return false;
			}
		});
		
		findViewById(R.id.btSalvar).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sincronizaParserContatos();
				
			}
		});
		
		findViewById(R.id.btAdicionar).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edNome = (EditText) findViewById(R.id.edNome);
				EditText edNumero = (EditText) findViewById(R.id.edNumero);
				String nome = edNome.getText().toString();
				String numero = edNumero.getText().toString();
				
				if(nome.equals("") || numero.equals("")){
					Toast.makeText(CadastroDestinatarioActivity.this, "Por favor! Preencha todos os campos!", Toast.LENGTH_LONG).show();
				}else{
					ParseObject contato = buildParserObjectContato(nome, numero);
					contatos.add(contato);
					contatoAdapter.notifyDataSetChanged();
				}
				
			}
		});
		
		
		importaContatosParser();
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
				contatoAdapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult "+requestCode+" "+resultCode);
		// Get the URI and query the content provider for the phone number
        Uri contactUri = data.getData();
        String[] projection = new String[]{CommonDataKinds.Phone.NUMBER, CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY};
        Cursor cursor = getContentResolver().query(contactUri, projection,null, null, null);
        // If the cursor returned is valid, get the phone number
        if (cursor != null && cursor.moveToFirst()) {
        	String nome = "Sem Nome";
        	int nomeIndex = cursor.getColumnIndex(CommonDataKinds.Identity.DISPLAY_NAME_PRIMARY);
        	if(nomeIndex == -1){
        		nomeIndex = cursor.getColumnIndex(CommonDataKinds.Identity.DISPLAY_NAME);
        	}
        	if(nomeIndex == -1){
        		nomeIndex = cursor.getColumnIndex(CommonDataKinds.Identity.DISPLAY_NAME_ALTERNATIVE);
        	}
        	if(nomeIndex == -1){
        		nomeIndex = cursor.getColumnIndex(CommonDataKinds.StructuredName.GIVEN_NAME);
        	}
        	if(nomeIndex == -1){
        		nomeIndex = cursor.getColumnIndex(CommonDataKinds.StructuredName.MIDDLE_NAME);
        	}
        	if(nomeIndex == -1){
        		nomeIndex = cursor.getColumnIndex(CommonDataKinds.StructuredName.FAMILY_NAME);
        	}
        	if(nomeIndex == -1){
        		nomeIndex = cursor.getColumnIndex(CommonDataKinds.Nickname.NAME);
        	}
        	if(nomeIndex != -1){
        		nome = cursor.getString(nomeIndex);
        	}
            
            int numberIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
            String numero = cursor.getString(numberIndex);
            // Do something with the phone number
            
            Log.d(TAG, "contactUri "+data);
            Log.d(TAG, "Nome: "+nome+" Número: "+numero);
            ParseObject contato = buildParserObjectContato(nome, numero);
            contatos.add(contato);
            contatoAdapter.notifyDataSetChanged();
	    }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro_destinatario, menu);
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
	
	public void doLaunchContactPicker() {
		Intent intent = new Intent(Intent.ACTION_PICK);
	    intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
	    if (intent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(intent, REQUEST_SELECT_CONTACT);
	    }

	}
	
	private void sincronizaParserContatos() {
		//PRIMEIRO DELETA PARA DEPOIS ADICIONAR
		ParseObject.deleteAllInBackground(contatosDeletados, new DeleteCallback() {
			@Override
			public void done(ParseException arg0) {
				if(arg0 == null){
					ParseObject.saveAllInBackground(contatos, new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							try{
								if(arg0 == null){
									Toast.makeText(CadastroDestinatarioActivity.this, "Contatos salvos!!!", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(CadastroDestinatarioActivity.this, "Erro! Os contatos não foram salvos!!", Toast.LENGTH_LONG).show();
								}
							}catch(Exception e){
								
							}
							startActivity(new Intent(CadastroDestinatarioActivity.this, InicialActivity.class));
							finish();
						}
					});
				}else{
					Toast.makeText(CadastroDestinatarioActivity.this, "Erro! Os contatos não foram salvos!!", Toast.LENGTH_LONG).show();
					startActivity(new Intent(CadastroDestinatarioActivity.this, InicialActivity.class));
					finish();
				}
				
			}
		});
	}

	private void deletaContato(int position) {
		ParseObject deleted = contatos.remove(position);
		contatosDeletados.add(deleted);
		contatoAdapter.notifyDataSetChanged();
	}

	private ParseObject buildParserObjectContato(String nome, String numero) {
		ParseObject contato = new ParseObject("Contato");
		contato.put("nome", nome);
		contato.put("numero", numero);
		contato.put("user", ParseUser.getCurrentUser().getObjectId());
		return contato;
	}

	private class ContatoAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {

			return contatos.size();
		}

		@Override
		public Object getItem(int position) {

			return contatos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ParseObject c = (ParseObject) getItem(position);
			TextView view = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			view.setText(c.getString("nome") + " " +c.getString("numero"));
			return view;
		}
	}
}
