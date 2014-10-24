package br.ufba.matc89.botaodopanico;

import android.support.v7.app.ActionBarActivity;
import android.app.ExpandableListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class AjudaActivity extends ActionBarActivity {

	private ExpandableListView expListAjuda;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajuda);
		
		expListAjuda = (ExpandableListView) findViewById(R.id.expandable_list_ajuda);
		expListAjuda.setAdapter(new ExpandableListAdapterAjuda());
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
			
			return getString(R.string.lorem_ipsum);
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
