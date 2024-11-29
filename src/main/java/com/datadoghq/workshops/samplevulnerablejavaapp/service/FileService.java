package com.datadoghq.workshops.samplevulnerablejavaapp.service;

import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileForbiddenFileException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileReadException;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileService {
    final static String ALLOWED_PREFIX = "/tmp/files/";

    public String readFile(String path) throws FileForbiddenFileException, FileReadException {
        try {
            File file = new File(path).getCanonicalFile();
            if (!file.getPath().startsWith(ALLOWED_PREFIX)) {
                throw new FileForbiddenFileException("You are not allowed to read " + path);
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                return sb.toString();
            } catch (IOException e) {
                throw new FileReadException(e.getMessage());
            }
        } catch (IOException e) {
            throw new FileReadException("Invalid file path: " + e.getMessage());
        }
    }
}
