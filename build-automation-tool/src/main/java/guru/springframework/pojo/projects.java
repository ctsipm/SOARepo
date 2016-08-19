package guru.springframework.pojo;

import java.util.List;

public class projects
{
	private String[] project;

    public String[] getProject ()
    {
        return project;
    }

    public void setProject (String[] project)
    {
        this.project = project;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [project = "+project+"]";
    }
}
	