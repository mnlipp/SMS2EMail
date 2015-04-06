package de.mnl.sms2email.authenticator;

import de.mnl.sms2email.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

public class AddAccountAdvanced extends Activity {

	public static final String 
		ENFORCE_SECURE_CONNECTIONS = "enforceSecureConnections";
	public static final String 
		ENFORCE_TRUSTED_CERTIFICATES = "enforceTrustedCertificates";

	private PreferenceFragment fragment;
	private CheckBoxPreference secureConnectionsCheckBox;
	private CheckBoxPreference trustedCertificatesCheckBox;
	
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
		setResult(RESULT_OK, resultIntent); 

		super.finish();
	}
	
}
