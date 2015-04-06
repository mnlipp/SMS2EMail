package de.mnl.sms2email.authenticator;

import de.mnl.sms2email.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class AddAccountAdvanced extends Activity {

	public static final String 
		ENFORCE_SECURE_CONNECTIONS = "enforceSecureConnections";
	public static final String 
		ENFORCE_TRUSTED_CERTIFICATES = "enforceTrustedCertificates";
	public static final String SMTP_PORT = "smtpPort";
	public static final String SMTPS_PORT = "smtpsPort";

	private PreferenceFragment fragment;
	private CheckBoxPreference secureConnectionsCheckBox;
	private CheckBoxPreference trustedCertificatesCheckBox;
	private Integer smtpPort = null;
	private Integer smtpsPort = null;
	private EditTextPreference smtpPortText;
	private EditTextPreference smtpsPortText;
	
	private class AccountSettingsFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.account_preferences);
		    secureConnectionsCheckBox = (CheckBoxPreference)
		    		fragment.findPreference("enforce_secure_connections");
			secureConnectionsCheckBox.setChecked
				(getIntent().getBooleanExtra(ENFORCE_SECURE_CONNECTIONS, true));
		    
		    trustedCertificatesCheckBox = (CheckBoxPreference)
		    		fragment.findPreference("enforce_trusted_certificates");
			trustedCertificatesCheckBox.setChecked
				(getIntent().getBooleanExtra(ENFORCE_TRUSTED_CERTIFICATES, true));

			smtpPort = getIntent().getIntExtra(SMTP_PORT, 25);
			smtpsPort = getIntent().getIntExtra(SMTPS_PORT, 465);
			
			smtpPortText = (EditTextPreference)
				fragment.findPreference("smtp_port");
			smtpPortText.setOnPreferenceChangeListener
				(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange
						(Preference preference, Object newValue) {
					smtpPort = parseNumber(newValue);
					updateSummaries();
					return true;
				}
			});
			
			smtpsPortText = (EditTextPreference)
				fragment.findPreference("smtps_port");
			smtpsPortText.setOnPreferenceChangeListener
				(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(
						Preference preference, Object newValue) {
					smtpsPort = parseNumber(newValue);
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
	    	if (smtpPort == null) {
	    		smtpPortText.setSummary(getResources().getString
	    				(R.string.using_default_port, 25));
	    	} else {
	    		smtpPortText.setSummary(getResources().getString
	    				(R.string.using_port, smtpPort));	    		
	    	}
	    	if (smtpsPort == null) {
	    		smtpsPortText.setSummary(getResources().getString
	    				(R.string.using_default_port, 465));
	    	} else {
	    		smtpsPortText.setSummary(getResources().getString
	    				(R.string.using_port, smtpsPort));	    		
	    	}
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = new AccountSettingsFragment();
	    // Display the fragment as the main content.
	    getFragmentManager().beginTransaction()
	            .replace(android.R.id.content, fragment)
	            .commit();
	}

	@Override
	public void finish() {
		Intent resultIntent = new Intent();
	    resultIntent.putExtra(ENFORCE_SECURE_CONNECTIONS, 
	    		secureConnectionsCheckBox.isChecked());
	    resultIntent.putExtra(ENFORCE_TRUSTED_CERTIFICATES, 
	    		trustedCertificatesCheckBox.isChecked());
	    resultIntent.putExtra(SMTP_PORT, smtpPort);
	    resultIntent.putExtra (SMTPS_PORT, smtpsPort);
	    
		setResult(RESULT_OK, resultIntent); 

		super.finish();
	}
	
}
