package email;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ListDir {
    private static BufferedWriter writer;
    private static ListDir instance = new ListDir();

    public static ListDir getInstance() {
        return instance;
    }

    public static void listAll(String r, String out) throws IOException {
        writer = new BufferedWriter(new FileWriter(out));
        list(r, "");
        writer.close();
    }

    public static void list(String root, String prefix) throws IOException {
        Path r = Path.of(root).toAbsolutePath().toRealPath();
        if (r.equals(root)) {
            writer.write(root.toString());
            writer.newLine();
        }
        Stream<Path> stream = Files.list(r);
        Path[] ls = stream.toArray(Path[]::new);
        stream.close();
        for (int i = 0; i < ls.length; i++) {
            Path p = ls[i];
            if (i == ls.length - 1) {
                writer.write(prefix);
                writer.write("└───");
                writer.write(p.getName(p.getNameCount() - 1).toString());
                writer.newLine();
                writer.flush();
                if (Files.isDirectory(p))
                    list(String.valueOf(p), prefix + "   ");
            } else {
                writer.write(prefix);
                writer.write("├───");
                writer.write(p.getName(p.getNameCount() - 1).toString());
                writer.newLine();
                writer.flush();
                if (Files.isDirectory(p))
                    list(String.valueOf(p), prefix + "│   ");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("List Directory");
        try {
            listAll("C:","out.txt");
        } catch (IOException e) {
            System.out.println("err");
            e.printStackTrace();
        }
    }
}