import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ListServiceImpl listService;

    public ClientHandler(Socket clientSocket, ListServiceImpl listService) {
        this.clientSocket = clientSocket;
        this.listService = listService;
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

            // Read the client's request and perform the corresponding action
            String request = (String) inputStream.readObject();

            if (request.equals("register")) {
                // Handle registration
                listService.registerUser(outputStream, inputStream, new Scanner(System.in));
            } else if (request.equals("signin")) {
                // Handle sign-in
                listService.signInUser(outputStream, inputStream, new Scanner(System.in));
            } else if (request.equals("updateList")) {
                // Handle updating user list
                String action = (String) inputStream.readObject();
                String item = (String) inputStream.readObject();
                String deadline = (String) inputStream.readObject();
                listService.updateUserList(outputStream, inputStream, action, item, deadline);
            } else if (request.equals("display")) {
                // Handle displaying user list
                listService.displayUserList(outputStream, inputStream);
            } else if (request.equals("download")) {
                // Handle downloading sorted user list
                listService.downloadSortedUserList(outputStream, inputStream);
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

