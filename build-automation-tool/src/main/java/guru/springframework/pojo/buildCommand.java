package guru.springframework.pojo;

public class buildCommand
{
    private String arguments;

    private String name;

    public String getarguments ()
    {
        return arguments;
    }

    public void setarguments (String arguments)
    {
        this.arguments = arguments;
    }

    public String getname ()
    {
        return name;
    }

    public void setname (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [arguments = "+arguments+", name = "+name+"]";
    }
}
			
		