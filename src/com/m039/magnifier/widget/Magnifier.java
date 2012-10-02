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
import android.widget.ListView;
import android.widget.AbsListView;
import android.view.MotionEvent;

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

    Thumb mThumb = new Thumb();
    Ruler mRuler = new Ruler();
    ItemData mItemData = new ItemData();

    @Override
    protected void onDraw (Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        mThumb.init(w, h);
        mRuler.init(w, h);
        mItemData.init(w, h);

        canvas.save();

        canvas.translate(mRuler.getRightPadding() / 2, 0);

        mThumb.onDraw(canvas);
        // mRuler.onDraw(canvas);
        mItemData.onDraw(canvas);

        canvas.restore();

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

            if (mList != null) {
                float position = 0;

                float x = event.getX();
                int count = mList.getCount();

                position =  (x - mRuler.getRightPadding() / 2)
                    / (getWidth() - mRuler.getRightPadding()) * count;
                
                mList.setSelection((int) position);
            }

            return true;
            
        } else if (action == MotionEvent.ACTION_MOVE) {

            if (mList != null) {
                float position = 0;

                float x = event.getX();
                int count = mList.getCount();

                position =  (x - mRuler.getRightPadding() / 2)
                    / (getWidth() - mRuler.getRightPadding()) * count;

                mList.setSelection((int) position);
            }
            
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            
            return true;
        }

        return false;
    }

    static class Thumb {

        static Paint paint = new Paint();

        static {
            paint.setColor(Color.LTGRAY);
        }

        int firstVisibleItem = 0;
        int visibleItemCount = 0;
        int totalItemCount = 0;

        void update(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            this.totalItemCount = totalItemCount;
        }

        int width;
        int height;

        // init or set the drawable state
        void init(int width, int height) {
            this.width = width;
            this.height = height;
        }

        void onDraw(Canvas canvas) {
            if (totalItemCount == 0 || visibleItemCount == 0) {
                return;
            }

            float dx = width / totalItemCount;
            float w = dx * visibleItemCount;

            canvas.save();

            canvas.translate(dx * firstVisibleItem, 0);
            canvas.drawRect(0, 0, w, height, paint);

            canvas.restore();
        }
    }

    static class Ruler {

        static Paint paint = new Paint();

        static {
            paint.setColor(Color.GREEN);
        }

        int totalItemCount = 0;

        void update(int totalItemCount) {
            this.totalItemCount = totalItemCount;
        }

        int width;
        int height;

        void init(int width, int height) {
            this.width = width;
            this.height = height;
        }

        void onDraw(Canvas canvas) {
            if (totalItemCount == 0) {
                return;
            }

            float dw = width / totalItemCount;

            canvas.save();

            // +1 just for visual clearness
            for(int i = 0; i < totalItemCount + 1; i++) {
                canvas.drawLine(0, 0, 0, height, paint);
                canvas.translate(dw, 0);
            }

            canvas.restore();
        }

        float getRightPadding() {
            float dw = width / totalItemCount;
            return width - dw * totalItemCount;
        }
    }

    static class ItemData {
        int items[] = null;

        static Paint pSelected = new Paint();
        static Paint pMine = new Paint();
        static Paint pNotMine = new Paint();

        static {
            pSelected.setColor(Color.CYAN);
            pMine.setColor(Color.GREEN);
            pNotMine.setColor(Color.RED);
        }

        static final int SELECTED = 1;
        static final int MINE = 2;
        static final int NOT_MINE = 4;

        void update(AbsListView view, ItemAnalyzer analyzer) {
            if (analyzer == null || view == null) {
                return;
            }

            int count = view.getCount();

            if (analyzer.isDataSetChanged()) {
                items = new int[count];

                for(int i = 0; i < count; i++) {
                    Object obj = view.getItemAtPosition(i);

                    int item = 0;

                    if (analyzer.isSelectedItem(obj)) {
                        item |= SELECTED;
                    }

                    if(analyzer.isMineItem(obj)) {
                        item |= MINE;
                    }

                    if(analyzer.isNotMineItem(obj)) {
                        item |= NOT_MINE;
                    }

                    items[i] = item;
                }
            }
        }

        int width;
        int height;

        void init(int width, int height) {
            this.width = width;
            this.height = height;
        }

        void onDraw(Canvas canvas) {
            if (items == null)
                return;

            canvas.save();

            float dw = width / items.length;

            for(int i = 0; i < items.length; i++) {
                int item = items[i];

                boolean selected = false;

                if ((item & SELECTED) == SELECTED) {
                    selected = true;
                }

                int padding = 1;

                if ((item & MINE) == MINE) {

                    canvas.drawRect(padding, height * 2 / 3, dw - padding, height ,
                                    (selected ? pSelected : pMine));

                } else if((item & NOT_MINE) == NOT_MINE) {

                    canvas.drawRect(padding, 0, dw - padding, height / 3,
                                    (selected ? pSelected : pNotMine));

                }

                canvas.translate(dw, 0);
            }

            canvas.restore();
        }

    }

    ListView mList = null;
    
    public void linkToListView(ListView lv) {
        mList = lv;
        
        lv.setOnScrollListener(new AbsListView.OnScrollListener () {
                public void onScroll(AbsListView view,
                                     int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {

                    mThumb.update(firstVisibleItem, visibleItemCount, totalItemCount);
                    mRuler.update(totalItemCount);
                    mItemData.update(view, mItemAnalyzer);

                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScroll(view,
                                                   firstVisibleItem, visibleItemCount, totalItemCount);
                    }

                    invalidate();
                }

                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScrollStateChanged(view, scrollState);
                    }
                }
            });
    }

    AbsListView.OnScrollListener mOnScrollListener = null;

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }


    public interface ItemAnalyzer {
        public boolean isDataSetChanged();

        public boolean isMineItem(Object item);
        public boolean isNotMineItem(Object item);
        public boolean isSelectedItem(Object item);
    }

    ItemAnalyzer mItemAnalyzer = null;

    public void setItemAnalyzer(ItemAnalyzer analyzer) {
        mItemAnalyzer = analyzer;
    }

} // Magnifier
