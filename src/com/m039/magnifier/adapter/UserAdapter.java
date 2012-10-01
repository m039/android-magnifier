/** UserAdapter.java --- 
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

package com.m039.magnifier.adapter;

import com.m039.magnifier.objs.InboxRequest;
import com.m039.magnifier.data.GlobalData;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.m039.magnifier.objs.InboxRequest.Inbox.InboxData;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.m039.magnifier.R;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */

public class UserAdapter extends ArrayAdapter<InboxData> {

    // GlobalData mData;
    
    public UserAdapter(Context context, GlobalData data) {
        super(context, R.layout.e_user, data.mInboxRequest.inbox.data);

        // mData = data;
    }

    public View getView(final int position,  View view, final ViewGroup parent) {
        final InboxData inbox = (InboxData) getItem(position);

        if (view == null) {
            Context ctx = getContext();
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.e_user, parent, false);
        }

        final View v = view;
        
        ((TextView) v.findViewById(R.id.name)).setText(getName(inbox));
        ((ImageView) v.findViewById(R.id.upic)).setImageBitmap(getUserImage(inbox));

        return v;
    }

    static public String getName(InboxData inbox) {
        return inbox.to.data.get(1).name;
    }

    static public Bitmap getUserImage(InboxData inbox) {
        return GlobalData.getInstance().getUserImage(inbox.to.data.get(1).id);
    }
    

} // UserAdapter
