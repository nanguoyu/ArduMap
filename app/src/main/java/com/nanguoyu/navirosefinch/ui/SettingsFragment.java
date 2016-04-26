package com.nanguoyu.navirosefinch.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.nanguoyu.navirosefinch.R;


public class SettingsFragment extends PreferenceFragment {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_activity_pref);
	}
}
