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


public class LogsActivity extends ActionBarActivity {
	private static final String TAG = "[LogsActivity]";
	
	private ArrayList<ParseObject> logs = new ArrayList<ParseObject>();
	private ListView listViewLogs;
	private LogAdapter logsAdapter = new LogAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logs);
		
		listViewLogs = (ListView) findViewById(R.id.listViewContatos);
		listViewLogs.setAdapter(logsAdapter);
		
		importaLogsParser();
	}

	private void importaLogsParser() {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Log");
		query.whereContains("user", ParseUser.getCurrentUser().getObjectId());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				for (ParseObject parseObject : arg0) {
					logs.add(parseObject);
				}
				logsAdapter.notifyDataSetChanged();
			}
		});
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
	

	private class LogAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return logs.size();
		}

		@Override
		public Object getItem(int position) {
			return logs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ParseObject c = (ParseObject) getItem(position);
			TextView view = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			view.setText(c.getDate("dataEnvio") + " " +c.getString("mensagem"));
			return view;
		}
	}
}
