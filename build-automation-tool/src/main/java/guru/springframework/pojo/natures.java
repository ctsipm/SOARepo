package guru.springframework.pojo;
public class natures
{
	private String[] nature;

    public String[] getNature ()
    {
        return nature;
    }

    public void setNature (String[] nature)
    {
        this.nature = nature;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [nature = "+nature+"]";
    }
}
			
	