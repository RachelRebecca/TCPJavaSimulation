import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
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
		String message = "Hello user! Welcome to this program. I hope you’re doing well today!"; // I changed this message
		Integer messageLength = message.length();

		// Creating array of packets in order, plus an array to hold randomized ordered packets
		ArrayList<Packet> packets = new ArrayList<>();
		ArrayList<Packet> randomizedPackets = new ArrayList<>();

		// Copy character by character into array
		for (int i = 0; i < messageLength; i++)
		{
			packets.add(new Packet(message.charAt(i), i, messageLength));
		}

		// create all sent packet
		Packet allSent = new Packet(Message.ALL_SENT);
		allSent.setTotalPacketsNumber(messageLength);

		try
				(
					ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
					Socket clientSocket = serverSocket.accept();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())
				)
		{
			// wait for initial request
			Packet clientRequest = (Packet) objectInputStream.readObject();

			if (clientRequest.getMessage() == Message.DO_NOT_SEND)
			{
				System.exit(0);
			}

			if (clientRequest.getMessage() == Message.READY)
			{
				// randomize packet order
				randomizedPackets.addAll(packets);
				Collections.shuffle(randomizedPackets);

				// send all packets with dropping probability
				for (Packet packet : randomizedPackets)
				{
					if (rnd.nextInt(100) >= 20)
					{
						objectOutputStream.writeObject(packet);
						//System.out.println("Sent packet character " + packet.getCharacter()); for the purpose of monitoring
					}
				}

				// send confirmation (always sent)
				objectOutputStream.writeObject(allSent);

				// empty randomizedPackets
				randomizedPackets.clear();
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
					// add all packets to send to array in order to be randomized
					for (Integer packetNumber : packetsToSend)
					{
						randomizedPackets.add(packets.get(packetNumber));
					}

					Collections.shuffle(randomizedPackets);

					for (Packet packet : randomizedPackets)
					{
						// send those packet numbers with dropping probability
						if (rnd.nextInt(100) >= 20)
						{
							objectOutputStream.writeObject(packet);
							//System.out.println("Sent packet character " + packet.getCharacter()); for monitoring purposes
						}
					}
				}
				else
				{
					// something has gone wrong, so exit
					System.exit(1);
				}

				// send confirmation (always sent)
				objectOutputStream.writeObject(allSent);

				// empty randomizedPackets
				randomizedPackets.clear();
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
