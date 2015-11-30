package nu.yona.server.exceptions;

/**
 * This exception is to be used in case user mobile number confirmation code is wrong.
 */
public class MobileNumberConfirmationException extends YonaException
{
	private static final long serialVersionUID = -7917208280838423613L;

	private MobileNumberConfirmationException(String messageId, Object... parameters)
	{
		super(messageId, parameters);
	}

	private MobileNumberConfirmationException(Throwable t, String messageId, Object... parameters)
	{
		super(t, messageId, parameters);
	}

	public static MobileNumberConfirmationException confirmationCodeMismatch()
	{
		return new MobileNumberConfirmationException("error.mobile.number.confirmation.code.mismatch");
	}

	public static MobileNumberConfirmationException confirmationCodeNotSet()
	{
		return new MobileNumberConfirmationException("error.mobile.number.confirmation.code.not.set");
	}

	public static MobileNumberConfirmationException mobileNumberAlreadyConfirmed()
	{
		return new MobileNumberConfirmationException("error.mobile.number.already.confirmed");
	}

	public static MobileNumberConfirmationException notConfirmed()
	{
		return new MobileNumberConfirmationException("error.mobile.number.not.confirmed");
	}
}