package ru.eleventh.fakenews.io;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
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

    public void checkFileValid() {
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
    }

    private void readFile() throws IOException {
        fileName = file.getOriginalFilename();
        fileExtension = FilenameUtils.getExtension(fileName);
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    filesInsideCount++;
                    if (FilenameUtils.getExtension(entry.getName()).equalsIgnoreCase("txt")) {
                        filesTxtCount++;
                    }
                    boolean readNext = true;
                    while (readNext) {
                        String line = br.readLine();
                        if (line != null) {
                            if (firstRow == null) {
                                firstRow = line;
                            } else {
                                secondRow = line;
                            }
                            rowsCount++;
                        } else {
                            readNext = false;
                        }
                    }
                }
            }
            zis.closeEntry();
        }
    }
}
