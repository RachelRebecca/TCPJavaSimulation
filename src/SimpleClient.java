import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * @author Chana Rosenbluth and Rachel Nemesure
 *
 * Simulates receiving packets from the server
 * It keeps accepting the packets and arranging them in their proper order
 * until all packets have been received by client
 */
public class SimpleClient
{
    private static Character[] packetList;
    private static int firstIndexReceived = -1; // used to handle a case in which all packets are dropped
    private static int roundNum = 0; // used to count number of rounds until message is sent to completion

    public static void main(String[] args) throws IOException
    {
        
		// Hardcode in IP and Port here if required
    	//args = new String[] {"127.0.0.1", "30121"};
    	
        if (args.length != 2 || !isInteger(args[1]))
        {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try
                (
                    Socket clientSocket = new Socket(hostName, portNumber);
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    BufferedReader stdIn = // standard input stream to get user's requests
                        new BufferedReader(new InputStreamReader(System.in))
                )
        {
            // get user prompt
            System.out.println("Would you like to receive the packets now? Enter 'y' if yes: ");
            String userInput = stdIn.readLine();

            // if don't want packets, send DO_NOT_SEND packet
            if (!userInput.equals("y") && !userInput.equals("Y"))
            {
                System.out.println("OK, packets will not be sent now.");
                objectOutputStream.writeObject(new Packet(Message.DO_NOT_SEND));
                System.exit(0);
            }

            // otherwise:
            System.out.println("OK, sending packets now.");

            // boolean to keep track of whether the client has all the packets
            boolean messageReceived = false;

            // send READY packet
            Packet allReady = new Packet(Message.READY);
            objectOutputStream.writeObject(allReady);

            Packet serverResponse = (Packet) objectInputStream.readObject(); //this is the first message received
            packetList = new Character[serverResponse.getTotalPacketsNumber()]; // create empty array to hold anticipated number of characters

            // allows us to confirm that at least one packet has been received
            if (serverResponse.getPacketNumber() != null)
            {
                firstIndexReceived = serverResponse.getPacketNumber();
            }

            while (!messageReceived)
            {
                roundNum++; //increase each round it takes until message is received to completion
                System.out.println("\nRound number " + roundNum + ": attempting to receive message from server");
                // while server is still sending characters:
                while (!serverResponse.getMessage().equals(Message.ALL_SENT))
                {
                    int packetNumber = serverResponse.getPacketNumber();
                    Character character = serverResponse.getCharacter();
                    packetList[packetNumber] = character;
                    System.out.println("Received packet character " + serverResponse.getCharacter()); // for the purpose of monitoring
                    serverResponse = (Packet) objectInputStream.readObject();   // wait for a new packet to arrive
                }

                // once ALL_SENT has been received, check to see which indices are still null
                ArrayList<Integer> missingInts = new ArrayList<>();
                for (int i = 0; i < packetList.length; i++)
                {
                    if (packetList[i] == null)
                    {
                        missingInts.add(i);
                    }
                }
                Integer[] intArray = new Integer[missingInts.size()];
                for (int i=0; i< missingInts.size(); i++)
                {
                    intArray[i] = missingInts.get(i);
                }

                // send request for server to send missing packets
                objectOutputStream.writeObject(new Packet(intArray));

                // not missing any packets AND it's not because we never got any packets (and thus we don't know how many to expect)
                if (missingInts.size() == 0 && firstIndexReceived != -1)
                {
                    messageReceived = true;
                    objectOutputStream.writeObject(new Packet(Message.ALL_RECEIVED));
                }
                else
                {
                    // wait for another packet
                    serverResponse = (Packet) objectInputStream.readObject();
                }
            }

            // print message to user
            System.out.println();
            System.out.println("The message: ");
            for (Character packetChar : packetList)
            {
                System.out.print(packetChar);
            }
            System.out.println();
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check if arg is an integer
     * @param arg (String)
     * @return boolean if arg is an integer
     */
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
