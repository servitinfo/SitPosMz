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
package com.servitlda.sitposmz.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import com.servitlda.sitposmz.R;
import com.servitlda.sitposmz.logic.model.pos.POSOrder;
import com.servitlda.sitposmz.logic.model.pos.PosProperties;

/**
 * Created by Diego Ruiz on 21/03/16.
 */
public class MultipleOrderTableDialogAdapter extends RecyclerView.Adapter<MultipleOrderTableDialogAdapter.MultipleOrderTableDialogViewHolder>
        implements View.OnClickListener {

    private ArrayList<POSOrder> mDataset;
    private View.OnClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MultipleOrderTableDialogViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtOrderId;
        public TextView txtTotal;

        public MultipleOrderTableDialogViewHolder(View v) {
            super(v);

            txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
            txtTotal = (TextView) itemView.findViewById(R.id.total_amount);
        }

        public void bindOrder(POSOrder order) {
            txtOrderId.setText(order.getOrderNo());
            NumberFormat currencyFormat = PosProperties.getInstance().getCurrencyFormat();
            txtTotal.setText(currencyFormat.format(order.getTotallines()));
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MultipleOrderTableDialogAdapter(ArrayList<POSOrder> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MultipleOrderTableDialogViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_order_table_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        v.setOnClickListener(this);

        return new MultipleOrderTableDialogViewHolder(v);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MultipleOrderTableDialogViewHolder holder, int position) {
        POSOrder order = mDataset.get(position);
        holder.bindOrder(order);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}