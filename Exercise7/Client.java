import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client implements Runnable {

    private List<String> history = new ArrayList<String>();
    private MessageExchange messageExchange = new MessageExchange();
    private String host;
    private Integer port;
    public int id;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
        Random rnd = new Random();
        id = rnd.nextInt()%10;
    }


    public static void main(String[] args) {
        if (args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else {
            System.out.println("Connection to server...");
            String serverHost = args[0];
            Integer serverPort = Integer.parseInt(args[1]);
            Client client = new Client(serverHost, serverPort);
            new Thread(client).start();
            System.out.println("Connected to server: " + serverHost + ":" + serverPort);
            client.listen();
        }
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/chat?token=" + messageExchange.getToken(history.size()));
        return (HttpURLConnection) url.openConnection();
    }

    public List<String> getMessages() {
        List<String> list = new ArrayList<String>();
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection();
            connection.connect();
            String response = messageExchange.inputStreamToString(connection.getInputStream());
            JSONObject jsonObject = messageExchange.getJSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("messages");
            for (Object o : jsonArray) {
                System.out.println(o);
                list.add(o.toString());
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return list;
    }

    public void sendMessage(String message) {
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            byte[] bytes = messageExchange.getClientSendMessageRequest(message,Integer.toString(id)).getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            connection.getInputStream();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public void delMessage(String  num) {
        HttpURLConnection connection = null;
        try {
            String buf = history.get(Integer.parseInt(num));
            if(!buf.substring(0, buf.indexOf(":")).equals(Integer.toString(id))) {
                System.out.println("Cannot access");
                return;
            }
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("DELETE");

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            byte[] bytes = num.getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            connection.getInputStream();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public void putMessage(String message) {
        HttpURLConnection connection = null;
        try {
            String str[] = message.split("[\\ ]+",2);
            String buf = history.get(Integer.parseInt(str[0]));
            if(!buf.substring(0, buf.indexOf(":")).equals(Integer.toString(id))) {
                System.out.println("Cannot access");
                return;
            }
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("PUT");

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            byte[] bytes = messageExchange.getClientSendMessageRequest(str[1],str[0]).getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            connection.getInputStream();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void listen() {
        while (true) {
            List<String> list = getMessages();

            if (list.size() > 0) {
                history.addAll(list);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            if(message.length() > 3 && message.substring(0, 3).equals("del")) {
                delMessage(message.substring(3));
            } else if(message.length() > 3 && message.substring(0, 3).equals("put")) {
                putMessage(message.substring(3));
            } else {
                sendMessage(message);
            }
        }
    }
}
