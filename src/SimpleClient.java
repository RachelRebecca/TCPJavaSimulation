import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleClient
{
    private static Character[] packetList;
    private static int firstIndexReceived = -1;

    public static void main(String[] args) throws IOException
    {
        
		// Hardcode in IP and Port here if required
    	//args = new String[] {"127.0.0.1", "30121"};
    	
        if (args.length != 2 || !isInteger(args[1]))
        {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
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
            boolean messageReceived = false;

            Packet allReady = new Packet(Message.READY);
            objectOutputStream.writeObject(allReady);

            Packet serverResponse = (Packet) objectInputStream.readObject(); //this is the first message received
            packetList = new Character[serverResponse.getTotalPacketsNumber()];
            if (serverResponse.getPacketNumber() != null)
            {
                firstIndexReceived = serverResponse.getPacketNumber();
            }

            System.out.println("Ready to receive the packages? enter 'y' if yes: ");
            //todo: enter some stuff from user

            while (!messageReceived)
            {
                while (!serverResponse.getMessage().equals(Message.ALL_SENT))
                {
                    int packetNumber = serverResponse.getPacketNumber();
                    Character character = serverResponse.getCharacter();
                    packetList[packetNumber] = character;
                    System.out.println("Received packet character " + serverResponse.getCharacter());
                    serverResponse = (Packet) objectInputStream.readObject();
                }
                ArrayList<Integer> missingInts = new ArrayList<>();
                for (int i = 0; i < packetList.length; i++)
                {
                    if (packetList[i] == null)
                    {
                        //objectOutputStream.writeInt(i);
                        missingInts.add(i);
                    }
                }
                Integer[] intArray = new Integer[missingInts.size()];
                for (int i=0; i< missingInts.size(); i++)
                {
                    intArray[i] = missingInts.get(i);
                }
                objectOutputStream.writeObject(new Packet(intArray));

                //todo: check if no packet was sent
                if (missingInts.size() == 0 && firstIndexReceived != 1)
                {
                    messageReceived = true;
                    objectOutputStream.writeObject(new Packet(Message.ALL_RECEIVED));
                }
                else
                {
                    serverResponse = (Packet) objectInputStream.readObject();
                    //IS THIS WHAT SHOULD GO HERE? OTHERWISE I THINK IT GETS STUCK IN INFINITE FOR LOOP
                    //SERVER SIDE RAISES EXCEPTION AND THEN RECONNECTS - FIXED WITH ADDING LINE 81
                    //NOT SURE IF THIS IS BAD HERE THOUGH
                    //SEEMS TO GET CLIENT SIDE ALL PACKETS BUT NOT SURE
                }
            }

            for (int i=0; i<packetList.length; i++)
            {
               System.out.print(packetList[i]);
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
