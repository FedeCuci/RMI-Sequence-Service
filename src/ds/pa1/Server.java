package ds.pa1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * The main class of the server.
 * This class implements the server functionality for a distributed system.
 */
public class Server {
	// Create a logger instance for this class to log information and errors
	static final Logger logger = LoggerFactory.getLogger(Server.class);

	// Helper method to return a string representation of the number of clients
	private String nrClientsString() {
		int nrClients = Util.getNrClients();
		if (nrClients == 1) {
			return "1 client";
		}
		return nrClients + " clients";
	}

	/**
	 * Starts the server, initializes the server implementation and displays performance metrics.
	 * Creates a remote object and makes it available to clients.
	 */
	public void start() {
		// Log the server's hostname for diagnostics
        logger.info("server: my hostname = {}", Util.getMyHostname());

		try {
			// Set hostname explicitly
			System.setProperty("java.rmi.server.hostname", Util.getMyHostname());

			// Create an instance of the server implementation
			ServerImplementation remoteServer = new ServerImplementation();

			// Create a registry specific to the server
			Registry registry = LocateRegistry.createRegistry(1099);

			// Bind the remote object's stub in the registry
			registry.rebind("ServerInterface", remoteServer);

			System.err.println("Server ready");

			// Wait for all clients to complete
			while (remoteServer.getCompletedClients() < Util.getNrClients()) {
			    Thread.sleep(100);
			}

			// Calculate performance metrics for sequence number generation
			double aggregatedTime = remoteServer.getAggregatedTimeSequenceNumbers() / 1000.0; //
			long totalCalls = (long) Util.getNrClients() * ClientServer.getNrSequenceNumberCalls();
			double microsPerCall = aggregatedTime / totalCalls;

			// Display performance statistics to the console
			System.out.printf("Time per getSequenceNumber call with %s and %d calls = %.3f microseconds\n",
					nrClientsString(), totalCalls, microsPerCall);

			// Exit the application with success code
			System.exit(0);
		} catch (Exception e) {
			// Handle and log any exceptions that occur during server startup
			logger.error("Exception Occurred in the server: ", e);
		}
	}

	// Start the server
	public static void main(String[] args) {
		new Server().start();
	}
}