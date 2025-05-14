import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Ask for name until unique
            while (true) {
                writer.println("Enter your name: ");
                clientName = reader.readLine();

                if (clientName == null || clientName.trim().isEmpty()) {
                    writer.println("❌ Name cannot be empty.");
                } else if (Server.isUsernameTaken(clientName)) {
                    writer.println("❌ Name already taken. Try another.");
                } else {
                    break;
                }
            }

            // Line separator
            writer.println("---------------------------------");

            // Show list of already connected users
            if (!Server.getClients().isEmpty()) {
                writer.println("🟡 Currently connected users:");
                for (ClientHandler client : Server.getClients()) {
                    writer.println("- " + client.getClientName());
                }
            } else {
                writer.println("🟢 You are the first user.");
            }

            writer.println("👥 Total users now: " + (Server.getClients().size() + 1));
            writer.println("---------------------------------");

            Server.addClient(this);
            System.out.println(clientName + " has joined.");
            Server.broadcast(formatNotice("🔵 " + clientName + " joined the chat!"), this);

            String message;
            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("/exit")) break;

                if (message.equalsIgnoreCase("/users")) {
                    writer.println("👥 Online users:");
                    for (ClientHandler client : Server.getClients()) {
                        writer.println("- " + client.getClientName());
                    }
                    continue;
                }

                String time = new SimpleDateFormat("HH:mm").format(new Date());
                String formattedMessage = time + " [" + clientName + "]: " + message;
                System.out.println(formattedMessage);
                Server.broadcast(formattedMessage, this);
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}

            Server.removeClient(this);
            Server.broadcast(formatNotice("🔴 " + clientName + " left the chat."), this);
            System.out.println(clientName + " has left.");
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getClientName() {
        return clientName;
    }

    private String formatNotice(String text) {
        return "=== " + text + " ===";
    }
}
