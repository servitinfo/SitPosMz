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

import com.servitlda.sitposmz.logic.model.idempiere.RestaurantInfo;
import com.servitlda.sitposmz.persistence.dbcontract.OrgInfoContract;
import com.servitlda.sitposmz.persistence.definition.Tables;

/**
 * Created by Diego Ruiz on 27/04/16.
 */
public class PosOrgInfoDataHelper extends PosObjectHelper {

    private static final String LOG_TAG = "Default data Helper";

    public PosOrgInfoDataHelper(Context mContext) {
        super(mContext);
    }

    public long createData(RestaurantInfo data) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_NAME, data.getName());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_ADDRESS1, data.getAddress1());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_ADDRESS2, data.getAddress2());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_CITY, data.getCity());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_PHONE, data.getPhone());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_POSTAL, data.getPostalCode());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_DESCRIPTION, data.getDescription());

        // insert row
        return database.insert(Tables.TABLE_ORG_INFO, null, values);
    }

    /*
    * Updating the default data
    */
    public int updateData(RestaurantInfo data) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_NAME, data.getName());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_ADDRESS1, data.getAddress1());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_ADDRESS2, data.getAddress2());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_CITY, data.getCity());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_PHONE, data.getPhone());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_POSTAL, data.getPostalCode());
        values.put(OrgInfoContract.OrgInfoDB.COLUMN_NAME_DESCRIPTION, data.getDescription());

        // updating row
        return db.update(Tables.TABLE_ORG_INFO, values, OrgInfoContract.OrgInfoDB.COLUMN_NAME_ORG_INFO_ID + " = ?",
                new String[] { String.valueOf(1) });
    }

    /*
    * get single instance of org info
    */
    public RestaurantInfo getOrgInfo(long data_id) {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Tables.TABLE_ORG_INFO + " WHERE "
                + OrgInfoContract.OrgInfoDB.COLUMN_NAME_ORG_INFO_ID + " = ?";

        Log.d(LOG_TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[] { String.valueOf(data_id) });

        if (c != null && c.getCount() > 0)
            c.moveToFirst();
        else {
            if (c != null)
                c.close();
            return null;
        }

        RestaurantInfo restaurantInfo = new RestaurantInfo();
        restaurantInfo.setName(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_NAME)));
        restaurantInfo.setAddress1(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_ADDRESS1)));
        restaurantInfo.setAddress2(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_ADDRESS2)));
        restaurantInfo.setCity(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_CITY)));
        restaurantInfo.setPhone(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_PHONE)));
        restaurantInfo.setPostalCode(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_POSTAL)));
        restaurantInfo.setDescription(c.getString(c.getColumnIndex(OrgInfoContract.OrgInfoDB.COLUMN_NAME_DESCRIPTION)));

        c.close();

        return restaurantInfo;
    }

}
