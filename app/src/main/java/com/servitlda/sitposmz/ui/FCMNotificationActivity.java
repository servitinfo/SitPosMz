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
package com.servitlda.sitposmz.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.servitlda.sitposmz.R;
import com.servitlda.sitposmz.fcm.BXPOSNotificationCode;
import com.servitlda.sitposmz.ui.fragment.AsyncFragment;

public class FCMNotificationActivity extends AppCompatActivity implements AsyncFragment.ParentActivity {

    private AsyncFragment mAsyncFragment;
    private static final String ASYNC_FRAGMENT_TAG = "FCM_ASYNC_FRAGMENT_TAG";

    private String clickAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmnotification);

        clickAction = getIntent().getAction();

        mAsyncFragment = (AsyncFragment) getSupportFragmentManager().findFragmentByTag(ASYNC_FRAGMENT_TAG);
        if (mAsyncFragment == null) {
            mAsyncFragment = new AsyncFragment();
            getSupportFragmentManager().beginTransaction().add(mAsyncFragment, ASYNC_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void handleReadDataTaskFinish(boolean success) {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (BXPOSNotificationCode.MANDATORY_UPDATE_ACTION.equals(clickAction)) {
            mAsyncFragment.runAsyncTask();
        } else if (BXPOSNotificationCode.RECOMMENDED_UPDATE_ACTION.equals(clickAction)) {
            onBackPressed();
        } else {
            onBackPressed();
        }
    }
}
