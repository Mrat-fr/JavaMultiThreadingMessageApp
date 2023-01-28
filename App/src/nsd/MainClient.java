package nsd;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.*;

public class MainClient {
    public static void main(String[] args) throws IOException {

        String hostName = "localhost";
        int portNumber = 12345;
        String User = null;

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("-----------------------------------------");
            System.out.println("|              Message App              |");
            System.out.println("|          /com (All Commands)          |");
            System.out.println("-----------------------------------------");
            String userInput;
            System.out.print("/");
            while ((userInput = stdIn.readLine()) != null) {
                Request req = null;
                Scanner sc = new Scanner(userInput);
                String data = null;//command info
                String command =  sc.next();//read first bit of line
                try {
                    switch (command) {//go to the command the user put
                        case "log":
                            data = sc.skip(" ").nextLine();
                            req = new RequestOpen(data);
                            User = data;
                            break;
                        case "say":
                            data = sc.skip(" ").nextLine();
                            req = new RequestPublish(data, User);
                            break;
                        case "search":
                            data = sc.skip(" ").nextLine();
                            req = new RequestSearch(data, User);
                            break;
                        case "sub":
                            data = sc.skip(" ").nextLine();
                            req = new RequestSubscribe(data, User);
                            break;
                        case "unsub":
                            data = sc.skip(" ").nextLine();
                            req = new RequestUnsubscribe(data, User);
                            break;
                        case "read":
                            data = sc.skip(" ").nextLine();
                            req = new RequestGet(data, User);
                            break;
                        case "quit":
                            req = new RequestQuit();
                            break;
                        case "com":
                            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------|");
                            System.out.println("|                                                                    (Commands)                                                                    |");
                            System.out.println("| Login              |  Subscribe         | Message            |  ReadAll           | Unsubscribe        | Search             |  Exit              |");
                            System.out.println("| log(Username)      |  sub(ChannelName)  | say(Message)       |  read(Timestamp)   | unsub(ChannelName) | search(Word)       |  quit              |");
                            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------|");
                            System.out.print("/");
                            continue;
                        default:
                            System.out.println("Unknown Command");
                            System.out.print("/");
                            continue;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("ILLEGAL COMMAND");
                    continue;
                }

                //response
                out.println(req);//take in response from server
                String serverResponse;

                //if there is no response
                if ((serverResponse = in.readLine()) == null)
                    break;


                Object json = JSONValue.parse(serverResponse);
                Response resp;

                //if everything was successful
                if (ResponseSuccess.fromJSON(json) != null){
                    System.out.print("/");
                    continue;
                }


                //print out message history or search
                if ((resp = ResponseMessageList.fromJSON(json)) != null) {
                    String Type = ((ResponseMessageList)resp).getType();
                    //display for search or read
                    if (Type.equals("Search")){
                        System.out.println("Search Results:");
                    } else{
                        System.out.println("History:");
                    }

                    for (Message m : ((ResponseMessageList)resp).getMessages())
                        System.out.println(m);

                    System.out.print("/");
                    continue;
                }

                if ((resp = ResponseError.fromJSON(json)) != null) {
                    System.out.println(((ResponseError)resp).getError());
                    System.out.print("/");
                    continue;
                }

                System.out.println("PANIC: " + serverResponse + " parsed as " + json);
                System.out.print("/");
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
