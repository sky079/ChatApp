import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 1234;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server is running on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Broadcast a message to all clients except the sender
    public static synchronized void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    // Add a client to the active clients list
    public static synchronized void addClient(ClientHandler client) {
        clients.add(client);
        System.out.println("Now " + clients.size() + " user(s) connected.");
    }

    // Remove a client from the active clients list
    public static synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }
    public static synchronized List<ClientHandler> getClients() {
        return new ArrayList<>(clients); // Return a copy to avoid modification
    }
    public static synchronized boolean isUsernameTaken(String name) {
        for (ClientHandler client : clients) {
            if (client.getClientName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }


}
