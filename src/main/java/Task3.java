import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task3{
    public static int port;
    public static String rootPath;
    public static int depth;
    public static String mask;
    public static ServerSocket server;

    public static void main(String[] args) throws IOException {
        port = Integer.parseInt(args[0]);
        rootPath = args[1];
        server = new ServerSocket(port);
        server.setReuseAddress(true);

        while (true) {
            if (server.isBound()) new Thread(() -> {
                try {
                    Socket client = server.accept();
                    System.out.println("New client connected");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                    writer.write("Input depth: ");
                    writer.flush();
                    depth = Integer.parseInt(reader.readLine());

                    writer.write("Input mask: ");
                    writer.flush();
                    mask = reader.readLine();

                    System.out.println("InputClosed");

                    try {
                        Files.walk(Path.of(rootPath))
                                .filter(p -> depth == p.getNameCount() - Path.of(rootPath).getNameCount())
                                .filter(p -> p.getFileName().toString().contains(mask))
                                .forEach(p -> {
                                    try {
                                        writer.write(p.toString());
                                        writer.newLine();
                                        writer.flush();
                                        Thread.sleep(200);
                                    } catch (IOException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}