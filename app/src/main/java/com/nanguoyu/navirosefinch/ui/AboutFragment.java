package com.nanguoyu.navirosefinch.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.nanguoyu.navirosefinch.MapsApplication;
import com.nanguoyu.navirosefinch.R;


public class AboutFragment extends PreferenceFragment {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_activity_pref);

		PackageManager packageManager = MapsApplication.getAppContext().getPackageManager();
		try {
			PackageInfo info = packageManager.getPackageInfo(getActivity().getPackageName(), 0);

			Preference version = findPreference("setting_pref_version");
			version.setSummary(info.versionName);

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

	}
}
