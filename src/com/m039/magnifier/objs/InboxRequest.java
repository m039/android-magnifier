/** Inbox.java --- 
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

package com.m039.magnifier.objs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * 
 *
 * Created: 10/01/12
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InboxRequest {

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class Inbox {

        //
        // Each for the conversation
        //
        @JsonIgnoreProperties(ignoreUnknown = true)
        static public class InboxData {

            @JsonIgnoreProperties(ignoreUnknown = true)
            static public class To {
                
                @JsonIgnoreProperties(ignoreUnknown = true)
                static public class ToData {
                    public String name;
                    public String id;
                }

                public List<ToData> data;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            static public class Comments {

                // 
                // Each for message
                //
                @JsonIgnoreProperties(ignoreUnknown = true)
                static public class CommentsData {
                    
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    static public class From extends To.ToData {}
                    
                    public String message;
                    public From from;
                }

                @JsonIgnoreProperties(ignoreUnknown = true)
                static public class Paging {
                    public String previous;
                    public String next;
                }

                public List<CommentsData> data;
                public Paging paging;
            }

            public To to;
            public Comments comments;
        }
        
        public List<InboxData> data;
    }

    public Inbox inbox;
    
} // Inbox
