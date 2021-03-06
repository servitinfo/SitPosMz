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

import com.servitlda.sitposmz.logic.model.idempiere.MProduct;
import com.servitlda.sitposmz.logic.model.idempiere.ProductPrice;
import com.servitlda.sitposmz.persistence.dbcontract.ProductPriceContract;
import com.servitlda.sitposmz.persistence.definition.Tables;

/**
 * Created by Diego Ruiz on 28/12/15.
 */
public class PosProductPriceHelper extends PosObjectHelper {

    private static final String LOG_TAG = "Product Price Helper";

    public PosProductPriceHelper(Context mContext) {
        super(mContext);
    }

    /*
    * Creating a product price
    */
    public long createProductPrice(ProductPrice productPrice) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_PRICE_ID, productPrice.getProductPriceID());
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_ID, productPrice.getProductID());
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIST_VERSION_ID, productPrice.getPriceListVersionID());
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_STD_PRICE, productPrice.getIntegerStdPrice());
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIMIT, productPrice.getIntegerPriceLimit());

        // insert row
        return db.insert(Tables.TABLE_PRODUCT_PRICE, null, values);
    }

    /*
    * get single product price
    */
    public ProductPrice getProductPrice(long productPrice_id) {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Tables.TABLE_PRODUCT_PRICE + " WHERE "
                + ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_PRICE_ID + " = ?";

        Log.d(LOG_TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[] { String.valueOf(productPrice_id) });

        if (c != null && c.getCount() > 0)
            c.moveToFirst();
        else {
            if (c != null)
                c.close();
            return null;
        }

        PosProductHelper productHelper = new PosProductHelper(mContext);

        ProductPrice productPrice = new ProductPrice();
        productPrice.setProductPriceID(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_PRICE_ID)));
        productPrice.setProductID(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_ID)));
        productPrice.setPriceListVersionID(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIST_VERSION_ID)));
        productPrice.setStdPriceFromInt(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_STD_PRICE)));
        productPrice.setPriceLimitFromInt(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIMIT)));
        productPrice.setProduct(productHelper.getProduct(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_ID))));

        c.close();

        return productPrice;
    }

    /*
    * get single product price by product id
    */
    public ProductPrice getProductPriceByProduct(MProduct product) {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Tables.TABLE_PRODUCT_PRICE +
                " WHERE " + ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_ID + " = ?";

        Log.d(LOG_TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[] {String.valueOf(product.getProductID())});

        if (c != null && c.getCount() > 0)
            c.moveToFirst();
        else {
            if (c != null)
                c.close();
            return null;
        }

        ProductPrice productPrice = new ProductPrice();
        productPrice.setProductPriceID(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_PRICE_ID)));
        productPrice.setProductID(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_ID)));
        productPrice.setPriceListVersionID(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIST_VERSION_ID)));
        productPrice.setStdPriceFromInt(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_STD_PRICE)));
        productPrice.setPriceLimitFromInt(c.getInt(c.getColumnIndex(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIMIT)));
        productPrice.setProduct(product);

        c.close();

        return productPrice;
    }

    /*
    * Updating a product price
    */
    public int updateProductPrice(ProductPrice productPrice) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIST_VERSION_ID, productPrice.getPriceListVersionID());
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_STD_PRICE, productPrice.getIntegerStdPrice());
        values.put(ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRICE_LIMIT, productPrice.getIntegerPriceLimit());

        // updating row
        return db.update(Tables.TABLE_PRODUCT_PRICE, values, ProductPriceContract.ProductPriceDB.COLUMN_NAME_PRODUCT_PRICE_ID + " = ?",
                new String[] { String.valueOf(productPrice.getProductPriceID()) });
    }

}
