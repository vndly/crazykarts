package com.mauriciotogneri.crazykarts.util;

import java.net.InetAddress;
import com.mauriciotogneri.crazykarts.common.network.DatagramCommunication;
import com.mauriciotogneri.crazykarts.connection.ClientConnection;

public class ConnectionUtils
{
	public static void send(final ClientConnection connection, final byte[] message)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				connection.send(message);
			}
		});
		thread.start();
	}
	
	public static void send(final DatagramCommunication datagramCommunication, final InetAddress address, final int port, final byte[] message)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				datagramCommunication.send(address, port, message);
			}
		});
		thread.start();
	}
}