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
import android.os.AsyncTask;

import com.servitlda.sitposmz.logic.DataWriter;
import com.servitlda.sitposmz.logic.model.pos.POSOrder;
import com.servitlda.sitposmz.logic.webservices.WebServiceRequestData;
import com.servitlda.sitposmz.ui.MainActivity;
import com.servitlda.sitposmz.ui.PayOrderActivity;

/**
 * Represents an asynchronous creating task used to send
 * the order to iDempiere
 * Created by Diego Ruiz on 2/03/16.
 */
public class CreateOrderTask extends AsyncTask<POSOrder, Void, Boolean> {

    private Activity mActivity;
    private boolean connectionError = false;
    private String  errorMessage = "";

    public CreateOrderTask(Activity callerActivity) {
        mActivity = callerActivity;
    }

    @Override
    protected Boolean doInBackground(POSOrder... orders) {

        DataWriter writer = new DataWriter();
        boolean success = true;
        WebServiceRequestData wsData = new WebServiceRequestData(mActivity.getBaseContext());

        if (wsData.isDataComplete()) {
            for (POSOrder order : orders) {
                writer.writeOrder(order, mActivity.getBaseContext(), wsData);
                //If no success creating the order in iDempiere and the problem is the connection with the server
                if (!writer.isSuccess()) {
                    success = false;

                    if (writer.isConnectionError())
                        connectionError = true;

                    errorMessage = writer.getErrorMessage();
                    break;

                }
                order.setSync(true);
                order.updateOrder(mActivity.getBaseContext());
                success = true;
            }
        } else {
            success = false;
        }

        return success;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (mActivity instanceof PayOrderActivity)
            ((PayOrderActivity) mActivity).postExecuteTask(success, connectionError, errorMessage);

        if (mActivity instanceof MainActivity)
            ((MainActivity) mActivity).postExecuteCreateOrderTask(success, connectionError, errorMessage);
    }
}