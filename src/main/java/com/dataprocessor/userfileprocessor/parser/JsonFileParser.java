package com.dataprocessor.userfileprocessor.parser;

import com.dataprocessor.userfileprocessor.dto.UserDTO;
import com.dataprocessor.userfileprocessor.exception.FileProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class JsonFileParser implements FileParser {

    private final ObjectMapper objectMapper;

    public JsonFileParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<UserDTO> parse(MultipartFile file) throws Exception {
        try {
            List<UserDTO> users = objectMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<List<UserDTO>>() {}
            );

            if (users == null || users.isEmpty()) {
                throw new FileProcessingException("Arquivo JSON está vazio ou não contém dados válidos");
            }

            for (int i = 0; i < users.size(); i++) {
                UserDTO user = users.get(i);
                if (user.getName() == null || user.getName().trim().isEmpty() ||
                        user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                    throw new FileProcessingException(
                            "Usuário na posição " + i + " contém dados vazios"
                    );
                }

                user.setName(user.getName().trim());
                user.setEmail(user.getEmail().trim());
            }

            return users;

        } catch (Exception e) {
            throw new FileProcessingException("Erro ao processar arquivo JSON: " + e.getMessage(), e);
        }
    }
}