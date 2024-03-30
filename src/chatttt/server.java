//package chatttt;
//
//
//import java.io.*;
//import java.net.*;
//import java.sql.*;
//import java.util.*;
//
//public class ChatServer {
//    private Map<String, ObjectOutputStream> clients = new HashMap<>();
//    private Connection databaseConnection;
//
//    public ChatServer(int port) {
//        try {
//            databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/APP", "root", "");
//            System.out.println("Database connection established.");
//
//            ServerSocket serverSocket = new ServerSocket(port);
//            System.out.println("Server started on port " + port);
//
//            while (true) {
//                Socket socket = serverSocket.accept();
//                new ClientHandler(socket).start();
//            }
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private class ClientHandler extends Thread {
//        private Socket socket;
//        private ObjectInputStream inputStream;
//        private ObjectOutputStream outputStream;
//        private String username;
//
//        public ClientHandler(Socket socket) {
//            this.socket = socket;
//            try {
//                outputStream = new ObjectOutputStream(socket.getOutputStream());
//                inputStream = new ObjectInputStream(socket.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void run() {
//            try {
//                while (true) {
//                    String message;
//                    try {
//                        message = (String) inputStream.readObject();
//                    } catch (EOFException e) {
//                        System.out.println("Client disconnected.");
//                        break;
//                    }
//                    if (message.startsWith("JOIN")) {
//                        handleJoin(message);
//                    }
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        //login
//        private void handleJoin(String message) throws IOException {
//            String[] parts = message.split(" ", 3);
//            String username = parts[1];
//            String email = parts[2];
//            if (validateCredentials(username, email)) {
//                this.username = username;
//                outputStream.writeObject("LOGIN_SUCCESSFUL");
//                clients.put(username, outputStream);
//            ;
//
//                // Send conversation history for each contact
//            } else {
//                outputStream.writeObject("INVALID_CREDENTIALS");
//                outputStream.flush();
//            }
//        }
//        private boolean validateCredentials(String username, String email) {
//            try {
//                PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM login WHERE username=? AND email=?");
//                statement.setString(1, username);
//                statement.setString(2, email);
//                ResultSet resultSet = statement.executeQuery();
//                return resultSet.next();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//
//
//    public static void main(String[] args) {
//        new ChatServer(12345);
//    }
//}