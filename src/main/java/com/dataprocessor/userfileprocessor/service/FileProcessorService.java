package com.dataprocessor.userfileprocessor.service;

import com.dataprocessor.userfileprocessor.dto.UserDTO;
import com.dataprocessor.userfileprocessor.enums.FileType;
import com.dataprocessor.userfileprocessor.exception.InvalidFileFormatException;
import com.dataprocessor.userfileprocessor.parser.FileParser;
import com.dataprocessor.userfileprocessor.parser.FileParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class FileProcessorService {

    private final FileParserFactory fileParserFactory;

    public FileProcessorService(FileParserFactory fileParserFactory) {
        this.fileParserFactory = fileParserFactory;
    }

    public List<UserDTO> processFile(MultipartFile file, FileType fileType) {
        log.info("Processando arquivo do tipo: {}", fileType);

        validateFile(file);

        try {
            FileParser parser = fileParserFactory.getParser(fileType);
            List<UserDTO> users = parser.parse(file);

            log.info("Arquivo processado com sucesso. Total de usuários: {}", users.size());
            return users;

        } catch (Exception e) {
            log.error("Erro ao processar arquivo: {}", e.getMessage(), e);
            throw new InvalidFileFormatException("Erro ao processar arquivo: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileFormatException("Arquivo não pode ser vazio");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new InvalidFileFormatException("Nome do arquivo é inválido");
        }

        log.debug("Arquivo validado: {} - Tamanho: {} bytes",
                file.getOriginalFilename(), file.getSize());
    }
}