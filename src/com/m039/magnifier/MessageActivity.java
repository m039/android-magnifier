/** MessageActivity.java ---
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
import android.widget.ListView;
import com.m039.magnifier.adapter.MessageAdapter;
import java.util.List;
import com.m039.magnifier.objs.InboxRequest.Inbox.InboxData.Comments.CommentsData;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import com.m039.magnifier.data.GlobalData;
import android.view.View;
import android.widget.AbsListView;
import com.m039.magnifier.widget.Magnifier;

/**
 *
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MessageActivity extends BaseActivity {

    ListView mList;
    EditText mEdit;
    Magnifier mMagnifier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        int index = getIntent().getExtras().getInt("message-index");

        List<CommentsData> comments = getCommentsData(index);

        mList = (ListView) findViewById(R.id.list);
        mList.setAdapter(new MessageAdapter(this, comments));

        mMagnifier = (Magnifier) findViewById(R.id.magnifier);
        mMagnifier.linkToListView(mList);
        mMagnifier.setItemAnalyzer(mItemAnalyzer);

        mEdit = (EditText) findViewById(R.id.edit);
        mEdit.addTextChangedListener(mTextWatcher);
    }

    List<CommentsData> getCommentsData(int index) {
        return getGlobalData().getInbox().data.get(index).comments.data;
    }

    class MessageItemAnalyzer
        implements Magnifier.ItemAnalyzer
    {
        boolean old = false;

        public boolean isDataSetChanged() {
            if (old) {
                return false;
            } else {
                old = true;
                return true;
            }
        }

        public boolean isMineItem(Object item) {
            CommentsData comment = (CommentsData) item;

            if (MessageAdapter.getUserImage(comment) == null) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isNotMineItem(Object item) {
            return !isMineItem(item);
        }

        public boolean isSelectedItem(Object item) {
            CommentsData comment = (CommentsData) item;
               
            return !textToSearch.isEmpty() && comment.message.indexOf(textToSearch) != -1;
        }

        String textToSearch = "";
        
        void setTextToSearch(String text) {
            textToSearch = text;
            old = false;
            mMagnifier.invalidate();
        }
    }

    MessageItemAnalyzer mItemAnalyzer = new MessageItemAnalyzer();  
        
    TextWatcher mTextWatcher = new TextWatcher() {
            public void     afterTextChanged(Editable s) {
            }
            
            public void     beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            public void     onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s);

                MessageAdapter madapter = (MessageAdapter) mList.getAdapter();
                madapter.setTextToSearch(text);
                madapter.notifyDataSetChanged();

                mItemAnalyzer.setTextToSearch(text);
            }
        };

} // MessageActivity
