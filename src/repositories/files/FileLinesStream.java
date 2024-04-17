package repositories.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class FileLinesStream {

    private List<String> lines;
    private int index;
    private Path filePath;

    public FileLinesStream(Path filePath) throws IOException {

        this.filePath = filePath;
        this.reset();
    }

    public String nextLine() {
        if (!hasNext())
            throw new IndexOutOfBoundsException("No more lines to read");
        return lines.get(index++);
    }

    public boolean hasNext() {
        return index < lines.size();
    }

    public void writeLines(List<String> strings) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.write(this.filePath, strings, CREATE, TRUNCATE_EXISTING);
    }

    public void reset() {
        this.index = 0;
        try {
            this.lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            this.lines = List.of(); // if file does not exist, use empty list
        }
    }
}
