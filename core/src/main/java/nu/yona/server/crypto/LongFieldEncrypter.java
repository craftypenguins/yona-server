/*******************************************************************************
 * Copyright (c) 2015, 2016 Stichting Yona Foundation This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package nu.yona.server.crypto;

import java.util.Base64;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LongFieldEncrypter implements AttributeConverter<Long, String>
{
	@Override
	public String convertToDatabaseColumn(Long attribute)
	{
		return (attribute == null) ? null : Base64.getEncoder().encodeToString(CryptoUtil.encryptLong(attribute));
	}

	@Override
	public Long convertToEntityAttribute(String dbData)
	{
		try
		{
			if (dbData == null)
			{
				return null;
			}

			return CryptoUtil.decryptLong(Base64.getDecoder().decode(dbData));
		}
		catch (RuntimeException ex)
		{
			return null;
		}
	}
}
