package ds.pa1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * TODO This is the interface defining the API of the remote object. 
 * TODO YOU HAVE TO CHANGE THIS FILE!
 */

public interface ServerInterface extends Remote {
	/**
	 * Gets a new unique sequence number.
	 * 
	 * @return the sequence number
	 * @throws RemoteException if a remote communication error occurs
	 */
	public int getSequenceNumber() throws RemoteException;

	/**
	 * A barrier: this method blocks until all clients have called this method.
	 * Then, the clients are released, and the barrier is reset. You can call the
	 * barrier method more than once.
	 * 
	 * @throws RemoteException if a remote communication error occurs
	 *  
	 */
	public void barrier() throws RemoteException;

	/**
	 * By calling this method, the clients inform the server that they are done. They
	 * pass some timing statistics to the server, so the server can compute
	 * latencies and throughputs.
	 * 
	 * @param nanosSequenceNumbers The total time (in nanoseconds) spent in the
	 *                             getSequenceNumber calls.
	 * @throws RemoteException if a remote communication error occurs
	 */
	public void setDone(long nanosSequenceNumbers) throws RemoteException;
}
