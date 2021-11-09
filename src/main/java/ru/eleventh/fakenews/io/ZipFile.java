package ru.eleventh.fakenews.io;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFile {
    private final MultipartFile file;
    private String fileName;
    private String fileExtension;
    private int filesInsideCount;
    private int filesTxtCount;
    private int rowsCount;
    private String firstRow;
    private String secondRow;

    public static ZipFile getInstance(MultipartFile file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        zipFile.readFile();
        return zipFile;
    }

    private ZipFile(MultipartFile file) {
        this.file = file;
    }

    public boolean isValid() {
        if (fileName.isEmpty()) {
            throw new RuntimeException("ZIP file is not exist");
        }
        if (!fileExtension.equalsIgnoreCase("zip")) {
            throw new RuntimeException("It is not ZIP file");
        }
        if (filesInsideCount > 1 || filesTxtCount != 1) {
            throw new RuntimeException("ZIP file should contain one TXT file");
        }
        if (rowsCount != 2) {
            throw new RuntimeException("TXT file should contain two rows");
        }
        return true;
    }

    private void readFile() throws IOException {
        fileName = file.getOriginalFilename();
        fileExtension = FilenameUtils.getExtension(this.fileName);

        try (ZipInputStream stream = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    filesInsideCount++;
                    if (FilenameUtils.getExtension(entry.getName()).equalsIgnoreCase("txt")) {
                        filesTxtCount++;
                    }
                    List<String> lines = IOUtils.readLines(stream, StandardCharsets.UTF_8);
                    if (!lines.isEmpty()) {
                        rowsCount = lines.size();
                        if (rowsCount >= 2) {
                            firstRow = lines.get(0);
                            secondRow = lines.get(1);
                        }
                    }
                }
                stream.closeEntry();
            }
        }
    }

    public MultipartFile getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getFilesInsideCount() {
        return filesInsideCount;
    }

    public int getFilesTxtCount() {
        return filesTxtCount;
    }

    public String getFirstRow() {
        return firstRow;
    }

    public String getSecondRow() {
        return secondRow;
    }
}
