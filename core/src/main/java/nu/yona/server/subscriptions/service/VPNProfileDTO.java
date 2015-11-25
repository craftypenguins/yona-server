/*******************************************************************************
 * Copyright (c) 2015 Stichting Yona Foundation This Source Code Form is subject to the terms of the Mozilla Public License, v.
 * 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package nu.yona.server.subscriptions.service;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import nu.yona.server.subscriptions.entities.User;

@JsonRootName("vpnProfile")
public class VPNProfileDTO
{
	private final UUID id;
	private UUID loginID;
	private String password;

	public VPNProfileDTO(UUID id, UUID loginID, String password)
	{
		this.id = id;
		this.loginID = loginID;
		this.password = password;
	}

	@JsonCreator
	public VPNProfileDTO(@JsonProperty("loginID") UUID loginID)
	{
		this(null, loginID, null);
	}

	@JsonIgnore
	public UUID getID()
	{
		return id;
	}

	public UUID getLoginID()
	{
		return loginID;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public static VPNProfileDTO createInstance(User user)
	{
		return new VPNProfileDTO(user.getAnonymized().getID(), user.getLoginID(), user.getPassword());
	}
}
