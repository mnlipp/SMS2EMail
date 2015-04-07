package de.mnl.sms2email.authenticator;

import java.io.Serializable;

import de.mnl.sms2email.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class AddAccountAdvanced extends Activity {

	public static final String DATA = AddAccountAdvanced.class + ".data"; 

	@SuppressWarnings("serial")
	public static class Data implements Serializable {
		public boolean enforceSecureConnections = true;
		public boolean enforceTrustedCertificates = true;
		public Integer smtpPort = null;
		public Integer smtpsPort = null;
	}

	private Data data;
	private PreferenceFragment fragment;
	private CheckBoxPreference secureConnectionsCheckBox;
	private CheckBoxPreference trustedCertificatesCheckBox;
	private EditTextPreference smtpPortText;
	private EditTextPreference smtpsPortText;

	private class AccountSettingsFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.account_preferences);

	        // Setup form
		    secureConnectionsCheckBox = (CheckBoxPreference)
		    		fragment.findPreference("enforce_secure_connections");
		    trustedCertificatesCheckBox = (CheckBoxPreference)
		    		fragment.findPreference("enforce_trusted_certificates");
			smtpPortText = (EditTextPreference)
					fragment.findPreference("smtp_port");
			smtpsPortText = (EditTextPreference)
					fragment.findPreference("smtps_port");

		    secureConnectionsCheckBox.setChecked(data.enforceSecureConnections);
			trustedCertificatesCheckBox.setChecked
				(data.enforceTrustedCertificates);
			smtpPortText.setOnPreferenceChangeListener
				(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange
						(Preference preference, Object newValue) {
					data.smtpPort = parseNumber(newValue);
					updateSummaries();
					return true;
				}
			});
			smtpsPortText.setOnPreferenceChangeListener
				(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(
						Preference preference, Object newValue) {
					data.smtpsPort = parseNumber(newValue);
					updateSummaries();
					return true;
				}
			});
			
			updateSummaries();
	    }

	    private Integer parseNumber(Object value) {
			String number = (String)value;
			if (number.length() == 0) {
				return null;
			} else {
				return Integer.parseInt(number);
			}
	    }
	    
	    private void updateSummaries () {
	    	if (data.smtpPort == null) {
	    		smtpPortText.setSummary(getResources().getString
	    				(R.string.using_default_port, 25));
	    	} else {
	    		smtpPortText.setSummary(getResources().getString
	    				(R.string.using_port, data.smtpPort));	    		
	    	}
	    	if (data.smtpsPort == null) {
	    		smtpsPortText.setSummary(getResources().getString
	    				(R.string.using_default_port, 465));
	    	} else {
	    		smtpsPortText.setSummary(getResources().getString
	    				(R.string.using_port, data.smtpsPort));	    		
	    	}
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = (Data)getIntent().getSerializableExtra(DATA);
		fragment = new AccountSettingsFragment();
	    // Display the fragment as the main content.
	    getFragmentManager().beginTransaction()
	            .replace(android.R.id.content, fragment)
	            .commit();
	}

	@Override
	public void finish() {
		Intent resultIntent = new Intent();
	    resultIntent.putExtra(DATA, data); 
		setResult(RESULT_OK, resultIntent); 
		super.finish();
	}
}
