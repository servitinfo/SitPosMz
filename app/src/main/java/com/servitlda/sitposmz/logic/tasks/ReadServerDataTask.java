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
package com.servitlda.sitposmz.logic.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.servitlda.sitposmz.logic.DataReader;
import com.servitlda.sitposmz.logic.webservices.WebServiceRequestData;
import com.servitlda.sitposmz.ui.fragment.AsyncFragment;

/**
 * Created by Diego Ruiz on 5/23/16.
 */
public class ReadServerDataTask extends AsyncTask<Void, Void, Boolean> {

    private AsyncFragment mFragment;
    private static final String TAG = "ReadServerDataTask";

    public ReadServerDataTask(AsyncFragment fragment) {
        mFragment = fragment;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        Context ctx;
        DataReader data;

        if (mFragment != null && mFragment.getParentActivity() != null) {
            ctx = ((Activity) mFragment.getParentActivity()).getBaseContext();

            WebServiceRequestData wsData = new WebServiceRequestData(ctx);

            //Check if the necessary data to perform a web service call exists
            if (wsData.isDataComplete()) {
                data = new DataReader(wsData, ctx);
            } else {
                Log.e(TAG, "Invalid Web service request data");
                return false;
            }
        } else
            return false;


        return data.isDataComplete() && !data.isError();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mFragment.handleTaskFinish(result);
    }
}