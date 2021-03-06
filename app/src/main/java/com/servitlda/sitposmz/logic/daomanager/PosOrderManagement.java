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
package com.servitlda.sitposmz.logic.daomanager;

import android.content.Context;

import java.util.List;

import com.servitlda.sitposmz.logic.model.idempiere.Table;
import com.servitlda.sitposmz.logic.model.pos.POSOrder;
import com.servitlda.sitposmz.logic.model.pos.POSOrderLine;

/**
 * Created by Diego Ruiz on 23/12/15.
 */
public class PosOrderManagement extends AbstractObjectManagement {

    public PosOrderManagement(Context ctx) {
        super(ctx);
    }

    @Override
    public POSOrder get(long id){
        return null;
    }

    public Table getTable(long id){
        return dataMapper.getTable(id);
    }

    public List<POSOrder> getAllOpenOrders() {
        return dataMapper.getOpenOrders();
    }

    public List<POSOrder> getClosedOrders() {
        return dataMapper.getClosedOrders();
    }

    public List<POSOrder> getUnsynchronizedOrders() {
        return dataMapper.getUnsynchronizedOrders();
    }

    public List<POSOrder> getPaidOrders(long fromDate, long toDate) {
        return dataMapper.getPaidOrders(fromDate, toDate);
    }

    public List<POSOrder> getUserPaidOrders(long fromDate, long toDate) {
        return dataMapper.getUserPaidOrders(fromDate, toDate);
    }

    public List<POSOrder> getTableOrders (Table table) {
        return dataMapper.getTableOrders(table);
    }

    public List<POSOrderLine> getPrintKitchenLines(POSOrder order) {
        return dataMapper.getPrintKitchenLines(order);
    }

    public List<POSOrderLine> getPrintBarLines(POSOrder order) {
        return dataMapper.getPrintBarLines(order);
    }

    public String getServerName(POSOrder order) {
        return dataMapper.getServerName(order);
    }

    public int getMaxDocumentNo(String orderPrefix) {
        return dataMapper.getMaxDocumentNo(orderPrefix);
    }

    public long getOrderDate(POSOrder order) {
        return dataMapper.getOrderDate(order);
    }
}
