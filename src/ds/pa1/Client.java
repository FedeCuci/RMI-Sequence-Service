package ds.pa1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Code for the client side. This will run n-1 times, on ranks 1 .. n (where n
 * is the number of nodes passed to srun
 */
public class Client {
	static final Logger logger = LoggerFactory.getLogger(Client.class);

// Establishes a connection to the server's remote object.
private ServerInterface connect() {
	String host = Util.getCoordinatorHostname(); // Retrieves the hostname of the coordinator (server).
	// Prints a message indicating the client is connecting to the specified host.
	System.err.println("client connecting to " + host);

	// Add retry logic - try up to 5 times with a 2-second delay between attempts
	for (int attempt = 1; attempt <= 5; attempt++) {
		try {
			Registry registry = LocateRegistry.getRegistry(host); //Get a reference to the RMI registry on the specified host
			ServerInterface serverInterface = (ServerInterface) registry.lookup("ServerInterface"); //Looks up the remote object bound to the name "ServerInterface" in the registry
			System.out.println("Successfully connected to server on attempt " + attempt);
			return serverInterface;
		} catch (Exception e) {
			System.err.println("Connection attempt failed: ");
			if (attempt < 5) {
				try {
					System.err.println("Retrying in 2 seconds...");
					Thread.sleep(2000); // Pause for 2 seconds before retrying
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt(); // Restore the interrupted status of current thread
				}
			} else {
				System.err.println("All connection attempts failed");
				e.printStackTrace(); // Print the stack trace of the exception
				throw new RuntimeException("Failed to connect to server after " + attempt + " attempts", e);
			}
		}
	}

	// Returns null if the connection fails.
	return null;
}
	public void start() throws RemoteException {
        logger.info("Client started on host {} master = {}", Util.getMyHostname(), Util.getCoordinatorHostname());

		ServerInterface serverInterface = connect();

		// Check if the connection was successful.
		if (serverInterface == null) {
			logger.error("Failed to connect to the server after multiple attempts.");
			System.exit(1);
		}

		// Call the getSequenceNumber method on the server 100 times.
		for (int i = 0; i < 100; i++) {
			serverInterface.getSequenceNumber();
		}

		try {
			System.out.println("Waiting at barrier for other clients to be ready...");
			serverInterface.barrier(); // call the barrier method on the server.
			System.out.println("Barrier passed, starting performance measurement");
		} catch (RemoteException e) {
			logger.error("Error at barrier synchronization: ", e);
			System.exit(1);
		}

		long start = System.nanoTime(); // Get the current time in nanoseconds.
		for (int i = 0; i < ClientServer.getNrSequenceNumberCalls(); i++) {
			serverInterface.getSequenceNumber(); // Call the getSequenceNumber method on the server.
		}
		long end = System.nanoTime();

		serverInterface.setDone(end - start); // Tell server that the client is done.

        logger.info("Client {} done", Util.getMyHostname());
	}

	// Start the client
	public static void main(String[] args) throws RemoteException {
		new Client().start();
	}
}
