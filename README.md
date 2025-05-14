Java Multi-Client Console Chat Application

📌 Overview

This is a console-based chat application that supports real-time communication between multiple clients using Java networking APIs. The system includes:

A server that handles multiple client connections concurrently.

Clients that can send and receive messages in real-time.

Simple commands like /users and /exit for interaction.

🛠️ Technologies Used

Java (JDK 17)

ServerSocket and Socket (for networking)

BufferedReader and PrintWriter (for efficient I/O)

Multithreading (for concurrent client handling)

🧩 System Design

1. Server

Initializes a ServerSocket on port 1234.

Listens continuously for new client connections.

Each connection spawns a new ClientHandler thread.

Maintains a synchronized list of active clients.

Broadcasts messages (excluding sender) and system events like join/leave.

2. ClientHandler (Per Client Thread)

Handles interaction with an individual client.

Enforces unique usernames.

Displays connected users upon joining.

Handles message broadcasting and special commands (/exit, /users).

Cleans up resources and notifies others when a user leaves.

3. Client

Connects to the server via Socket.

Reads and sends messages via BufferedReader and PrintWriter.

Spawns a thread to listen for messages from the server.

Formats message input/output with clear prompts (>).

🧪 How to Run

✅ Requirements

Java JDK 17+

IntelliJ IDEA / Terminal / Any IDE

🔹 Start the Server

javac Server.java ClientHandler.java
java Server

🔹 Start Clients (In Separate Terminals)

javac Client.java
java Client

Each client will:

Be prompted for a unique name.

See the list of already connected users.

Send and receive messages in real-time.

Use /users to view online users and /exit to leave the chat.
