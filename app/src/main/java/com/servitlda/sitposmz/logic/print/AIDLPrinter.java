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
package com.servitlda.sitposmz.logic.print;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servitlda.sitposmz.logic.model.idempiere.DefaultPosData;
import com.servitlda.sitposmz.logic.model.pos.POSOrder;
import com.servitlda.sitposmz.logic.model.pos.POSOrderLine;
import com.servitlda.sitposmz.logic.model.pos.PosProperties;
import com.servitlda.sitposmz.logic.model.report.ReportGenericObject;

/**
 * Created by Diego Ruiz on 21/04/16.
 */
public class AIDLPrinter extends AbstractPOSPrinter {

    public AIDLPrinter(POSOrder order, int pageWidth) {
        super(order, pageWidth);
    }

    /**
     * Print the tickets
     * Returns a string with %s
     * 1 - Order string
     * 2 - Table
     * 3 - Table Number
     * 3 - Server
     * 4 - Guests
     */
    @Override
    protected String getTicketText(String target, String orderLabel, String tableLabel, String tableName, String serverLabel, String guestsLabel) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, PosProperties.getInstance().getLocale());
        Calendar cal = Calendar.getInstance();

        StringBuilder ticket = new StringBuilder();

        List<POSOrderLine> lines = new ArrayList<>();

        if(target.equals(KITCHEN_RECEIPT)) {
            lines = order.getPrintKitchenLines(null);
        } else if (target.equals(BAR_RECEIPT)) {
            lines = order.getPrintBarLines(null);
        }

        for(POSOrderLine line : lines) {
            ticket.append(line.getQtyOrdered() + "  " + line.getProduct().getProductName() + "\r\n");
            if(line.getProductRemark() != null && !line.getProductRemark().isEmpty())
                ticket.append("    " + line.getProductRemark() + "\r\n");
            line.setPrinted(true);
            line.updateLine(null);
        }

        return String.format(ticket.toString(), orderLabel, order.getOrderId(), tableLabel, tableName, serverLabel,
                order.getServerName(null), guestsLabel, order.getGuestNumber(), dateFormat.format(cal.getTime()));
    }

    @Override
    public byte[] printTicket(String target, String orderLabel, String tableLabel, String tableName, String serverLabel, String guestsLabel) {
        return getTicketText(target, orderLabel, tableLabel, tableName, serverLabel, guestsLabel).getBytes();
    }

    /**
     * Returns a string with several %s to format
     * 0 - Page Width
     * 1 - Restaurant Name
     * 2 - Address
     * 3 - City
     * 4 - Receipt label
     * 5 - Receipt Number
     * 6 - Table string
     * 7 - Table name
     * 8 - Server string
     * 9 - Guests string
     * 10 - Amount String
     * 11 - Charges String
     * 12 - Total String
     * 13 - Cash string
     * 14 - Back string
     * 15 - Footer description
     * @return String for receipt printing
     */
    @Override
    protected String getReceiptText(String restaurantName, String address, String city, String receiptLabel, String orderPrefix, String tableLabel, String tableName, String serverLabel,
                                    String guestsLabel, String subtotalLabel, String surchargeLabel, String totalLabel, String cashLabel, String changeLabel) {

        StringBuilder ticket = new StringBuilder();

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(PosProperties.getInstance().getLocale());
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, PosProperties.getInstance().getLocale());
        Calendar cal = Calendar.getInstance();

        //header T font size x y data

        String label = "-----------SIT POS MZ-----------\r\n" +
                "ID: %s\r\n" + //Restaurant name
                "DES: %s\r\n" + //Restaurant address
                "ENDERECO: %s\r\n" + //Restaurant city
                "%s\r\n" + //Restaurant city
                "-----------REFERENCIA-----------\r\n" +
                "%s: %s\r\n" + //Receipt Number
                "%s: %s\r\n" + //Table Number
                "%s: %s\r\n" + //Server name
                "%s: %s\r\n" +  //Guest
                "Data: %s\r\n" +  //Date
                "-------------PRODUTO------------\r\n";

        ticket.append(label);

        DefaultPosData defaultPosData = DefaultPosData.get(null);

        //If single lines for every product -> Configurable
        if(!defaultPosData.isCombineReceiptItems()) {
            for(POSOrderLine line : order.getOrderedLines()) {
                ticket.append(line.getQtyOrdered() + " " + line.getProduct().getProductName() +"\r\nValor: MT " +
                        currencyFormat.format(line.getLineNetAmt()) + "\r\n");
                ticket.append("S/N : " + line.getProductRemark() + "\r\n");
                /*if (line.getProductRemark() != null && !line.getProductRemark().isEmpty())*/
                ticket.append("              ----              \r\n");

            }
        } else {
            //If summarized lines for receipts
            String format = "  %-3s%-32s%7s";
            for(ReportGenericObject line : order.getSummarizeLines()) {
                ticket.append(String.format(format, line.getQuantity(), line.getDescription(),currencyFormat.format(line.getAmount())) + "\r\n");
                ticket.append("              ----              \r\n");
            }
        }

        //footer
        StringBuilder footerSection = new StringBuilder();
        int height = 300;

        int posY = 15;

        footerSection.append("-----------PAGAMENTO------------\r\n"); //offset - 200 - 200 - height -qty
        footerSection.append("%s (MT) : " + currencyFormat.format(order.getTotallines()) +"\r\n"); //Amount of the lines
        /*posY = posY + 40;*/

        footerSection.append("%s (MT) : " + currencyFormat.format(order.getSurcharge()) +"\r\n"); //Charge amount
        /*posY = posY + 40;*/

        footerSection.append("%s (MT) : " + currencyFormat.format(order.getTotallines().add(order.getSurcharge())) +"\r\n"); //Total amount

        /*posY = posY + 40;*/
