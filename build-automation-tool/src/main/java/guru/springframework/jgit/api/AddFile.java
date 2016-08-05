package guru.springframework.jgit.api;



import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;



/**
 * Simple snippet which shows how to add a file to the index
 *
 * @author dominik.stadler at gmx.at
 */
public class AddFile {

    public static void addFile() throws IOException, GitAPIException {
        // prepare a new test-repository
        try (Repository repository = CookbookHelper.createNewRepository()) {
            try (Git git = new Git(repository)) {
                // create the file
                File myfile = new File(repository.getDirectory().getParent(), "testfile");
                myfile.createNewFile();

                // run the add-call
                git.add()
                        .addFilepattern("testfile")
                        .call();

                System.out.println("Added file " + myfile + " to repository at " + repository.getDirectory());
            }
        }
    }
}
