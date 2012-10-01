/** Magnifier.java ---
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

package com.m039.magnifier.widget;

import android.content.Context;
import android.util.AttributeSet;
import java.util.List;
import android.graphics.Paint;
import android.graphics.Color;
import android.view.View;
import android.graphics.Canvas;
import com.m039.magnifier.objs.InboxRequest.Inbox.InboxData.Comments.CommentsData;
import com.m039.magnifier.data.GlobalData;
import com.m039.magnifier.adapter.MessageAdapter;

/**
 *
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class Magnifier extends View {

    public Magnifier(Context context) {
        super(context);
    }

    public Magnifier(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        List<CommentsData> comments = GlobalData.getInstance().mComments;

        if (comments == null) {
            super.onDraw(canvas);
        } else {
            int width = getWidth();
            int height = getHeight();

            drawCommentLines(canvas, comments, width, height);

            super.onDraw(canvas);
        }
    }

    void drawCommentLines(Canvas canvas, List<CommentsData> comments, int width, int height) {
        String textToSearch = GlobalData.getInstance().mTextToSearch;
        int firstVisibleItem = GlobalData.getInstance().mFirstVisibleItem;

        int count = comments.size();
        float dw = width / count;

        Paint me = new Paint();
        me.setColor(Color.RED);

        Paint you = new Paint();
        you.setColor(Color.GREEN);

        Paint check = new Paint();
        check.setColor(Color.BLACK);
        check.setStrokeWidth(4);

        canvas.save();

        for(int i = 0; i < count; i++) {
            canvas.translate(dw, 0);

            if (i == firstVisibleItem) {
                canvas.drawLine(0, 0, 0, height, check);
            }
            
            CommentsData comment = comments.get(i);
            Paint p;

            if (MessageAdapter.getUserImage(comment) == null) {
                // me

                p = me;
                
                if (!textToSearch.isEmpty() && comment.message.indexOf(textToSearch) != -1) {
                    p.setStrokeWidth(3);
                    // p = check;
                } else {
                    p.setStrokeWidth(1);
                    // p = me;
                }

                canvas.drawLine(0, height / 2, 0, height, p);
            } else {
                // you
                p = you;

                if (!textToSearch.isEmpty() && comment.message.indexOf(textToSearch) != -1) {
                    p.setStrokeWidth(3);                    
                    // p = check;
                } else {
                    p.setStrokeWidth(1);
                    // p = you;
                }

                canvas.drawLine(0, 0, 0, height / 2, p);
            }
        }

        canvas.restore();
    }

} // Magnifier
