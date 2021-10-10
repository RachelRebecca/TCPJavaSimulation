import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class SimpleServer
{
	static Random rnd = new Random();	// use this later for the dropping loop

	public static void main(String[] args) throws Exception
	{

		// Hard code in port number if necessary:
		//args = new String[] { "30121" };
		
		if (args.length != 1 || !isInteger(args[0]))
		{
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		// create packets
		String message = "Hello "; // I changed this message
		Integer messageLength = message.length();

		// Creating array of packets
		ArrayList<Packet> packets = new ArrayList<>();

		// Copy character by character into array
		for (int i = 0; i < messageLength; i++)
		{
			packets.add(new Packet(message.charAt(i), i, messageLength));
		}

		// create all sent packet
		Packet allSent = new Packet(Message.ALL_SENT);
		allSent.setTotalPacketsNumber(messageLength);

		try (
						ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
						Socket clientSocket = serverSocket.accept();
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
						ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())
				)
		{
			// wait for initial request
			Packet clientRequest = (Packet) objectInputStream.readObject();

			if (clientRequest.getMessage() == Message.READY)
			{
				// send all packets with dropping probability
				for (Packet packet : packets)
				{
					if (rnd.nextInt(100) >= 20)
					{
						objectOutputStream.writeObject(packet);
					}
				}

				// send confirmation (always sent)
				objectOutputStream.writeObject(allSent);
			}
			else
			{
				System.exit(1);
			}

			while ((clientRequest = (Packet) objectInputStream.readObject()).getMessage() != Message.ALL_RECEIVED)
			{
				if (clientRequest.getRequestedPacketsNumbers() != null)
				{
					Integer[] packetsToSend = clientRequest.getRequestedPacketsNumbers();
					for (Integer packetNumber : packetsToSend)
					{
						// send those packet numbers with dropping probability
						if (rnd.nextInt(100) >= 20)
						{
							objectOutputStream.writeObject(packets.get(packetNumber));
						}
					}
				}
				else
				{
					// todo: Should this exit instead??? Something will probably have gone wrong
					// send all packets with dropping probability
					for (Packet packet : packets)
					{
						if (rnd.nextInt(100) >= 20)
						{
							objectOutputStream.writeObject(packet);
						}
					}
				}

				// send confirmation (always sent)
				objectOutputStream.writeObject(allSent);
			}
		}
		catch (Exception e)
		{
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}

	}

	private static boolean isInteger(String arg)
	{
		boolean isInteger = true;
		try
		{
			Integer.parseInt(arg);
		}
		catch (Exception e)
		{
			isInteger = false;
		}
		return isInteger;
	}

}
