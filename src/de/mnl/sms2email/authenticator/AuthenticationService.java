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
 */
package de.mnl.sms2email.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * The place to live for the Authenticator.
 * 
 * @author mnl
 */
public class AuthenticationService extends Service {

	private final static String TAG = "AuthenticationService";
	
	private Authenticator authenticator;
	
	public AuthenticationService() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, TAG + " started.");
        }
		authenticator = new Authenticator(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, TAG + " returns binder for intent " + intent);
        }
        return authenticator.getIBinder();
	}

    @Override
    public void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, TAG + " stopped.");
        }
    }

}
