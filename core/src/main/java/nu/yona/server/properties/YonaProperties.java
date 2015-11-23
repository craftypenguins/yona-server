package nu.yona.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("yona")
@Configuration
public class YonaProperties
{
	private int newDeviceRequestExpirationDays = 1;

	public int getNewDeviceRequestExpirationDays()
	{
		return newDeviceRequestExpirationDays;
	}

	public void setNewDeviceRequestExpirationDays(int newDeviceRequestExpiration)
	{
		this.newDeviceRequestExpirationDays = newDeviceRequestExpiration;
	}

	private boolean isRunningInTestMode = false;

	public boolean getIsRunningInTestMode()
	{
		return isRunningInTestMode;
	}

	public void setIsRunningInTestMode(boolean isRunningInTestMode)
	{
		this.isRunningInTestMode = isRunningInTestMode;
	}
}