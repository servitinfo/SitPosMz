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
package com.servitlda.sitposmz.logic.model.report;

import android.content.Context;

import java.math.BigDecimal;
import java.util.List;

import com.servitlda.sitposmz.R;
import com.servitlda.sitposmz.logic.daomanager.PosReportManagement;

/**
 * Created by Diego Ruiz on 25/03/16.
 */
public class VoidItemsReport extends Report {

    private List<ReportGenericObject> voidedLines;
    private ReportHtmlTemplate htmlTemplate;

    public VoidItemsReport(Context mContext) {
        super(mContext);
        htmlTemplate = new ReportHtmlTemplate();
    }

    @Override
    protected void performReport() {
        voidedLines = new PosReportManagement(mContext).getVoidedReportRows(fromDate, toDate);
    }

    /**
     * Set the result with the html template
     */
    @Override
    protected void setReportResult() {

        htmlResult.append(htmlTemplate.getHtmlTemplate().replace(ReportHtmlTemplate.TITLE_TAG, name));
        if(voidedLines != null && !voidedLines.isEmpty()) {

            BigDecimal totalVoided = BigDecimal.ZERO;
            int totalQty = 0;

            //Open HTML table
            htmlResult.append(htmlTemplate.getHtmlTableHeader());

            //Every iteration creates a table row
            for(ReportGenericObject genericObject : voidedLines) {

                htmlResult.append(htmlTemplate.getHtmlRowOpen());

                totalVoided = totalVoided.add(genericObject.getAmount());
                totalQty = totalQty + Integer.parseInt(genericObject.getQuantity());

                htmlResult.append(htmlTemplate.getHtmlColumn("left").replace(ReportHtmlTemplate.ROW_TAG, genericObject.getDescription()));
                htmlResult.append(htmlTemplate.getHtmlColumn("center").replace(ReportHtmlTemplate.ROW_TAG, genericObject.getQuantity()));
                htmlResult.append(htmlTemplate.getHtmlColumn("right").replace(ReportHtmlTemplate.ROW_TAG, getFormattedValue(genericObject.getAmount())));

                htmlResult.append(htmlTemplate.getHtmlRowClose());
            }

            //Total row
            htmlResult.append(htmlTemplate.getHtmlRowOpen());

            htmlResult.append(htmlTemplate.getHtmlColumn("left").replace(ReportHtmlTemplate.ROW_TAG, mContext.getString(R.string.total)));
            htmlResult.append(htmlTemplate.getHtmlColumn("center").replace(ReportHtmlTemplate.ROW_TAG, String.valueOf(totalQty)));
            htmlResult.append(htmlTemplate.getHtmlColumn("right").replace(ReportHtmlTemplate.ROW_TAG, getFormattedValue(totalVoided)));

            htmlResult.append(htmlTemplate.getHtmlRowClose());

            //Close HTML table
            htmlResult.append(htmlTemplate.getHtmlTableClose());
            htmlResult.append(htmlTemplate.getHtmlClose());
        }
        else {
            htmlResult.append(htmlTemplate.getRowText().replace(ReportHtmlTemplate.ROW_TAG, mContext.getString(R.string.no_records)));
            htmlResult.append(htmlTemplate.getHtmlClose());
        }

    }

}
