package de.mnl.sms2email.authenticator;

import de.mnl.sms2email.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class AddAccountAdvanced extends Activity {

	public static final String 
		ENFORCE_SECURE_CONNECTIONS = "enforceSecureConnections";
	public static final String 
		ENFORCE_TRUSTED_CERTIFICATES = "enforceTrustedCertificates";
	
	private CheckBox enforceSecureConnectionsView;
	private CheckBox enforceTrustedCertificatesView;
	private Intent resultIntent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_account_advanced);
		
		enforceSecureConnectionsView 
			= (CheckBox)findViewById(R.id.enforce_secure_connections_checkbox);
		enforceSecureConnectionsView.setChecked
			(getIntent().getBooleanExtra(ENFORCE_SECURE_CONNECTIONS, true));
		enforceSecureConnectionsView
			.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resultIntent.putExtra(ENFORCE_SECURE_CONNECTIONS, 
							enforceSecureConnectionsView.isChecked());
			}
		});
		enforceTrustedCertificatesView = (CheckBox)
			findViewById(R.id.enforce_trusted_certificates_checkbox);
		enforceTrustedCertificatesView.setChecked
			(getIntent().getBooleanExtra(ENFORCE_TRUSTED_CERTIFICATES, true));
		enforceTrustedCertificatesView
			.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resultIntent.putExtra(ENFORCE_TRUSTED_CERTIFICATES, 
						enforceTrustedCertificatesView.isChecked());
			}
		});

		setResult(RESULT_OK, resultIntent); 
	}

}
