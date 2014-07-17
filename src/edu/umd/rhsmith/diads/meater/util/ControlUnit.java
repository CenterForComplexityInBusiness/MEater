package edu.umd.rhsmith.diads.meater.util;

public abstract class ControlUnit extends LogUnit {
	private final Object controlWaiter;
	protected final Object controlLock;

	private boolean stopBegun;
	private boolean startBegun;
	private boolean stopFinished;
	private boolean startFinished;
	private boolean startFailed;
	private ControlException failureException;

	public ControlUnit() {
		this.controlWaiter = new Object();
		this.controlLock = new Object();

		this.startBegun = false;
		this.startFinished = false;
		this.stopBegun = false;
		this.stopFinished = false;
		this.startFailed = false;
		this.failureException = null;
	}

	/*
	 * --------------------------------
	 * Control commands
	 * --------------------------------
	 */

	public final void start() throws ControlException {
		synchronized (this.controlLock) {
			this.requireUnStarted();
			this.requireUnStopped();

			this.getLogger().info(this.messageString(MSG_STARTING));
			synchronized (this.controlWaiter) {
				this.startBegun = true;
				this.controlWaiter.notifyAll();
			}

			try {
				this.doStartupRoutine();
			} catch (ControlException e) {
				this.getLogger().info(this.messageString(MSG_START_FAILED));
				synchronized (this.controlWaiter) {
					this.startFailed = true;
					this.failureException = e;
					this.controlWaiter.notifyAll();
				}
				throw e;
			}

			this.getLogger().info(this.messageString(MSG_STARTED));
			synchronized (this.controlWaiter) {
				this.startFinished = true;
				this.controlWaiter.notifyAll();
			}
		}
	}

	protected abstract void doStartupRoutine() throws ControlException;

	public final void stop() {
		synchronized (this.controlLock) {
			synchronized (this.controlWaiter) {
				this.stopBegun = true;
				this.getLogger().info(this.messageString(MSG_STOPPING));
				this.controlWaiter.notifyAll();
			}

			this.doShutdownRoutine();

			synchronized (this.controlWaiter) {
				this.stopFinished = true;
				this.getLogger().info(this.messageString(MSG_STOPPED));
				this.controlWaiter.notifyAll();
			}
		}
	}

	protected abstract void doShutdownRoutine();

	/*
	 * --------------------------------
	 * Control status-checkers
	 * --------------------------------
	 */

	public final boolean isStartBegun() {
		return startBegun;
	}

	public final boolean isStartFinished() {
		return startFinished;
	}

	public final boolean isStopBegun() {
		return stopBegun;
	}

	public final boolean isStopFinished() {
		return stopFinished;
	}

	public final boolean isStartFailed() {
		return startFailed;
	}

	/*
	 * --------------------------------
	 * Control status-waiters
	 * --------------------------------
	 */

	public final void awaitStartBegun() throws InterruptedException {
		synchronized (this.controlWaiter) {
			while (!this.startBegun) {
				this.controlWaiter.wait();
			}
		}
	}

	public final void awaitStopBegun() throws InterruptedException {
		synchronized (this.controlWaiter) {
			while (!this.stopBegun) {
				this.controlWaiter.wait();
			}
		}
	}

	public final void awaitStartFinished() throws InterruptedException,
			ControlException {
		synchronized (this.controlWaiter) {
			while (!this.startFinished && !startFailed) {
				this.controlWaiter.wait();
			}

			if (startFailed) {
				throw failureException;
			}
		}
	}

	public final void awaitStopFinished() throws InterruptedException {
		synchronized (this.controlWaiter) {
			while (!this.stopFinished) {
				this.controlWaiter.wait();
			}
		}
	}

	/*
	 * --------------------------------
	 * Control state-validators
	 * --------------------------------
	 */

	public final void requireStarted() throws IllegalStateException {
		if (!this.startFinished) {
			throw new IllegalStateException(this.messageString(MSG_ERR_NOSTART));
		}
	}

	public final void requireUnStarted() throws IllegalStateException {
		if (this.startBegun) {
			throw new IllegalStateException(this.messageString(MSG_ERR_START));
		}
	}

	public final void requireStopped() throws IllegalStateException {
		if (!this.stopFinished) {
			throw new IllegalStateException(this.messageString(MSG_ERR_NOSTOP));
		}
	}

	public final void requireUnStopped() throws IllegalStateException {
		if (this.stopBegun) {
			throw new IllegalStateException(this.messageString(MSG_ERR_STOP));
		}
	}

	/*
	 * --------------------------------
	 * General getters / setters
	 * --------------------------------
	 */

	public String getControlStatusString() {
		if (!this.isStartBegun()) {
			return "Startup not begun";
		} else if (!this.isStartFinished()) {
			return "Startup in progress";
		} else if (!this.isStopBegun()) {
			return "Active";
		} else if (!this.isStopFinished()) {
			return "Shutdown in progress";
		} else {
			return "Terminated";
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NOSTART = "not yet started";
	private static final String MSG_ERR_START = "already starting / started";
	private static final String MSG_STARTING = "starting";
	private static final String MSG_STARTED = "started";
	private static final String MSG_START_FAILED = "start failed";

	private static final String MSG_ERR_NOSTOP = "not yet stopped";
	private static final String MSG_ERR_STOP = "already stopping / stopped";
	private static final String MSG_STOPPING = "stopping";
	private static final String MSG_STOPPED = "stopped";
}
