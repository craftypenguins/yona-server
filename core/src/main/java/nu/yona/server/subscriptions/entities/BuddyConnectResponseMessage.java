/*******************************************************************************
 * Copyright (c) 2015 Stichting Yona Foundation
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package nu.yona.server.subscriptions.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Transient;

import nu.yona.server.crypto.Decryptor;
import nu.yona.server.crypto.Encryptor;
import nu.yona.server.messaging.entities.Message;

@Entity
public class BuddyConnectResponseMessage extends Message {

	@Transient
	private UUID respondingUserID;
	private byte[] respondingUserIDCiphertext;

	@Transient
	private String message;
	private byte[] messageCiphertext;

	@Transient
	private UUID buddyID;
	private byte[] buddyIDCiphertext;

	@Transient
	private UUID destinationID;
	private byte[] destinationIDCiphertext;
	private BuddyAnonymized.Status status = BuddyAnonymized.Status.NOT_REQUESTED;
	private boolean isProcessed;

	// Default constructor is required for JPA
	public BuddyConnectResponseMessage() {
		super(null, null);
	}

	private BuddyConnectResponseMessage(UUID id, UUID respondingUserID, UUID loginID, String message, UUID buddyID,
			UUID destinationID, BuddyAnonymized.Status status) {
		super(id, loginID);
		this.respondingUserID = respondingUserID;
		this.message = message;
		this.buddyID = buddyID;
		this.destinationID = destinationID;
		this.status = status;
	}

	public User getRespondingUser() {
		return User.getRepository().findOne(respondingUserID);
	}

	public String getMessage() {
		return message;
	}

	public UUID getBuddyID() {
		return buddyID;
	}

	public UUID getDestinationID() {
		return destinationID;
	}

	public BuddyAnonymized.Status getStatus() {
		return status;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed() {
		this.isProcessed = true;
	}

	public static BuddyConnectResponseMessage createInstance(UUID respondingUserID, UUID respondingUserLoginID,
			UUID destinationID, String message, UUID buddyID, BuddyAnonymized.Status status) {
		return new BuddyConnectResponseMessage(UUID.randomUUID(), respondingUserID, respondingUserLoginID, message,
				buddyID, destinationID, status);
	}

	@Override
	public void encrypt(Encryptor encryptor) {
		respondingUserIDCiphertext = encryptor.encrypt(respondingUserID);
		messageCiphertext = encryptor.encrypt(message);
		buddyIDCiphertext = encryptor.encrypt(buddyID);
		destinationIDCiphertext = encryptor.encrypt(destinationID);
	}

	@Override
	public void decrypt(Decryptor decryptor) {
		respondingUserID = decryptor.decryptUUID(respondingUserIDCiphertext);
		message = decryptor.decryptString(messageCiphertext);
		buddyID = decryptor.decryptUUID(buddyIDCiphertext);
		destinationID = decryptor.decryptUUID(destinationIDCiphertext);
	}
}