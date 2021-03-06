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

import java.util.ArrayList;

import com.servitlda.sitposmz.logic.model.pos.PosUser;

/**
 * Created by Diego Ruiz on 23/12/15.
 */
public class PosUserManagement extends AbstractObjectManagement {

    public PosUserManagement(Context ctx) {
        super(ctx);
    }

    @Override
    public PosUser get(long id){
        return dataMapper.getUser(id);
    }

    @Override
    public boolean remove(Object object) {
        return true;
    }

    /**
     * Get the user from the username
     * @param username
     * @return
     */
    public PosUser get(String username) {
        return dataMapper.getUser(username);
    }

    //return the usernames that have already logged in
    public ArrayList<String> getUsernameList() {
        return dataMapper.getUsernameList();
    }

    /**
     * Updates the info read from the server
     * - username to displauy
     * - user PIN
     */
    public boolean updateUserInfo(PosUser user) {
        return dataMapper.updateUserInfo(user);
    }

    public String getCurrentUserDisplayName() {
        return dataMapper.getCurrentUserDisplayName();
    }


}
