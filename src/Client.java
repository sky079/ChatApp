import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Name prompt
            System.out.print(serverInput.readLine()); // "Enter your name: "
            String name = userInput.readLine();
            serverOutput.println(name);

            // Receive thread
            new Thread(() -> {
                String response;
                try {
                    while ((response = serverInput.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.err.println("Connection lost.");
                }
            }).start();

            // Input loop
            String input;
//            System.out.print("> ");
            while ((input = userInput.readLine()) != null) {
                serverOutput.println(input);
                if (input.equalsIgnoreCase("/exit")) break;
                System.out.print("> ");
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
