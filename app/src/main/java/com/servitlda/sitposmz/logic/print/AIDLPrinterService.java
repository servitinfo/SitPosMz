package com.servitlda.sitposmz.logic.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;



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

public class AIDLPrinterService extends AbstractPOSPrinterService {

    private static final String TAG = "AIDLPrinterService";
    private static int MAX_DATA_TO_WRITE_TO_STREAM_AT_ONCE = 1024;

    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;


    public AIDLPrinterService (Activity mActivity, String printerName) {
        super(mActivity, printerName);

        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        mActivity.startService(intent);//启动打印服务
        mActivity.bindService(intent, connService, Context.BIND_AUTO_CREATE);

    }

    // Method that finds a bluetooth printer device
    public void findDevice(String printerName) {

        try {
            woyouService.printerSelfChecking(callback);//这里使用的AIDL方式打印
            Log.i(TAG, "Check");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    public void openConnection() throws IOException {
        try {
            Log.i(TAG, "Check");

        } catch (Exception e) {
            Log.e(TAG, "Check", e);
        }
    }

    // Method that sends the text to be printed by the bluetooth printer
    public void sendData(byte[] out) {


        if (isConnected() == true) {

            try {
                woyouService.sendRAWData(out,callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


    }


    // Close the connection to bluetooth printer to avoid battery consumption
    public void closeConnection() throws IOException {
        try {
            stopWorker = true;
            Log.i(TAG, "Check");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isConnected() { return true; }


    public IWoyouService woyouService;

    public ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    final ICallback callback = new ICallback.Stub() {

            @Override
            public void onRunResult(boolean success) throws RemoteException {
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
            }

            @Override
            public void onRaiseException(int code, final String msg)
                    throws RemoteException {
            }
        };

    }

