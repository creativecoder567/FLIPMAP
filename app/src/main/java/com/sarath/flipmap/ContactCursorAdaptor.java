package com.sarath.flipmap;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by vinoth on 9/1/17.
 */

public class ContactCursorAdaptor extends CursorAdapter{

    public ContactCursorAdaptor(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.listrow,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textView = (TextView) view.findViewById(R.id.tvName);
        String name=cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.Column_NAME));
        textView.setText(name);

    }

}
