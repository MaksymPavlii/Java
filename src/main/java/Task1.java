import java.io.IOException;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;

public class Task1{

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter mask: ");
        String mask = in.nextLine();
        System.out.print("Enter rootPath: ");
        String path = in.nextLine();
        System.out.print("Enter depth: ");
        int depth = in.nextInt();

        try {
            Files.walk(Path.of(path))
                    .filter(p -> depth == p.getNameCount() - Path.of(path).getNameCount())
                    .filter(p -> p.getFileName().toString().contains(mask))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
       }
    }
}