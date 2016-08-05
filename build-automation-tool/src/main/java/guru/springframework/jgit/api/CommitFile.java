package guru.springframework.jgit.api;



import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;



/**
 * Simple snippet which shows how to commit a file
 *
 * @author dominik.stadler at gmx.at
 */
public class CommitFile {

    public static void commitFile() throws IOException, GitAPIException {
        // prepare a new test-repository
        try (Repository repository = CookbookHelper.createNewRepository()) {
            try (Git git = new Git(repository)) {
                // create the file
                File myfile = new File(repository.getDirectory().getParent(), "testfile");
                myfile.createNewFile();

                // run the add
                git.add()
                        .addFilepattern("testfile")
                        .call();

                // and then commit the changes
                git.commit()
                        .setMessage("Added testfile")
                        .call();

                System.out.println("Committed file " + myfile + " to repository at " + repository.getDirectory());
            }
        }
    }
}
