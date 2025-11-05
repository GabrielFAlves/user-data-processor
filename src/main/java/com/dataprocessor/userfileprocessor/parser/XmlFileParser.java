package com.dataprocessor.userfileprocessor.parser;

import com.dataprocessor.userfileprocessor.dto.UserDTO;
import com.dataprocessor.userfileprocessor.exception.FileProcessingException;
import com.dataprocessor.userfileprocessor.wrapper.UsersInputWrapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class XmlFileParser implements FileParser {

    private final XmlMapper xmlMapper;

    public XmlFileParser() {
        this.xmlMapper = new XmlMapper();
    }

    @Override
    public List<UserDTO> parse(MultipartFile file) throws Exception {
        try {
            UsersInputWrapper wrapper = xmlMapper.readValue(
                    file.getInputStream(),
                    UsersInputWrapper.class
            );

            if (wrapper == null || wrapper.getUsers() == null || wrapper.getUsers().isEmpty()) {
                throw new FileProcessingException("Arquivo XML está vazio ou não contém dados válidos");
            }

            List<UserDTO> users = wrapper.getUsers();

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
            throw new FileProcessingException("Erro ao processar arquivo XML: " + e.getMessage(), e);
        }
    }
}