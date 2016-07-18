/*******************************************************************************
 * Copyright (c) 2015, 2016 Stichting Yona Foundation This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package nu.yona.server.messaging.service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonRootName;

import nu.yona.server.analysis.entities.GoalConflictMessage;
import nu.yona.server.analysis.entities.GoalConflictMessage.Status;
import nu.yona.server.messaging.entities.DisclosureResponseMessage;
import nu.yona.server.messaging.entities.Message;
import nu.yona.server.messaging.service.MessageService.TheDTOManager;
import nu.yona.server.subscriptions.service.UserDTO;

@JsonRootName("disclosureResponseMessage")
public class DisclosureResponseMessageDTO extends BuddyMessageLinkedUserDTO
{
	private final Status status;

	private DisclosureResponseMessageDTO(UUID id, ZonedDateTime creationTime, boolean isRead, UserDTO user, Status status,
			String nickname, String message, UUID targetGoalConflictMessageID)
	{
		super(id, creationTime, isRead, targetGoalConflictMessageID, user, nickname, message);
		this.status = status;
	}

	@Override
	public String getType()
	{
		return "DisclosureResponseMessage";
	}

	@Override
	public Set<String> getPossibleActions()
	{
		Set<String> possibleActions = super.getPossibleActions();
		return possibleActions;
	}

	public Status getStatus()
	{
		return status;
	}

	@Override
	public boolean canBeDeleted()
	{
		return true;
	}

	public static DisclosureResponseMessageDTO createInstance(UserDTO actingUser, DisclosureResponseMessage messageEntity)
	{
		GoalConflictMessage targetGoalConflictMessage = messageEntity.getTargetGoalConflictMessage();
		return new DisclosureResponseMessageDTO(messageEntity.getID(), messageEntity.getCreationTime(), messageEntity.isRead(),
				UserDTO.createInstanceIfNotNull(messageEntity.getSenderUser()), messageEntity.getStatus(),
				messageEntity.getSenderNickname(), messageEntity.getMessage(), targetGoalConflictMessage.getID());
	}

	@Component
	private static class Manager extends MessageDTO.Manager
	{
		@Autowired
		private TheDTOManager theDTOFactory;

		@PostConstruct
		private void init()
		{
			theDTOFactory.addManager(DisclosureResponseMessage.class, this);
		}

		@Override
		public MessageDTO createInstance(UserDTO actingUser, Message messageEntity)
		{
			return DisclosureResponseMessageDTO.createInstance(actingUser, (DisclosureResponseMessage) messageEntity);
		}

		@Override
		public MessageActionDTO handleAction(UserDTO actingUser, Message messageEntity, String action,
				MessageActionDTO requestPayload)
		{
			switch (action)
			{
				default:
					return super.handleAction(actingUser, messageEntity, action, requestPayload);
			}
		}
	}
}
