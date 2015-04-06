/**
 * SMS2EMail Forwaring App
 * Copyright (C) 2014 Michael N. Lipp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class is based on the LoginForm template from Android SDK.
 */
package de.mnl.sms2email.authenticator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import de.mnl.sms2email.R;

/**
 * A screen that allows to define an account.
 */
public class AddAccountActivity extends Activity {

	private static final int ADVANCED_SETTINGS_REQUEST = 1;
	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private ConnectTask mAuthTask = null;

	// UI references.
	private EditText accountNameView;
	private EditText serverUriView;
	private EditText userNameView;
	private EditText passwordView;
	private Button advancedSettingsButton;
	private View progressView;
	private View loginFormView;
	private boolean enforceSecureConnections = true;
	private boolean enforceTrustedConnections = true;
	private Integer smtpPort = null;
	private Integer smtpsPort = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_account);
		
		// Set up the form.
		accountNameView = (EditText) findViewById(R.id.account_name);
		serverUriView = (EditText) findViewById(R.id.host_name);
		userNameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
		advancedSettingsButton 
			= (Button) findViewById(R.id.advanced_settings_button);
		advancedSettingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent
					(AddAccountActivity.this, AddAccountAdvanced.class); 
				intent.putExtra(AddAccountAdvanced.ENFORCE_SECURE_CONNECTIONS,
						enforceSecureConnections);
				intent.putExtra(AddAccountAdvanced.ENFORCE_TRUSTED_CERTIFICATES,
						enforceTrustedConnections);
				intent.putExtra(AddAccountAdvanced.SMTP_PORT, smtpPort);
				intent.putExtra(AddAccountAdvanced.SMTPS_PORT, smtpsPort);
				startActivityForResult(intent, ADVANCED_SETTINGS_REQUEST);
			}
		});

		loginFormView = findViewById(R.id.add_account_form);
		progressView = findViewById(R.id.login_progress);

		findViewById(R.id.create_account_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptConnect();
					}
				});
	}

	@Override
	protected void onActivityResult
		(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == ADVANCED_SETTINGS_REQUEST) {
			enforceSecureConnections = data.getBooleanExtra
				(AddAccountAdvanced.ENFORCE_SECURE_CONNECTIONS, false);
			enforceTrustedConnections = data.getBooleanExtra
					(AddAccountAdvanced.ENFORCE_TRUSTED_CERTIFICATES, false);
			smtpPort = (Integer)
					data.getSerializableExtra(AddAccountAdvanced.SMTP_PORT);
			smtpsPort = (Integer)
					data.getSerializableExtra(AddAccountAdvanced.SMTPS_PORT);
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptConnect() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		serverUriView.setError(null);
		passwordView.setError(null);

		// Store values at the time of the login attempt.
		String server = serverUriView.getText().toString();
		String username = userNameView.getText().toString();
		String password = passwordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid server URI.
		/*
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		*/

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new ConnectTask(username, password);
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			progressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							progressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class ConnectTask extends AsyncTask<Void, Void, Boolean> {

		private final String email;
		private final String password;

		ConnectTask(String email, String password) {
			this.email = email;
			this.password = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				passwordView
						.setError(getString(R.string.error_incorrect_password));
				passwordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
