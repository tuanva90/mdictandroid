package tma.sdc;

public class Dictionary {
	private String name;
	private DataProvider dataProvider;
	private PronounceEngine proEngine=null;
	public Dictionary(DataProvider dp)
	{
		this.dataProvider=dp;
	}
	public void SetName(String name)
	{
		this.name=name;
	}
	public String GetName()
	{
		return this.name;
	}
	public DataProvider GetDataProvider()
	{
		return this.dataProvider;
	}
	public void SetDataProvider(DataProvider dp)
	{
		this.dataProvider=dp;
	}
	public String LookUp(String word)
	{
		return this.dataProvider.GetDefination(word);
	}
	public String[] GetSuggestion(String prefix)
	{
		return this.dataProvider.GetSuggestion(prefix);
	}
	public void Dispose()
	{
		this.dataProvider.Close();
	}
	public PronounceEngine GetPronounceEngine()
	{
		return this.proEngine;
	}
	public void SetPronounceEngine(PronounceEngine proEngine)
	{
		this.proEngine=proEngine;
	}
	
}
