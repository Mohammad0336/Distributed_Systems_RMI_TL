import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

// ClientFunctions class
public class ClientFunctions {
    // function for registration 
    public static void registerUser(PrintWriter out, BufferedReader in, Scanner scanner) {
        try {
            // prompt to get user to input a username and password to later use in other functions
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            out.println(username); // sends username to server
            out.println(password); // sends password to server

            // Handle registration response from the server
            String response = in.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function for logging in to server
    public static void signInUser(PrintWriter out, BufferedReader in, Scanner scanner) {
        try {
            out.println("signin"); // Sends the "sign in" response to the server

            // Prompt to get the user to input a username and password to later use in other functions
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            out.println(username); // Sends the username to the server
            out.println(password); // Sends the password to the server

            // Handle sign-in response from the server
            String response = in.readLine();
            System.out.println("Server response: " + response);

            if (response.equals("Sign-in failed. Invalid username or password.")) {
                System.out.println("Exiting the client application.");
                System.exit(0); // Exit the client application
            } else if (response.contains("User data file does not exist.")) {
                System.out.println("Exiting the client application.");
                System.exit(0); // Exit the client application
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function for adding / removing values to a specific user's list
    public static void updateUserList(PrintWriter out, BufferedReader in, String action, String item, String deadline) {
        // Send the request to update the user's list to the server
        out.println("updateList");

        out.println(action); // add or remove an action to manipulate a user list
        out.println(item); // item that is being manipulated in the list
        out.println(deadline); // deadline for the item being implemented in the list

        try { // try-catch block to send server response and catch errors
            String response = in.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function for displaying User List
    public static void displayUserList(PrintWriter out, BufferedReader in) {
        // Send the request to display the user list to the server        
        out.println("display");
        try {
            String response = in.readLine();
            System.out.println("Server response: \n" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadSortedUserList(PrintWriter out, BufferedReader in) {
        out.println("download");

        try {
            String response = in.readLine();
            System.out.println("Server response: " + response);

            if (response.equals("Sorted list downloaded successfully.")) {
                System.out.println("Sorted list downloaded successfully. Check your client directory for the file.");
            } else {
                System.out.println("Failed to download the sorted list.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

