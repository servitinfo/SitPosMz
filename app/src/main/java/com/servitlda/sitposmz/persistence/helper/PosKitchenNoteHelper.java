/**********************************************************************
 * This file is part of FreiBier POS                                   *
 *                                                                     *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - Diego Ruiz - Bx Service GmbH                                      *
 **********************************************************************/
package com.servitlda.sitposmz.persistence.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import com.servitlda.sitposmz.persistence.dbcontract.KitchenNoteContract;
import com.servitlda.sitposmz.persistence.definition.Tables;

/**
 * Created by Diego Ruiz on 11/05/16.
 */
public class PosKitchenNoteHelper extends PosObjectHelper {

    private static final String LOG_TAG = "Kitchen note Helper";

    public PosKitchenNoteHelper(Context mContext) {
        super(mContext);
    }

    public long createKitchenNote(String note) {
        SQLiteDatabase database = getWritableDatabase();

        int userId = getLoggedUser();

        ContentValues values = new ContentValues();
        values.put(KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_CREATED_AT, Long.parseLong(getCurrentDate()));
        values.put(KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_CREATED_BY, userId);
        values.put(KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_NOTE, note);

        return database.insert(Tables.TABLE_KITCHEN_NOTE, null, values);
    }

    /*
    * note exist
    */
    public boolean noteExist(String note) {

        boolean exists;
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT " + KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_KITCHEN_NOTE_ID +
                " FROM " + Tables.TABLE_KITCHEN_NOTE +
                " WHERE LOWER(" + KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_NOTE + ") =?";

        Log.i(LOG_TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[] {note.toLowerCase()});

        exists = c != null && c.getCount() > 0;

        if (c != null)
            c.close();

        return exists;
    }

    /**
    * Get all kitchen notes
    * @return
    */
    public ArrayList<String> getKitchenNotes() {
        ArrayList<String> notes = new ArrayList<>();

        String selectQuery = "SELECT " + KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_NOTE +
                " FROM " + Tables.TABLE_KITCHEN_NOTE;


        Log.d(LOG_TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null /*new String[] {POSOrder.COMPLETE_STATUS, POSOrder.VOID_STATUS}*/);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                notes.add(c.getString(c.getColumnIndex(KitchenNoteContract.KitchenNoteDB.COLUMN_NAME_NOTE)));
            } while (c.moveToNext());
            c.close();
        }

        return notes;
    }

}
