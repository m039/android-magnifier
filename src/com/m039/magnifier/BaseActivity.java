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

import android.app.Activity;
import android.content.SharedPreferences;
import com.facebook.android.Facebook;
import android.os.Bundle;

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

    static Facebook mFacebook = new Facebook("293215484124892");

    public Facebook getFacebook() {
        return mFacebook;
    }

    SharedPreferences mPrefs;

    public SharedPreferences getPrefs() {
        return mPrefs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFacebook();
    }
    
    void initFacebook() {
        if(!mFacebook.isSessionValid()) {
            mPrefs = getPreferences(MODE_PRIVATE);
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
    
} // BaseActivity
