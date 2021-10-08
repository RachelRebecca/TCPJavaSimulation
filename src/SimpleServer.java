import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SimpleServer
{

	static Random rnd = new Random();
	//static MagicEightBall magicEightBall = new MagicEightBall(rnd);
	
	public static void main(String[] args) throws IOException
	{

		// Hard code in port number if necessary:
		//args = new String[] { "30121" };
		
		if (args.length != 1 || !isInteger(args[0]))
		{
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		try
				(
				ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
				Socket clientSocket1 = serverSocket.accept();
				Socket clientSocket2 = serverSocket.accept();
				PrintWriter responseWriter1 = new PrintWriter(clientSocket1.getOutputStream(), true);
				BufferedReader requestReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
				PrintWriter responseWriter2 = new PrintWriter(clientSocket2.getOutputStream(), true);
				BufferedReader requestReader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));

				//if client 1 goes, then client 2 goes, client 2 cannot make progress until client 1 sends something else.
				)
		{
			String usersRequest;
			while ((usersRequest = requestReader1.readLine()) != null)
			{
				System.out.println("\"" + usersRequest + "\" received");
				//String response = magicEightBall.getNextAnswer();
				//System.out.println("Responding: \"" + response + "\"");
				//responseWriter1.println(response);
				usersRequest = requestReader2.readLine();
				System.out.println("\"" + usersRequest + "\" received");
				//response = magicEightBall.getNextAnswer();
				//System.out.println("Responding: \"" + response + "\"");
				//responseWriter2.println(response);
			}
		}
		catch (IOException e)
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
