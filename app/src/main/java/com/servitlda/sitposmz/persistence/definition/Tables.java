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
package com.servitlda.sitposmz.persistence.definition;

import com.servitlda.sitposmz.persistence.dbcontract.DefaultPosDataContract;
import com.servitlda.sitposmz.persistence.dbcontract.GroupTableContract;
import com.servitlda.sitposmz.persistence.dbcontract.KitchenNoteContract;
import com.servitlda.sitposmz.persistence.dbcontract.OrgInfoContract;
import com.servitlda.sitposmz.persistence.dbcontract.OutputDeviceContract;
import com.servitlda.sitposmz.persistence.dbcontract.PosOrderContract;
import com.servitlda.sitposmz.persistence.dbcontract.PosOrderLineContract;
import com.servitlda.sitposmz.persistence.dbcontract.PosPaymentContract;
import com.servitlda.sitposmz.persistence.dbcontract.PosTenderTypeContract;
import com.servitlda.sitposmz.persistence.dbcontract.ProductCategoryContract;
import com.servitlda.sitposmz.persistence.dbcontract.ProductContract;
import com.servitlda.sitposmz.persistence.dbcontract.ProductPriceContract;
import com.servitlda.sitposmz.persistence.dbcontract.SessionPreferenceContract;
import com.servitlda.sitposmz.persistence.dbcontract.TableContract;
import com.servitlda.sitposmz.persistence.dbcontract.TaxCategoryContract;
import com.servitlda.sitposmz.persistence.dbcontract.TaxContract;
import com.servitlda.sitposmz.persistence.dbcontract.UserContract;

/**
 * Created by Diego Ruiz on 23/12/15.
 */
public interface Tables {

    //Controls the database version
    String TABLE_META_INDEX = "meta_index";

    //Access tables
    String TABLE_USER = UserContract.User.TABLE_NAME;

    //Physical space tables
    String TABLE_TABLE         = TableContract.TableDB.TABLE_NAME;
    String TABLE_TABLE_GROUP   = GroupTableContract.GroupTableDB.TABLE_NAME;

    //Taxes
    String TABLE_TAX_CATEGORY  = TaxContract.TaxDB.TABLE_NAME;
    String TABLE_TAX           = TaxCategoryContract.TaxCategoryDB.TABLE_NAME;

    //Order management tables
    String TABLE_POSORDER       = PosOrderContract.POSOrderDB.TABLE_NAME;
    String TABLE_POSORDER_LINE  = PosOrderLineContract.POSOrderLineDB.TABLE_NAME;
    String TABLE_POSPAYMENT     = PosPaymentContract.POSPaymentDB.TABLE_NAME;
    String TABLE_POSTENDER_TYPE = PosTenderTypeContract.PosTenderTypeDB.TABLE_NAME;

    //Product management tables
    String TABLE_PRODUCT          = ProductContract.ProductDB.TABLE_NAME;
    String TABLE_PRODUCT_CATEGORY = ProductCategoryContract.ProductCategoryDB.TABLE_NAME;
    String TABLE_PRODUCT_PRICE    = ProductPriceContract.ProductPriceDB.TABLE_NAME;

    //Data to send request to iDempiere
    String TABLE_DEFAULT_POS_DATA = DefaultPosDataContract.DefaultDataDB.TABLE_NAME;

    //Organization info from iDempiere
    String TABLE_ORG_INFO = OrgInfoContract.OrgInfoDB.TABLE_NAME;

    //Output device
    String TABLE_OUTPUT_DEVICE = OutputDeviceContract.OutputDeviceDB.TABLE_NAME;

    //Kitchen notes
    String TABLE_KITCHEN_NOTE = KitchenNoteContract.KitchenNoteDB.TABLE_NAME;

    //Kitchen notes
    String TABLE_SESSION_PREFERENCE = SessionPreferenceContract.SessionPreferenceDB.TABLE_NAME;

}
