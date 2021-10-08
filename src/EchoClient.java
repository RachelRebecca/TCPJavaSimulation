package source_code;/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.io.*;
import java.net.*;

public class EchoClient
{
    public static void main(String[] args) throws IOException
    {

        if (args.length != 2)
        {
            System.err.println("Usage: java source_code.EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try
                (
                        Socket echoSocket = new Socket(hostName, portNumber);
                        //requires host's IP address (hostname), has to be the same port number as servers
                        //this becomes the client socket that is getting accepted by the server
                        PrintWriter out =
                                new PrintWriter(echoSocket.getOutputStream(), true);
                        BufferedReader in =
                                new BufferedReader(
                                        new InputStreamReader(echoSocket.getInputStream()));
                        // when a message comes from server into echo socket, read that input stream (a.k.a. message)
                        BufferedReader stdIn =
                                new BufferedReader(
                                        new InputStreamReader(System.in))
                        // standard input to read from the user,
                        // cuz we want the user to type in some commands which the client will then send it to server
                )
        {
            String userInput;
            while ((userInput = stdIn.readLine()) != null)
            // what user types is assigned to standard input using output stream
            // what you get back from server is displayed to the stream (it's our very message)
            {
                out.println(userInput);
                // which is the server's input which really the output from the client which is really the user's input
                // this is when it's sent to the server
                System.out.println("echo: " + in.readLine()); //read the next line coming in from the server
                //THE RESPONSE: output from server, input from client, out to the user
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}