/** MessageAdapter.java ---
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

import java.util.List;
import android.widget.ArrayAdapter;
import android.content.Context;
import com.m039.magnifier.objs.InboxRequest.Inbox.InboxData.Comments.CommentsData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import com.m039.magnifier.R;
import android.widget.TextView;
import android.widget.ImageView;
import com.m039.magnifier.data.GlobalData;
import android.text.Spannable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

/**
 *
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MessageAdapter extends ArrayAdapter<CommentsData> {

    public MessageAdapter(Context context, List<CommentsData> comments) {
        super(context, R.layout.e_message, comments);
    }

    public View getView(final int position,  View view, final ViewGroup parent) {
        final CommentsData comment = (CommentsData) getItem(position);

        if (view == null) {
            Context ctx = getContext();
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.e_message, parent, false);
        }

        final View v = view;

        View me, you, root;

        me = v.findViewById(R.id.me);
        you = v.findViewById(R.id.you);

        // Sorry.. no holder patern for optimization

        Bitmap userImage = getUserImage(comment);

        if (userImage == null) {
            // me
            me.setVisibility(View.VISIBLE);
            you.setVisibility(View.INVISIBLE);

            root = me;
        } else {
            // you
            you.setVisibility(View.VISIBLE);
            me.setVisibility(View.INVISIBLE);

            root = you;
        }

        ((TextView) root.findViewById(R.id.message)).setText(getMessage(comment));

        if (userImage != null) {
            ((ImageView) root.findViewById(R.id.upic)).setImageBitmap(userImage);
        }

        return v;
    }


    Spanned getMessage(CommentsData comment) {
        String textToSearch = GlobalData.getInstance().mTextToSearch;
        String message = comment.message;

        if (textToSearch == null || textToSearch.isEmpty()) {
            return new SpannableString(message);
        } else {
            return Html.fromHtml(message.replaceAll(textToSearch,
                                                    "<font color=\"#00FF00\">" + textToSearch + "</font>"));
        }
    }

    static public Bitmap getUserImage(CommentsData comment) {
        return GlobalData.getInstance().getUserImage(comment.from.id);
    }

} // MessageAdapter
