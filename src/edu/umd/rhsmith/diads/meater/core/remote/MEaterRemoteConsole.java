package edu.umd.rhsmith.diads.meater.core.remote;

public class MEaterRemoteConsole {

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsage();
			return;
		}

		String command = args[0];

		if (command.equals("start")) {
			startMEater(args);
		} else if (command.equals("list")) {
			listMEaters(args);
		} else if (command.equals("stop")) {
			stopMEater(args);
		} else {
			printUsage();
		}
	}

	private static void startMEater(String[] args) {
		// TODO Auto-generated method stub

	}

	private static void listMEaters(String[] args) {
		// TODO Auto-generated method stub

	}

	private static void stopMEater(String[] args) {
		// TODO Auto-generated method stub

	}

	private static void printUsage() {
		// TODO Auto-generated method stub
	}
}
