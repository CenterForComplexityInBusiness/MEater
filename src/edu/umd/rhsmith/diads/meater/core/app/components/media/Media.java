package edu.umd.rhsmith.diads.meater.core.app.components.media;

public class Media {

	private Media() {
	}

	public static String handlerName(String ownerName,
			String handlerBaseName) {
		if (ownerName == null || ownerName.isEmpty()) {
			if (handlerBaseName == null || handlerBaseName.isEmpty()) {
				return "";
			} else {
				return handlerBaseName;
			}
		} else {
			if (handlerBaseName == null || handlerBaseName.isEmpty()) {
				return ownerName;
			} else {
				return String.format("%s.%s", ownerName, handlerBaseName);
			}
		}
	}
}
