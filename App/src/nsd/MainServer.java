package nsd;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.*;

public class MainServer {

    static class ClientHandler extends Thread {

        static class Clock {//acts as a clock to keep track of when and what order the message was sent
            private long t;

            public Clock() { t = 0; }

            // tick the clock and return the current time
            public synchronized long tick() { return ++t; }//clock is synced up with every client
        }

        private static List<Message> board = new ArrayList<Message>();//store every message

        private static Clock clock = new Clock();

        private String user;//store username

        private List<String> Subscription = new ArrayList<String>();//store what channels user are subbed to!

        private static List<String> Users = new ArrayList<String>();//seen by all clients

        private static List<String> UsersOnline = new ArrayList<String>();//seen by all clients

        private Socket client;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            client = socket;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            user = null;
            board.clear();
            Users.clear();
            Users();
        }

        public void Users(){//get all the users
            String filePath = "C:\\Users\\mratf\\Desktop\\huh\\src\\nsd\\Users.txt";
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                        Users.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void AddUser(String User){//add a new user to the list
            String filePath = "C:\\Users\\mratf\\Desktop\\huh\\src\\nsd\\Users.txt";
            try {
                File file = new File(filePath);
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write(User);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void MessageHistory(){//take all the messages
            String filePath = "C:\\Users\\mratf\\Desktop\\huh\\src\\nsd\\MessageHistory.txt";
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    board.add(new Message(values[0], Long.parseLong(values[1]),values[2]));
                    clock.tick();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void MessageStore(Message M){
            String filePath = "C:\\Users\\mratf\\Desktop\\huh\\src\\nsd\\MessageHistory.txt";//change to fit your file path (Ctrl+Shit+C on file)
            try {
                File file = new File(filePath);
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write(M.getFrom()+","+M.getTimeStamp()+","+M.getBody());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    long ts = clock.tick();

                    System.out.println(inputLine);//show request

                    Object json = JSONValue.parse(inputLine);
                    Request req;

                    //login---------------------------------------------------------------------------------------------
                    if ((req = RequestOpen.fromJSON(json)) != null) {
                        String User = ((RequestOpen)req).getIdentity();
                        synchronized (ClientHandler.class) {//check there are no other users with the same name
                            if (UsersOnline.contains(User)) {
                                out.println(new ResponseError("There is already a " + User + " online!"));
                                continue;
                            }
                        }
                        if (user != null) {//check if the user is already logged in
                            out.println(new ResponseError("Already logged in!"));
                            continue;
                        }

                        Subscription.add(User);//add user to their own channel
                        user = User;//give this client a name
                        if (!Users.contains(User))
                            AddUser(User);
                        synchronized (ClientHandler.class) {//add user to online list
                            Users.add(User);
                        }
                        MessageHistory();//get message history
                        out.println(new ResponseSuccess());//Response that it worked
                        continue;
                    }

                    //Type----------------------------------------------------------------------------------------------
                    if (user != null && (req = RequestPublish.fromJSON(json)) != null) {//online work if logged on
                        String message = ((RequestPublish)req).getMessage();
                        synchronized (ClientHandler.class) {//add message to every user unless the message is too big
                            if(message.length() <= 50){
                                board.add(new Message(user, ts, message));
                                MessageStore(board.get(board.size() - 1));/////
                            }else{
                                out.println(new ResponseError("Message too big!"));
                                continue;
                            }
                        }
                        out.println(new ResponseSuccess());
                        continue;
                    }

                    //Search--------------------------------------------------------------------------------------------
                    if (user != null && (req = RequestSearch.fromJSON(json)) != null) {//search every message and see if contains a word
                        List<Message> msgs = new ArrayList<Message>();
                        String Search = String.valueOf(((RequestSearch)req).getSearch());
                        for (int i = 0; i < board.size(); i++){
                            Message Cmess = board.get(i);
                            String Mess = Cmess.getBody();
                            String User = Cmess.getFrom();
                            if(Mess.contains(Search) && Subscription.contains(User))
                                msgs.add(Cmess);
                        }
                        String Type = "Search";
                        out.println(new ResponseMessageList(msgs, Type));
                        continue;
                    }

                    //Subscribe-----------------------------------------------------------------------------------------
                    if (user != null && (req = RequestSubscribe.fromJSON(json)) != null) {
                        String channel = ((RequestSubscribe)req).getChannel();
                        if (!Users.contains(channel)) {//look if there is a user of search name online
                            out.println(new ResponseError("There is no " + channel + " channel!"));
                            continue;
                        }
                        if(!Subscription.contains(channel)){// see if they are already subbed
                            Subscription.add(channel);
                            out.println(new ResponseSuccess());
                        }else{
                            out.println(new ResponseError("Already subscribed to: "+channel+"!"));
                        }
                        continue;
                    }

                    //Unsubscribe---------------------------------------------------------------------------------------
                    if (user != null && (req = RequestUnsubscribe.fromJSON(json)) != null) {
                        String channel = ((RequestUnsubscribe)req).getChannel();
                            if (Subscription.contains(channel)){//look if user is subbed to them in the first place
                                Subscription.remove(channel);
                                out.println(new ResponseSuccess());
                            }else{
                                out.println(new ResponseError("You are not subscribed to: "+channel+"!"));
                            }
                        continue;
                    }

                    //Read----------------------------------------------------------------------------------------------
                    if (user != null && (req = RequestGet.fromJSON(json)) != null) {
                        List<Message> msgs = new ArrayList<Message>();
                        int After = Integer.valueOf(((RequestGet)req).getAfter());
                        for (int i = After; i < board.size(); i++){
                            Message Cmess = board.get(i);
                            String User = Cmess.getFrom();
                            if(Subscription.contains(User))//only get messages that users are subbed too
                                msgs.add(Cmess);
                        }
                        String Type = "Read";
                        out.println(new ResponseMessageList(msgs, Type));
                        continue;
                    }

                    //quit----------------------------------------------------------------------------------------------
                    if (RequestQuit.fromJSON(json) != null) {
                        if(user != null){
                            synchronized (Users) {//make the user "offline"
                                UsersOnline.remove(user);
                            }
                        }
                        in.close();//end connection
                        out.close();
                        return;
                    }

                    //commands before logging in
                    if (user == null) {
                        out.println(new ResponseError("Login First!"));
                        continue;
                    }

                    //command with invalid info
                    out.println(new ResponseError("Invalid Request!"));
                }
            } catch (IOException e) {//lost connection
                System.out.println("Exception while connected");
                System.out.println(e.getMessage());
            }
        }
    }


    public static void main(String[] args) {

        int portNumber = 12345;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            while (true) {
                System.out.println("Server started...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established with a client...");
                new ClientHandler(clientSocket).start();
                //new Thread(new ClientHandler(clientSocket)).start();

            }
        } catch (IOException e) {
            System.out.println("Exception listening for connection on port " + portNumber);//cant connect to the port number
            System.out.println(e.getMessage());
        }
    }
}