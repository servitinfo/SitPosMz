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
package com.servitlda.sitposmz.logic.model.idempiere;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.Serializable;
import java.util.List;

import com.servitlda.sitposmz.logic.daomanager.PosTableManagement;
import com.servitlda.sitposmz.logic.tasks.UpdateTableStatusTask;

/**
 * Created by Diego Ruiz on 9/11/15.
 */
public class Table implements Serializable {

    //Table status
    public static final String FREE_STATUS     = "FREE";
    public static final String BUSY_STATUS     = "BUSY";

    public static final String BAY_Table_ID = "BAY_Table_ID";

    private PosTableManagement tableManager;
    private TableGroup belongingGroup;
    private long tableID;
    private String tableName;
    private String value;
    private String status;
    private String serverName = "";
    //last time the status was changed -> yyyymmddhhmm
    private long statusChangeTime = 0;
    private boolean isStatusChanged = false;

    public long getTableID() {
        return tableID;
    }

    public void setTableID(long tableID) {
        this.tableID = tableID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(FREE_STATUS) ||
                status.equals(BUSY_STATUS))
            this.status = status;
    }

    public TableGroup getBelongingGroup() {
        return belongingGroup;
    }

    public void setBelongingGroup(TableGroup belongingGroup) {
        this.belongingGroup = belongingGroup;
    }

    public String getOrderTime() {
        return String.valueOf(statusChangeTime);
    }

    public void setStatusChangeTime(long statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public long getStatusChangeTime() {
        return statusChangeTime;
    }

    public boolean isStatusChanged() {
        return isStatusChanged;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean save(Context ctx) {
        tableManager = new PosTableManagement(ctx);
        Table originalTable = tableManager.get(tableID);
        if (originalTable == null)
            return createTable();
        else {
            status = originalTable.getStatus();
            return updateTable();
        }
    }

    private boolean createTable(){
        return tableManager.create(this);
    }

    /**
     * Set the table as occupied
     * @param ctx
     * @param notify determines if the change will be replicated to the other devices
     * @return
     */
    public boolean occupyTable(Context ctx, boolean notify) {
        status = BUSY_STATUS;
        isStatusChanged = true;

        if (notify)
            updateServerTableStatus(ctx); //Send the new status to iDempiere

        return updateTable(ctx);
    }

    /**
     * Free-up the table
     * @param ctx
     * @param notify determines if the change will be replicated to the other devices
     * @return
     */
    public boolean freeTable(Context ctx, boolean notify) {
        tableManager = new PosTableManagement(ctx);

        //Check if there are no more orders in the table -> if there are, don't free the table
        if (tableManager.isTableFree(this)) {
            status = FREE_STATUS;
            setServerName("");
            isStatusChanged = true;
            if (notify)
                updateServerTableStatus(ctx); //Send the new status to iDempiere

            return updateTable(ctx);
        }

        return false;
    }

    public boolean updateTable(Context ctx) {
        tableManager = new PosTableManagement(ctx);
        return updateTable();
    }

    private boolean updateTable() {
        return tableManager.update(this);
    }

    /**
     * Updates the status in iDempiere if there's internet connection
     * @param ctx
     * @return
     */
    private boolean updateServerTableStatus(Context ctx) {
        if(ctx == null)
            return false;

        //Check if network connection is available
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            UpdateTableStatusTask updateTask = new UpdateTableStatusTask(ctx);
            updateTask.execute(this);
        }

        return true;

    }

    public static Table getTable(Context ctx, long tableID) {
        PosTableManagement tableManager = new PosTableManagement(ctx);
        return tableManager.get(tableID);
    }

    public static List<Table> getAllTables(Context ctx) {
        PosTableManagement dataProvider = new PosTableManagement(ctx);
        return dataProvider.getAllTables();
    }
}
