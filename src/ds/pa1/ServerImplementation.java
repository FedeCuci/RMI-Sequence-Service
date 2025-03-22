package ds.pa1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the remote object (on the server side). 
 * TODO: YOU HAVE TO MODIFY AND EXTEND THIS FILE
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

	// Atomic integer to ensure thread safety when generating sequence numbers
	private final AtomicInteger sequenceNumber = new AtomicInteger(0);

	// Barrier to synchronize all clients
	private final CyclicBarrier barrier;

	// Track time spent by clients getting sequence numbers
	private long aggregatedTimeSequenceNumbers = 0;

	// Count how many clients have completed
	private int completedClients = 0;

	static final Logger logger = LoggerFactory.getLogger(ServerImplementation.class);


	public ServerImplementation() throws RemoteException {
		super();
		// Initialize the barrier with the number of clients
		barrier = new CyclicBarrier(Util.getNrClients());
	}

	/**
	 * Gets a new unique sequence number. This method MUST be implemented to get a
	 * passing grade for this assignment.
	 * 
	 * @return the sequence number
	 */
	@Override
	public int getSequenceNumber() throws RemoteException {
		return sequenceNumber.getAndIncrement();
	}

	/**
	 * By calling this method, the clients inform the server that they are done. The
	 * pass some timing statistics to the server, so the server can compute
	 * latencies and throughputs. his method MUST be implemented to get a passing
	 * grade for this assignment.
	 * 
	 * @param nanosSequenceNumbers The total time (in nanoseconds) spent in the
	 *                             getSequenceNumber calls.
	 */
	@Override
	public void setDone(long nanosSequenceNumbers) throws RemoteException {
		aggregatedTimeSequenceNumbers += nanosSequenceNumbers;
		completedClients++;
	}

	/**
	 * A barrier: this method blocks until all clients have called this method.
	 * Then, the clients are released, and the barrier is reset. You can call the
	 * barrier method more than once. This method MUST be implemented to get a passing grade for this assignment.
	 * 
	 */
	@Override
	public void barrier() throws RemoteException {
		try {
			// Wait until all clients reach this point
			barrier.await();
		} catch (Exception e) {
			throw new RemoteException("Barrier operation failed", e);
		}
	}

	// The methods below are only called by the server, and never by a client

	protected long getAggregatedTimeSequenceNumbers() {
		return aggregatedTimeSequenceNumbers;
	}

	public int getCompletedClients() {
		return completedClients;
	}
}
