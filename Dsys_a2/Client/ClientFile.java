import java.util.Scanner;
import java.rmi.Naming;

// Client class
public class ClientFile {
    private static String signedInUsername;
    public static void main(String[] args) {

        // declarations socket and scanner to receive user input
        Scanner scanner = new Scanner(System.in);

        try {
            
            String path = "rmi://localhost/ListServer";
            RemoteListService listService = (RemoteListService) Naming.lookup(path);

            System.out.println("Client Ready – remote stub active…");
            
            while (true) {
                // prompting user for sign in, register, or exit the program
                System.out.println("Would you like to register or sign in: ");
                System.out.println("Commands (register) (sign in) (quit)");

                String answer = scanner.nextLine();
                // Send the user's choice to the server
                // Use listService to invoke remote methods based on the user's choice
                if (answer.equals("register")) {
                    registerUser(listService, scanner);
                } else if (answer.equals("sign in")) {
                    signInUser(listService, scanner);
                    break;
                } else if (answer.equalsIgnoreCase("quit")) {
                    System.exit(0); // Exit the entire program
                } else {
                    System.out.println("Invalid action.");
                }
            }

            while (true) {
                // while statement for user response to edit user list or exit the application
                System.out.println("Do you want to add an item to your list, remove an item, " +
                                   "view your list, download sorted list, or exit");
                System.out.println("Commands: (add) (remove) (display) (download) (exit)");

                String answer = scanner.nextLine(); // item to be parsed

                // Use listService to invoke remote methods based on the user's choice
                if (answer.equalsIgnoreCase("add")) {
                    System.out.println("Enter a task you want to add:");
                    String item = scanner.nextLine();
                    System.out.println("Enter a deadline in (yyyyMMdd)"); // deadline for item
                    String deadline = scanner.nextLine();
                    listService.updateUserList(signedInUsername,"add", item, deadline);
                } else if (answer.equalsIgnoreCase("remove")) {
                    System.out.println("Enter the item you want to remove:");
                    String item = scanner.nextLine();
                    System.out.println("Enter the deadline in (yyyyMMdd)"); // deadline of the removed item
                    String deadline = scanner.nextLine();
                    listService.updateUserList(signedInUsername, "remove", item, deadline);
                    System.out.println("Removed: " + item);
                } else if (answer.equalsIgnoreCase("exit")) {
                    break;
                } else if (answer.equalsIgnoreCase("display")) {
                    // Display user list action
                    System.out.println("Your List:");
                    listService.displayUserList(signedInUsername);
                } else if (answer.equalsIgnoreCase("download")) {
                    System.out.println("Downloading sorted list...");
                    listService.downloadSortedUserList(signedInUsername);
                } else {
                    System.out.println("Invalid action."); // if the user does not make a proper request
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to register a user
    private static void registerUser(RemoteListService listService, Scanner scanner) {
        try {
            // prompt to get the user to input a username and password to later use in other functions
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            // Use listService to invoke remote registration method
            String response = listService.registerUser(username, password);
            System.out.println("Server response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to sign in a user
    private static void signInUser(RemoteListService listService, Scanner scanner) {
        try {
            // Prompt to get the user to input a username and password to later use in other functions
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            // Use listService to invoke remote sign-in method
            String response = listService.signInUser(username, password);
            System.out.println("Server response: " + response);

            // Store the signed-in username
            if (response.startsWith("Sign-in successful")) {
                signedInUsername = response.substring(response.lastIndexOf(" ") + 1);
            }

            if (response.equals("Sign-in failed. Invalid username or password.")) {
                System.out.println("Exiting the client application.");
                System.exit(0); // Exit the client application
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

