package tma.sdc;
public abstract class PronounceEngine {
	private String name;
	public PronounceEngine(String name)
	{
		this.name=name;
	}
	public abstract void Pronounce(String text);
}
