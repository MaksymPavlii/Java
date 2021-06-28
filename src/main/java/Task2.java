import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Task2 {

    private static final BlockingQueue<String> targetFilesPaths = new ArrayBlockingQueue<>(1, true);
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
        while (!Files.exists(Path.of(path))) {
            System.out.print("Invalid path: ");
            path = in.nextLine();
        }
        System.out.print("Enter depth: ");
        while ((depth = in.nextInt()) < 0){
            System.out.print("depth should be positive: ");
        }
        in.close();

        new SearchingThread().start();
        new PrintingThread().start();
    }

    public static class SearchingThread extends Thread {
        @Override
        public void run() {
                try {
                    Stream<Path> filesTraverse = Files.walk(Path.of(path));
                    filesTraverse
                            .filter(p -> p.getFileName().toString().contains(mask))
                            .filter(p -> depth == (p.getNameCount() - Path.of(path).getNameCount()))
                            .forEach(p -> {
                                try {
                                    targetFilesPaths.put(p.toString());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    targetFilesPaths.put("done");
                    interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
    }

    public static class PrintingThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    targetFile = targetFilesPaths.take();
                    if (targetFile.equals("done"))  stop();
                    System.out.println(targetFile);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}