/*
        footerSection.append("%s :" + currencyFormat.format(order.getCashAmt()) +"\r\n"); //Received amount
        footerSection.append("%s :" +  currencyFormat.format(order.getChangeAmt()) +"\r\n"); //back amount
*/

        /*posY = posY + 45;*/
        footerSection.append("              ----              \r\n"); //back amount
        footerSection.append("\r\n"); //Footer message
        footerSection.append("\r\n"); //Footer message
        footerSection.append("------OBRIGADO VOLTE SEMPRE-----\r\n"); //back amount
        footerSection.append("\r\n"); //SCROLL
        footerSection.append("\r\n"); //SCROLL
        footerSection.append("\r\n"); //SCROLL
        footerSection.append("\r\n"); //SCROLL

                /*label = "! 0 200 200 200 1\r\n" +
                "LEFT\r\n" +
                "LINE 0 0 550 0 2\r\n" +
                "T 7 1 25 15 %s\r\n" + //Total label
                "RIGHT\r\n" +
                "T 7 1 400 15 "+ currencyFormat.format(order.getTotallines()) +"\r\n" + //Total value
                "LEFT\r\n" +
                "T 7 1 25 55 %s\r\n" + //Received
                "RIGHT\r\n" +
                "T 7 1 400 55 " + currencyFormat.format(order.getCashAmt()) +"\r\n" + //Received value
                "LEFT\r\n" +
                "T 7 1 25 95 %s\r\n" + //back label
                "RIGHT\r\n" +
                "T 7 1 400 95 " + currencyFormat.format(order.getChangeAmt()) +"\r\n" + //back
                "CENTER\r\n" +
                "T 7 1 10 150 %s\r\n" + //Footer message
                "POSTFEED 20\r\n" +
                "FORM \r\n\r\n"+
                "PRINT\r\n";*/

        ticket.append(footerSection.toString());

        return String.format(ticket.toString(), pageWidth, restaurantName, address, city, receiptLabel, orderPrefix + order.getOrderId(),
                tableLabel, tableName, serverLabel, order.getServerName(null), guestsLabel, order.getGuestNumber(), dateFormat.format(cal.getTime()),
                subtotalLabel, surchargeLabel, totalLabel, cashLabel, changeLabel, defaultPosData.getReceiptFooter());
    }

    @Override
    public byte[] printReceipt(String restaurantName, String address, String city, String receiptLabel, String orderPrefix, String tableLabel, String tableName, String serverLabel,
                               String guestsLabel, String subtotalLabel, String surchargeLabel, String totalLabel, String cashLabel, String changeLabel) {

        return getReceiptText(restaurantName, address, city, receiptLabel, orderPrefix, tableLabel, tableName, serverLabel,
                guestsLabel, subtotalLabel, surchargeLabel, totalLabel, cashLabel, changeLabel).getBytes();
    }
}

