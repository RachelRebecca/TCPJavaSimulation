/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.net.*;
import java.io.*;

public class EchoServer
{
    public static void main(String[] args) throws IOException //excepts arguments
    {

        if (args.length != 1)
        {
            System.err.println("Usage: java source_code.EchoServer <port number>");
            System.exit(1);
        }

        //should have some sort of validation
        int portNumber = Integer.parseInt(args[0]);

        try
            // certain resources are taken from the OS and object is able to use them.
            // But object needs to be told when it's no longer in use.
            // Garbage collector causes memory to be free when port's no longer in use,
            // but what tells OS that port is now available for use?
            // This process is called disposal.
            // This try with resources block: when try is done,
            // the dispose method is called automatically.
            // access 2 streams (out and in) - can send a message to another computer
            // and receive a message from another computer
                (
                        ServerSocket serverSocket =
                                new ServerSocket(Integer.parseInt(args[0]));
                        // like the host, not looking for someone specific, but wait for someone to send to it.
                        // The port is the application, every app has a port
                        // Server is like a parking lot - each person in apartment has one spot -
                        // the client is the one who has the car and drives to address and parks in his spot,
                        // and he must park in his own spot - whenever trying to establish the connection it's at a port.
                        // The server socket has to say, I'm accepting messages coming at this particular port (args[0])
                        // but any computer can send messages to this port.
                        // Creating a server socket creates a sort of plug in. When something plugs in, a client has plugged
                        // - no specifically which client is plugging in ("plugging in" happens via the internet)

                        Socket clientSocket = serverSocket.accept();
                        // accept method accepts what's coming to the server socket and returns a socket
                        // A client computer sends something to this socket. We're not specifying the computer
                        // As long as you are actually something, we'll call this the client.
                        PrintWriter out =
                                new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        // out sends something to output stream to the client socket
                        // in reads from the client
                )
        //everything in parenthesis is treated like a resource,
        // so we don't have to manually dispose all these things to free them when we're done
        {
            String inputLine;
            while ((inputLine = in.readLine()) != null)//use the in stream
            // as long as you receive that message, spit it back out to the other computer.
            {
                out.println(inputLine);
            }
        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}

