import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleClient
{
    private static Character[] packetList;
    private static ArrayList<Integer> missingPacketInts;
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

        missingPacketInts = new ArrayList<>();

        try
                (
                    Socket clientSocket = new Socket(hostName, portNumber);
                    PrintWriter requestWriter = // stream to write text requests to server
                        new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader responseReader= // stream to read text response from server
                        new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
                    BufferedReader stdIn = // standard input stream to get user's requests
                        new BufferedReader(
                            new InputStreamReader(System.in))
                )
        {
            boolean messageReceived = false;
            int totalMessages;
            String serverResponse = responseReader.readLine(); //if this idles, just initialize this to ""
			System.out.println("Ready to receive the packages? enter 'y' if yes: ");
			if (stdIn.readLine().equals("y"))
			{
			    while (!messageReceived)
                {
                    while (!serverResponse.equals(Message.ALL_SENT.toString()))
                    {
                        //serverResponse = responseReader.readLine();
                        //break down response into three parts
                        packetList = new Character[serverResponse.length()]; //it's going to ACTUALLY BE TOTAL INT
                        //update and fill array here at proper index
                        //remove from missing packet ints if found
                    }
                    for (int i = 0; i < packetList.length; i++)
                    {
                        if (packetList[i] == null)
                        {
                            requestWriter.println(i);
                            if (!missingPacketInts.contains(i))
                                missingPacketInts.add(i);
                        }
                    }
                    messageReceived = true;
                }
                /*
			    while ((userInput = stdIn.readLine()) != null)
			    {
                    requestWriter.println(userInput); // send request to server
                    serverResponse = responseReader.readLine();
                    System.out.println("SERVER RESPONDS: \"" + serverResponse + "\"");
                }
                 */
            }
			else
            {
                System.out.println("Okay. Not sending packages now.");
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
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
