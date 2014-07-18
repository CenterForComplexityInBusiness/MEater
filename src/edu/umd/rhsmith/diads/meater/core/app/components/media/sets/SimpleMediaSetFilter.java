package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

public abstract class SimpleMediaSetFilter<M> extends BaseMediaSetFilter<M> {

	private final String presentName;
	private final String absentName;
	private final Class<M> mediaClass;

	public SimpleMediaSetFilter(String presentName, String absentName,
			Class<M> mediaClass) throws IllegalArgumentException {
		this.presentName = presentName;
		this.absentName = absentName;
		this.mediaClass = mediaClass;
	}

	@Override
	public String getFilterPresentName() {
		return this.presentName;
	}

	@Override
	public String getFilterAbsentName() {
		return this.absentName;
	}

	@Override
	public Class<M> getMediaClass() {
		return this.mediaClass;
	}

}
