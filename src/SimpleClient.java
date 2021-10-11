import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * @author Chana Rosenbluth and Rachel Nemesure
 */
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

            System.out.println("Would you like to receive the packets now? Enter 'y' if yes: ");
            String userInput = stdIn.readLine();
            if (!userInput.equals("y") && !userInput.equals("Y"))
            {
                System.out.println("OK, packets will not be sent now.");
                objectOutputStream.writeObject(new Packet(Message.DO_NOT_SEND));
                System.exit(0);
            }
            System.out.println("OK, sending packets now.");

            boolean messageReceived = false;

            Packet allReady = new Packet(Message.READY);
            objectOutputStream.writeObject(allReady);

            Packet serverResponse = (Packet) objectInputStream.readObject(); //this is the first message received
            packetList = new Character[serverResponse.getTotalPacketsNumber()];
            if (serverResponse.getPacketNumber() != null)
            {
                firstIndexReceived = serverResponse.getPacketNumber();
            }

            while (!messageReceived)
            {
                while (!serverResponse.getMessage().equals(Message.ALL_SENT))
                {
                    int packetNumber = serverResponse.getPacketNumber();
                    Character character = serverResponse.getCharacter();
                    packetList[packetNumber] = character;
                    //System.out.println("Received packet character " + serverResponse.getCharacter()); for the purpose of monitoring
                    serverResponse = (Packet) objectInputStream.readObject();
                }
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
                objectOutputStream.writeObject(new Packet(intArray));

                if (missingInts.size() == 0 && firstIndexReceived != -1)
                {
                    messageReceived = true;
                    objectOutputStream.writeObject(new Packet(Message.ALL_RECEIVED));
                }
                else
                {
                    serverResponse = (Packet) objectInputStream.readObject();
                }
            }

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
