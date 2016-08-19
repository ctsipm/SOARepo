package guru.springframework.pojo;
public class arguments
{
    private String[] argument;

    public String[] getargument ()
    {
        return argument;
    }

    public void setargument (String[] argument)
    {
        this.argument = argument;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [argument = "+argument+"]";
    }
}