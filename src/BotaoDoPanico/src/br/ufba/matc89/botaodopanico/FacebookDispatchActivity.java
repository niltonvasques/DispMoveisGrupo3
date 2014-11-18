package br.ufba.matc89.botaodopanico;

import com.parse.ui.ParseLoginDispatchActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FacebookDispatchActivity extends ParseLoginDispatchActivity {

	@Override
	protected Class<?> getTargetClass() {
		return SampleProfileActivity.class;
	}
}
