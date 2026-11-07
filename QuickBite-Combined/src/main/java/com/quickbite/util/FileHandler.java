package com.quickbite.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

/** Reads and writes a text file (UTF-8). Creates the folder and file if they do not exist. */
public class FileHandler {
    private final Path file;

    public FileHandler(String directory, String fileName) throws IOException {
        Path dir = Paths.get(directory);
        Files.createDirectories(dir);
        file = dir.resolve(fileName);
        if (!Files.exists(file)) Files.createFile(file);
    }

    public List<String> readLines() throws IOException {
        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }

    /** Writes to a temp file first and then swaps it in, so a crash can never leave a half-written data file. */
    public void writeLines(List<String> lines) throws IOException {
        Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
        Files.write(tmp, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        try {
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void appendLine(String line) throws IOException {
        Files.write(file, List.of(line), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public Path getPath() { return file; }
}
