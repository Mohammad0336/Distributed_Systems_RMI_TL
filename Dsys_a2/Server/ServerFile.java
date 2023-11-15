import java.rmi.Naming;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFile {

    public static void main(String[] args) {
        try {
            ListServiceImpl listService = new ListServiceImpl();

            // Thread pool with a fixed number of threads
            int numThreads = 10; // You can adjust this based on your requirements
            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

            // Register the service with the RMI registry
            String path = "rmi://localhost/ListServer";
            Naming.rebind(path, listService);

            System.out.println("Server is ready to accept requests...");

            // Wait for the user to press Enter before shutting down the server
            System.out.println("Press Enter to exit.");
            System.in.read();

            // Shutdown the executor service before exiting
            executorService.shutdown();
            System.out.println("Server has been shut down.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
