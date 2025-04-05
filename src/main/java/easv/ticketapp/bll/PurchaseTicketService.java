package easv.ticketapp.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PurchaseTicketService {

    public static List<File> convertPathsToFiles(List<String> filePaths) {
        List<File> files = new ArrayList<>();
        for (String path : filePaths) {
            File file = new File(path);
            if (file.exists()) {
                files.add(file);
                System.out.println("File added: " + file.getAbsolutePath() + ", Size: " + file.length());
            } else {
                System.out.println("File does not exist: " + path);
            }
        }
        return files;
    }
}
