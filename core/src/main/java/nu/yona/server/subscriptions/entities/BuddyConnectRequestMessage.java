/*******************************************************************************
 * Copyright (c) 2015 Stichting Yona Foundation This Source Code Form is subject to the terms of the Mozilla Public License, v.
 * 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package nu.yona.server.subscriptions.entities;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import nu.yona.server.goals.entities.Goal;
import nu.yona.server.subscriptions.entities.BuddyAnonymized.Status;
import nu.yona.server.subscriptions.service.BuddyServiceException;

@Entity
public class BuddyConnectRequestMessage extends BuddyConnectMessage
{
	private boolean isRequestingSending;
	private boolean isRequestingReceiving;

	private Status status;

	// Default constructor is required for JPA
	public BuddyConnectRequestMessage()
	{
		super();
	}

	private BuddyConnectRequestMessage(UUID id, UUID userID, UUID userAnonymizedID, Set<UUID> goalIDs, String nickname,
			String message, UUID buddyID, boolean isRequestingSending, boolean isRequestingReceiving)
	{
		super(id, userAnonymizedID, userID, nickname, message, buddyID);

		if (userID == null)
		{
			throw BuddyServiceException.requestingUserCannotBeNull();
		}

		this.status = Status.REQUESTED;
		this.isRequestingSending = isRequestingSending;
		this.isRequestingReceiving = isRequestingReceiving;
	}

	public boolean requestingSending()
	{
		return isRequestingSending;
	}

	public boolean requestingReceiving()
	{
		return isRequestingReceiving;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Status getStatus()
	{
		return this.status;
	}

	public static BuddyConnectRequestMessage createInstance(UUID requestingUserID, UUID requestingUserUserAnonymizedID,
			Set<Goal> goals, String nickname, String message, UUID buddyID, boolean isRequestingSending,
			boolean isRequestingReceiving)
	{
		return new BuddyConnectRequestMessage(UUID.randomUUID(), requestingUserID, requestingUserUserAnonymizedID,
				goals.stream().map(g -> g.getID()).collect(Collectors.toSet()), nickname, message, buddyID, isRequestingSending,
				isRequestingReceiving);
	}

	@Override
	public boolean canBeDeleted()
	{
		return this.status == Status.ACCEPTED || this.status == Status.REJECTED;
	}
}
