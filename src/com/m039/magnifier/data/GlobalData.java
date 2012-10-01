/** Global.java --- 
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

package com.m039.magnifier.data;

import com.m039.magnifier.objs.InboxRequest;
import android.graphics.Bitmap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.m039.magnifier.objs.InboxRequest.Inbox;

/**
 * 
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */
public class GlobalData {

    static GlobalData mInstance = new GlobalData();

    static public GlobalData getInstance() {
        return mInstance;
    }

    public InboxRequest mInboxRequest = null;

    public Inbox getInbox() {
        return mInboxRequest.inbox;
    }
    
    public Map<String, Bitmap> mUserImage = new HashMap<String, Bitmap>();

    public Bitmap getUserImage(String id) {
        return mUserImage.get(id);
    }

    public void putUserImage(String id, Bitmap image) {
        mUserImage.put(id, image);
    }

    public String mTextToSearch = "";
    
} // GlobalData
