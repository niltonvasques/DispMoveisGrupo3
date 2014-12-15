package br.ufba.matc89.botaodopanico;

import android.support.v7.app.ActionBarActivity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class AjudaActivity extends ActionBarActivity {

	private ExpandableListView expListAjuda;
	private Button botaoProx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajuda);
		
		expListAjuda = (ExpandableListView) findViewById(R.id.expandable_list_ajuda);
		expListAjuda.setAdapter(new ExpandableListAdapterAjuda());
		botaoProx = (Button) findViewById(R.id.activity_ajuda_btn_finalizar);
		
		botaoProx.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AjudaActivity.this, TelaMain.class));	
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajuda, menu);
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
	
	private class ExpandableListAdapterAjuda extends BaseExpandableListAdapter{
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if (groupPosition == 0 )
				return getString(R.string.ajuda_modo_de_uso);
			if (groupPosition == 1 )
				return getString(R.string.ajuda_configuracao);
			return getString(R.string.ajuda_sobre);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView child = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			child.setText((String)getChild(groupPosition, childPosition));
			child.setTextColor(Color.GRAY);
			return child;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return getResources().getStringArray(R.array.ajuda_items)[groupPosition];
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return getResources().getStringArray(R.array.ajuda_items).length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView group = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			group.setText((String)getGroup(groupPosition));
			group.setGravity(Gravity.CENTER);
			return group;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
