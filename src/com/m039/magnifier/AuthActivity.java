/** AuthActivity.java --- 
 *
 * Copyright (C) 2012 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.  If not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301, USA.
 */

package com.m039.magnifier;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;

/**
 * 
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */
public class AuthActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
    }

    public void onButtonClick(View button) {
        if (button == null)
            return;
        
        int id = button.getId();

        switch (id) {
        case R.id.login:
            if (getFacebook().isSessionValid()) {
                startUserActivity();
            } else {
                authorize();
            }
            break;
        case R.id.logout:
            logout();
            break;          
        }
    }

    void authorize() {
        final Facebook facebook =  getFacebook();
        
        if(!facebook.isSessionValid()) {
            facebook.authorize(this, new String[] { "read_mailbox" }, Facebook.FORCE_DIALOG_AUTH,
                               new DialogListener() {
                    @Override
                    public void onComplete(Bundle values) {
                        SharedPreferences.Editor editor = getPrefs().edit();
                    
                        editor.putString("access_token", facebook.getAccessToken());
                        editor.putLong("access_expires", facebook.getAccessExpires());
                        editor.commit();

                        onButtonClick(findViewById(R.id.login));
                    }
    
                    @Override
                    public void onFacebookError(FacebookError error) {}
    
                    @Override
                    public void onError(DialogError e) {}
    
                    @Override
                    public void onCancel() {}
                });
            
        } else {
            Toast.makeText(this, R.string.auth_already_auth, Toast.LENGTH_SHORT).show();
        } 
    }

    void logout() {
        final Facebook facebook =  getFacebook();
        
        if(!facebook.isSessionValid()) {
            Toast.makeText(this, R.string.auth_not_already_auth, Toast.LENGTH_SHORT).show();
        } else {
            new AsyncFacebookRunner(getFacebook()).logout(this, new RequestListener() {
                    @Override
                    public void onComplete(String response, Object state) {
                        deinitFacebook();
                        runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(AuthActivity.this,
                                                   R.string.auth_logout_complete, Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
  
                    @Override
                    public void onIOException(IOException e, Object state) {}
  
                    @Override
                    public void onFileNotFoundException(FileNotFoundException e, Object state) {}
  
                    @Override
                    public void onMalformedURLException(MalformedURLException e, Object state) {}
  
                    @Override
                    public void onFacebookError(FacebookError e, Object state) {}
                });
        } 
    }

    
} // AuthActivity

