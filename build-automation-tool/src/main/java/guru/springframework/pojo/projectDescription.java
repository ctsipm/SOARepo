package guru.springframework.pojo;
public class projectDescription
{

    private buildSpec buildSpec;

    private projects projects;

    private natures natures;

    private String name;

    private String comment;

    public buildSpec getbuildSpec ()
    {
        return buildSpec;
    }

    public void setbuildSpec (buildSpec buildSpec)
    {
        this.buildSpec = buildSpec;
    }

    public projects getprojects ()
    {
        return projects;
    }

    public void setprojects (projects projects)
    {
        this.projects = projects;
    }

    public natures getnatures ()
    {
        return natures;
    }

    public void setnatures (natures natures)
    {
        this.natures = natures;
    }

    public String getname ()
    {
        return name;
    }

    public void setname (String name)
    {
        this.name = name;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [buildSpec = "+buildSpec+", projects = "+projects+", natures = "+natures+", name = "+name+", comment = "+comment+"]";
    }	
}
