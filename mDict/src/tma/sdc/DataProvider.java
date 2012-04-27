package tma.sdc;
public abstract class DataProvider 
{
	public String GetName()
	{
		return "Default Name. You must override GetName method.";
	}
	public abstract String GetDefination(String word);
	public abstract String[] GetSuggestion(String prefix);
	public abstract void Close();
	
}
