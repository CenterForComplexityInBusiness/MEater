package edu.umd.rhsmith.diads.meater.core.remote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.core.config.MEaterConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.Util;

public class MEaterRemoteControl implements IMEaterRemoteControl {

	/*
	 * --------------------------------
	 * Entry point for main()
	 * --------------------------------
	 */

	public static void main(String[] args) {
		// interpret args
		String configurationFilename;
		if (args.length == 0) {
			configurationFilename = MEaterSetupConsole.DEFAULT_MEATER_CONFIG_FILENAME;
		} else {
			configurationFilename = args[1];
		}

		// let's go
		MEaterMain main = null;
		MEaterRemoteControl remote = null;
		// set up application
		try {
			main = MEaterConfig
					.mainFromConfigurationFile(configurationFilename);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// set up remote control
		try {
			remote = new MEaterRemoteControl(main);
			remote.remoteRegister();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			main.start();
			main.awaitStopFinished();
		} catch (Exception e) {
			main.logSevere(Util.traceMessage(e));
		} finally {
			remote.remoteUnregister();
		}
	}

	/*
	 * --------------------------------
	 * Actual main routine
	 * --------------------------------
	 */

	private final MEaterMain main;

	public MEaterRemoteControl(MEaterMain main) throws RemoteException {
		this.main = main;
	}

	/*
	 * --------------------------------
	 * RMI start/stop interface
	 * --------------------------------
	 */

	public boolean remoteRegister() {
		this.main.logInfo(String.format(FMT_MSG_REGISTERING, this.main
				.getName()));

		IMEaterRemoteControl stub;
		try {
			stub = (IMEaterRemoteControl) UnicastRemoteObject.exportObject(
					this, 0);
			LocateRegistry.getRegistry().rebind(this.main.getName(), stub);
		} catch (RemoteException e) {
			this.main.logSevere(String.format(FMT_MSG_ERR_COULDNT_REGISTER,
					Util.traceMessage(e)));
			return false;
		}

		this.main.logInfo(String.format(FMT_MSG_REGISTERED, Util.getHost()));

		return true;
	}

	public boolean remoteUnregister() {
		this.main.logInfo(String.format(FMT_MSG_DEREGISTERING, this.main
				.getName()));

		try {
			LocateRegistry.getRegistry().unbind(this.main.getName());
		} catch (RemoteException | NotBoundException e) {
			this.main.logSevere(String.format(FMT_MSG_ERR_COULDNT_DEREGISTER,
					Util.traceMessage(e)));
			return false;
		}

		this.main.logInfo(String.format(FMT_MSG_DEREGISTERED, Util.getHost()));

		return true;
	}

	/*
	 * --------------------------------
	 * Remote control actions
	 * --------------------------------
	 */

	@Override
	public String remoteGetControlStatus() throws RemoteException {
		return this.main.getControlStatusString();
	}

	@Override
	public void remoteStop() throws RemoteException, IllegalStateException,
			ControlException {
		this.main.stop();
	}

	@Override
	public boolean remoteIsActive() throws RemoteException {
		return this.main.isStartBegun() && !this.main.isStopBegun();
	}

	@Override
	public float remoteGetMemoryUsage() throws RemoteException {
		return Util.getMemoryUtilizationFraction();
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */

	private static final String FMT_MSG_REGISTERING = "Registering %s";
	private static final String FMT_MSG_REGISTERED = "Registered successfully on %s";
	private static final String FMT_MSG_ERR_COULDNT_REGISTER = "Couldn't register on RMI registry: %s";
	private static final String FMT_MSG_DEREGISTERING = "Unregistering %s";
	private static final String FMT_MSG_DEREGISTERED = "Unregistered successfully on %s";
	private static final String FMT_MSG_ERR_COULDNT_DEREGISTER = "Couldn't unregister from RMI registry: %s";

}
