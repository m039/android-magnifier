/** UsersActivity.java ---
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

import com.m039.magnifier.adapter.UserAdapter;

import android.os.Bundle;
import com.facebook.android.Facebook;
import android.widget.ProgressBar;
import android.view.View;
import android.util.Log;
import com.facebook.android.AsyncFacebookRunner;
import android.widget.Toast;
import com.m039.magnifier.objs.InboxRequest;
import java.io.StringWriter;
import android.widget.ListView;
import java.io.InputStream;
import com.m039.magnifier.data.GlobalData;
import com.m039.magnifier.objs.InboxRequest.Inbox.InboxData;
import android.graphics.BitmapFactory;
import android.widget.AdapterView;

/**
 *
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class UserActivity extends BaseActivity {

    View mProgress;
    ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        mProgress = findViewById(R.id.progress);

        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(mOnItemClickListener);

        Bundle args = new Bundle();

        args.putString("fields", "inbox");

        new AsyncFacebookRunner(getFacebook()).request("me", args, new AsyncFacebookRunner.RequestListener() {
                @Override
                public void onComplete(String response, Object state) {
                    try {

                        fetchAll(response);
                        goneProgress();
                        updateList();

                    } catch (java.io.IOException e) {
                        Log.wtf(TAG, "onCreateView", e);
                        goneProgress(true);
                    }
                }

                @Override
                public void onIOException(java.io.IOException e, Object state) {
                    goneProgress(true);
                }

                @Override
                public void onFileNotFoundException(java.io.FileNotFoundException e, Object state) {
                    goneProgress(true);
                }

                @Override
                public void onMalformedURLException(java.net.MalformedURLException e, Object state) {
                    goneProgress(true);
                }

                @Override
                public void onFacebookError(com.facebook.android.FacebookError e, Object state) {
                    goneProgress(true);
                }
            });
    }

    void fetchAll(String response) throws java.io.IOException {
        GlobalData data = getGlobalData();

        data.mInboxRequest = getMapper().readValue(response, InboxRequest.class);

        for(InboxData inbox : data.mInboxRequest.inbox.data) {
            String id = inbox.to.data.get(1).id;

            try {
                InputStream in = new java.net.URL("http://graph.facebook.com/" + id + "/picture").openStream();
                data.putUserImage(id, BitmapFactory.decodeStream(in));
            } catch (Exception e) {
                Log.wtf(TAG, "fetchAll", e);
            }
        }

    }

    void goneProgress() {
        goneProgress(false);
    }

    void goneProgress(final boolean error) {
        runOnUiThread(new Runnable() {
                public void run() {
                    mProgress.setVisibility(View.GONE);
                    if (error) {
                        Toast.makeText(UserActivity.this, R.string.user_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    void updateList() {
        runOnUiThread(new Runnable() {
                public void run() {
                    mList.setAdapter(new UserAdapter(UserActivity.this, getGlobalData()));
                }
            });
    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                startMessageActivity(position);
            }
        };

} // UsersActivity
