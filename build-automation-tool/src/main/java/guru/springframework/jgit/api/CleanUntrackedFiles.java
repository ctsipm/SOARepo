package guru.springframework.jgit.api;



import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;



/**
 * Simple snippet which shows how to list all Tags
 *
 * @author dominik.stadler at gmx.at
 */
public class CleanUntrackedFiles {

    public static void cleanUntrackedFiles() throws IOException, GitAPIException {
        try (Repository repository = CookbookHelper.createNewRepository()) {
            System.out.println("Repository at " + repository.getWorkTree());

            File untrackedFile = File.createTempFile("untracked", ".txt", repository.getWorkTree());
            File untrackedDir = File.createTempFile("untrackedDir", "", repository.getWorkTree());
            untrackedDir.delete();
            untrackedDir.mkdirs();

            System.out.println("Untracked exists: " + untrackedFile.exists() + " Dir: " + untrackedDir.exists() + "/" + untrackedDir.isDirectory());

            try (Git git = new Git(repository)) {
                Set<String> removed = git.clean().setCleanDirectories(true).call();
                for(String item : removed) {
                	System.out.println("Removed: " + item);
                }
                System.out.println("Removed " + removed.size() + " items");
            }

            System.out.println("Untracked after: " + untrackedFile.exists() + " Dir: " + untrackedDir.exists() + "/" + untrackedDir.isDirectory());
        }
    }
}
