package guru.springframework.pojo;
public class buildSpec
{
    private buildCommand[] buildCommand;

    public buildCommand[] getbuildCommand ()
    {
        return buildCommand;
    }

    public void setbuildCommand (buildCommand[] buildCommand)
    {
        this.buildCommand = buildCommand;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [buildCommand = "+buildCommand+"]";
    }
}