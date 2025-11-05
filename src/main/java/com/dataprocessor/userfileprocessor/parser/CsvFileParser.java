package com.dataprocessor.userfileprocessor.parser;

import com.dataprocessor.userfileprocessor.dto.UserDTO;
import com.dataprocessor.userfileprocessor.exception.FileProcessingException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvFileParser implements FileParser {

    @Override
    public List<UserDTO> parse(MultipartFile file) throws Exception {
        List<UserDTO> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT
                             .builder()
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build())) {

            for (CSVRecord record : csvParser) {
                String name = record.get("name");
                String email = record.get("email");

                if (name == null || name.trim().isEmpty() ||
                        email == null || email.trim().isEmpty()) {
                    throw new FileProcessingException(
                            "Linha " + record.getRecordNumber() + " contém dados vazios"
                    );
                }

                users.add(UserDTO.builder()
                        .name(name.trim())
                        .email(email.trim())
                        .build());
            }

            if (users.isEmpty()) {
                throw new FileProcessingException("Arquivo CSV está vazio ou não contém dados válidos");
            }

        } catch (Exception e) {
            throw new FileProcessingException("Erro ao processar arquivo CSV: " + e.getMessage(), e);
        }

        return users;
    }
}