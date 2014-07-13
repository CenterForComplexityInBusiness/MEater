package edu.umd.rhsmith.diads.meater.core.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import edu.umd.rhsmith.diads.meater.util.ControlException;

/**
 * The interface with which another process can control a running
 * MEater instance.
 * 
 */
public interface IMEaterRemoteControl extends Remote {

	/**
	 * Instructs this MEater instance to cease data collection
	 * (though it can continue working through its backlog).
	 * 
	 * @throws RemoteException
	 *             If a network error occurs
	 * @throws IllegalStateException
	 *             If this instance is not yet started
	 * @throws ControlException
	 *             If an internal error occurs while shutting down the MEater
	 *             instance
	 * @throws ControlException
	 */
	public void remoteStop() throws RemoteException, IllegalStateException,
			ControlException;

	/**
	 * @return <code>true</code> if this MEater instance is
	 *         actively collecting data (and not only working through its
	 *         backlog)
	 * @throws RemoteException
	 *             If a network error occurs
	 */
	public boolean remoteIsActive() throws RemoteException;

	/**
	 * @return The fraction of available memory this MEater instance's JVM
	 *         is currently using.
	 * @throws RemoteException
	 *             If a network error occurs
	 */
	public float remoteGetMemoryUsage() throws RemoteException;

	/**
	 * 
	 * @return The current status of this MEater instance with regards to
	 *         startup and shutdown control.
	 * @throws RemoteException
	 *             If a network error occurs
	 */
	public String remoteGetControlStatus() throws RemoteException;
}
