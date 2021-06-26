import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {

    private static final BlockingQueue<String> targetFilePaths = new ArrayBlockingQueue<>(1, true);
    public static String mask;
    public static String path;
    public static int depth;
    public static String targetFile;

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter mask: ");
        mask = in.nextLine();
        System.out.print("Enter rootPath: ");
        path = in.nextLine();
        System.out.print("Enter depth: ");
        depth = in.nextInt();
        in.close();

        new SearchingThread().start();
        new ShowingThread().start();

    }

    public static class SearchingThread extends Thread {
        @Override
        public void run() {
                try {
                    Stream<Path> filesTraverse = Files.walk(Path.of(path));

                    filesTraverse.filter(p -> depth == p.getNameCount() - Path.of(path).getNameCount())
                            .filter(p -> p.getFileName().toString().contains(mask))
                            .forEach(p -> {
                                try {
                                    targetFilePaths.put(p.toString());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    targetFilePaths.put("done");
                    stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
    }

    public static class ShowingThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    targetFile = targetFilePaths.take();
                    if (targetFile == "done")  stop();
                    System.out.println(targetFile);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}