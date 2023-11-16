import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListServiceImpl extends UnicastRemoteObject implements RemoteListService {

    public ListServiceImpl() throws RemoteException {
    	super();
    }


    @Override
    public String registerUser(String username, String password) throws RemoteException {
        String response;

        File file = new File("user_data.txt"); // database file

        synchronized (file) {
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    // Check if the username already exists
                    if (reader.lines().anyMatch(line -> line.startsWith("Username: " + username))) {
                        response = "Username already exists. Please choose a different username.";
                        return response; // Terminate the registration process
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    response = "Failed to register.";
                    return response;
                }
            }

            // Process and save user registration data
            String userData = "Username: " + username + "\nPassword: " + password;

            // Save user data to a file or database
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                try (FileWriter fileWriter = new FileWriter(file, true)) {
                    fileWriter.write(userData + "\n"); // Append user info to the existing content
                    response = "Registration successful: " + username;
                } catch (IOException e) {
                    e.printStackTrace();
                    response = "Failed to register.";
                }
            } catch (IOException e) {
                e.printStackTrace();
                response = "Failed to register.";
            }
        }

        return response;
    }

    @Override
    public String signInUser(String username, String password) throws RemoteException {
        String response = "Sign-in failed. Invalid username or password.";

        try {
            File file = new File("user_data.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isUserFound = false;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Username: " + username)) {
                        String storedPassword = reader.readLine();

                        if (storedPassword.equals("Password: " + password)) {
                            isUserFound = true;
                            System.out.println("User has appropriate credentials. " + username + " is now logged in");
                            response = "Sign-in successful. Welcome, " + username;
                            break;  // Break out of the loop since the user is found
                        }
                    }
                }

                if (!isUserFound) {
                    // response is already set to the default value
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = "Error reading the user data file: " + e.getMessage();
        }

        return response;
    }

    @Override
    public void updateUserList(String username, String action, String itemName, String itemDeadline) throws RemoteException {
        try {
            // Modify the DateTimeFormatter pattern to match "yyyyMMdd"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate deadline = LocalDate.parse(itemDeadline, formatter);

            if (action.equalsIgnoreCase("add")) {
                // Append item and deadline to the user's list file
                File userFile = new File(username + "_list.txt");
                FileWriter fileWriter = new FileWriter(userFile, true); // Append to file

                // Format the deadline in the desired "yyyyMMdd" format
                String formattedDeadline = deadline.format(formatter);
                fileWriter.write(itemName + " Due: " + formattedDeadline + "\n");

                fileWriter.close();
                System.out.println("Item added to the list for user: " + username);
            } else if (action.equalsIgnoreCase("remove")) {
                // Remove the item from the user's list file
                File userFile = new File(username + "_list.txt");
                File tempFile = new File(username + "_list_temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                boolean itemFound = false;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(itemName)) { // Check if the line starts with the item name
                        itemFound = true;
                    } else {
                        writer.write(line + "\n");
                    }
                }

                reader.close();
                writer.close();

                if (itemFound) { // if loop for a decision on how to handle item situations
                    // Rename the temporary file to the original user's list file
                    if (tempFile.renameTo(userFile)) {
                        System.out.println("Item removed from the list for user: " + username);
                    } else {
                        System.out.println("Failed to remove the item.");
                    }
                } else {
                    System.out.println("Item not found in the list.");
                }
            } else {
                System.out.println("Invalid action. Please use 'add' or 'remove'.");
            }
        } catch (DateTimeParseException | IOException e) {
            System.err.println("Error updating user list data: " + e.getMessage());
        }
    }

    @Override
    public List<String> displayUserList(String username) throws RemoteException {
        List<String> result = new ArrayList<>();

        try {
            File userFile = new File(username + "_list.txt");
            if (userFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                List<String> items = new ArrayList<>();

                String line;
                while ((line = reader.readLine()) != null) {
                    items.add(line);
                }

                // Sort the items by deadline using a custom Comparator
                Collections.sort(items, new DeadlineComparator());

                //result.add(username + "'s Items");
                System.out.println(username + "'s Items");
                for (String item : items) {
                    result.add(item); // Add each item to the result list
                    System.out.println(item); // Print to the server's console
                }

                reader.close();
            } else {
                result.add("Your list is empty.");
            }
        } catch (IOException e) {
            System.err.println("Error displaying user list: " + e.getMessage());
            result.add("Failed to display items.");
        }

        return result;
    }

    @Override
    public String downloadSortedUserList(String username) throws RemoteException {
        try {
            File userFile = new File(username + "_list.txt");
            StringBuilder response = new StringBuilder();

            if (userFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                List<String> items = new ArrayList<>();

                String line;
                while ((line = reader.readLine()) != null) {
                    items.add(line);
                }

                // Sort the items by deadline using a custom Comparator
                Collections.sort(items, new DeadlineComparator());

                // Specify the absolute path for the sorted list directory
                String absolutePath = "../Client/"; // Change this to your desired path

                // Create a new file for the sorted list in the specified directory
                File sortedFile = new File(absolutePath + username + "_sorted_list.txt");
                sortedFile.getParentFile().mkdirs(); // Create the necessary directories
                try (FileWriter fileWriter = new FileWriter(sortedFile)) {
                    for (String item : items) {
                        fileWriter.write(item + "\n");
                        response.append(item).append("\n");
                    }
                    response.append("Sorted list downloaded successfully.");
                } catch (IOException e) {
                    response.append("Failed to download the sorted list.");
                    e.printStackTrace();
                }
                reader.close();
            } else {
                response.append("Your list is empty.");
            }
            return response.toString();
        } catch (IOException e) {
            return "Failed to download the sorted list.\n" + e.getMessage();
        }
    }

    public static class DeadlineComparator implements Comparator<String> {
        @Override
        public int compare(String item1, String item2) {
            // Parse and compare the deadlines
            LocalDate deadline1 = parseDeadline(item1);
            LocalDate deadline2 = parseDeadline(item2);
            return deadline1.compareTo(deadline2);
        }
    
        private LocalDate parseDeadline(String item) {
            try {
                // Extract the deadline substring and parse it into a LocalDate
                String deadlineSubstring = item.substring(item.indexOf(" Due: ") + 6);
                return LocalDate.parse(deadlineSubstring, DateTimeFormatter.ofPattern("yyyyMMdd"));
            } catch (DateTimeParseException e) {
                // Handle parsing errors
                return LocalDate.MIN;
            }
        }
    }
}


