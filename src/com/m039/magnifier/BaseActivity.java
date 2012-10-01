/** BaseActivity.java --- 
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

import com.m039.magnifier.data.GlobalData;
import android.app.Activity;
import android.content.SharedPreferences;
import com.facebook.android.Facebook;
import android.os.Bundle;
import android.content.Intent;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */
public class BaseActivity extends Activity {

    public static final String TAG = "m039";
    
    static Facebook mFacebook = new Facebook("293215484124892");

    static ObjectMapper mMapper = new ObjectMapper();

    public Facebook getFacebook() {
        return mFacebook;
    }

    public ObjectMapper getMapper() {
        return mMapper;
    }

    SharedPreferences mPrefs;

    public SharedPreferences getPrefs() {
        return mPrefs;
    }

    public GlobalData getGlobalData() {
        return GlobalData.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getPreferences(MODE_PRIVATE);
        
        initFacebook();
    }
    
    void initFacebook() {
        if(!mFacebook.isSessionValid()) {
            String access_token = mPrefs.getString("access_token", null);
            long expires = mPrefs.getLong("access_expires", 0);
            if(access_token != null) {
                mFacebook.setAccessToken(access_token);
            }
            if(expires != 0) {
                mFacebook.setAccessExpires(expires);
            }
        }
    }
    
    void deinitFacebook() {
        String at = null;
        int expires = 0;
        
        mFacebook.setAccessToken(at);
        mFacebook.setAccessExpires(expires);

        SharedPreferences.Editor editor = getPrefs().edit();
                    
        editor.putString("access_token", at);
        editor.putLong("access_expires", expires);
        editor.commit();
    }

        
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    void startUserActivity() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
    
} // BaseActivity
