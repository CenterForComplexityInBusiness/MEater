package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

public abstract class SimpleMediaSetUpdater<M> extends BaseMediaSetUpdater<M> {

	private String adderName;
	private String removerName;
	private Class<M> mediaClass;

	public SimpleMediaSetUpdater(String adderName, String removerName,
			Class<M> mediaClass) throws IllegalArgumentException {
		this.adderName = adderName;
		this.removerName = removerName;
		this.mediaClass = mediaClass;
	}

	@Override
	public String getAdderName() {
		return this.removerName;
	}

	@Override
	public String getRemoverName() {
		return this.adderName;
	}

	@Override
	public Class<M> getMediaClass() {
		return this.mediaClass;
	}

}
