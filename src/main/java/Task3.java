import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


public class Task3 {
    public static int port;
    public static String rootPath;
    public static ServerSocket server;

    public static void main(String[] args) throws IOException {
        if(args[0].length() != 4)
            throw new IllegalArgumentException("port should be positive number");
        port = Integer.parseInt(args[0]);
        if (Arrays.stream(args).count() > 2)
            for (int i = 2; i < args.length; i++)
                args[1] = args[1].concat(" " + args[i]);
        rootPath = args[1];
        if(rootPath.isEmpty() || !Path.of(rootPath).toString().contains(":"))
            throw new IllegalArgumentException("Invalid Path");

        server = new ServerSocket(port);
        server.setReuseAddress(true);

        while (true) {
            if (server.isBound())
                new Thread(() -> {
                try {
                    Socket clientSocket = server.accept();
                    Client client = new Client();
                    System.out.println("New client connected");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    writer.write("Input depth: ");
                    writer.flush();
                    client.depth = Integer.parseInt(reader.readLine());
                    if(client.depth < 0)
                        throw new IllegalArgumentException("depth should be positive number");

                    writer.write("Input mask: ");
                    writer.flush();
                    client.mask = reader.readLine();

                    try {
                        Files.walk(Path.of(rootPath))
                                .filter(p -> client.depth == p.getNameCount() - Path.of(rootPath).getNameCount())
                                .filter(p -> p.getFileName().toString().contains(client.mask))
                                .forEach(p -> {
                                    try {
                                        writer.write(p.toString());
                                        writer.newLine();
                                        writer.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}