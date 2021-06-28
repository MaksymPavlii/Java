import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Task1 {

    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);
        System.out.print("Enter mask: ");
        String mask = in.nextLine();
        System.out.print("Enter rootPath: ");
        String path = in.nextLine();
        while (!Files.exists(Path.of(path))) {
            System.out.print("Invalid path: ");
            path = in.nextLine();
        }
        String finalPath = path;
        System.out.print("Enter depth: ");
        int depth;
        while ((depth = in.nextInt()) < 0){
            System.out.print("depth should be positive: ");
        }
        int finalDepth = depth;
        in.close();

        Files.walk(Path.of(path))
                .filter(p -> finalDepth == (p.getNameCount() - Path.of(finalPath).getNameCount()))
                .filter(p -> p.getFileName().toString().contains(mask))
                .forEach(System.out::println);
    }
}