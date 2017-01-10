package com.sarath.flipmap;

import android.provider.BaseColumns;

/**
 * Created by vinoth on 9/1/17.
 */

public class ContactContract {

     static final class ContactEntry implements BaseColumns {
        public static final  String TABLE_NAME="contacts";
        public static final  String Column_NAME="name";
        public static final String COLUMN_EMAIL="email";
        public static final String COLUMN_HOMENO="homeno";
        public static final String COLUMN_PHONENO="phone";


    }
}
