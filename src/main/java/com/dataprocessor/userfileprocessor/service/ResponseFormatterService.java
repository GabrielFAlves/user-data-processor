package com.dataprocessor.userfileprocessor.service;

import com.dataprocessor.userfileprocessor.dto.UserResponseDTO;
import com.dataprocessor.userfileprocessor.wrapper.UsersOutputWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ResponseFormatterService {

    private final ObjectMapper jsonMapper;
    private final XmlMapper xmlMapper;
    private final CsvMapper csvMapper;

    public ResponseFormatterService(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new JavaTimeModule());
        this.xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.csvMapper = new CsvMapper();
        this.csvMapper.registerModule(new JavaTimeModule());
        this.csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String formatUserResponse(List<UserResponseDTO> users, String format) throws JsonProcessingException {
        log.debug("Formatando resposta no formato: {}", format);

        return switch (format.toLowerCase()) {
            case "json" -> formatToJson(users);
            case "xml" -> formatToXml(users);
            case "csv" -> formatToCsv(users);
            default -> throw new IllegalArgumentException(
                    "Formato inválido: " + format + ". Formatos aceitos: json, csv, xml"
            );
        };
    }

    public MediaType getMediaType(String format) {
        return switch (format.toLowerCase()) {
            case "json" -> MediaType.APPLICATION_JSON;
            case "xml" -> MediaType.APPLICATION_XML;
            case "csv" -> MediaType.parseMediaType("text/csv");
            default -> MediaType.APPLICATION_JSON;
        };
    }

    private String formatToJson(List<UserResponseDTO> users) throws JsonProcessingException {
        log.debug("Convertendo {} usuários para JSON", users.size());
        return jsonMapper.writeValueAsString(users);
    }

    private String formatToXml(List<UserResponseDTO> users) throws JsonProcessingException {
        log.debug("Convertendo {} usuários para XML", users.size());
        UsersOutputWrapper wrapper = new UsersOutputWrapper(users);
        return xmlMapper.writeValueAsString(wrapper);
    }

    private String formatToCsv(List<UserResponseDTO> users) throws JsonProcessingException {
        log.debug("Convertendo {} usuários para CSV", users.size());
        CsvSchema schema = csvMapper.schemaFor(UserResponseDTO.class).withHeader();
        return csvMapper.writer(schema).writeValueAsString(users);
    }

    public boolean isValidFormat(String format) {
        if (format == null || format.trim().isEmpty()) {
            return false;
        }
        String formatLower = format.toLowerCase();
        return formatLower.equals("json") ||
                formatLower.equals("xml") ||
                formatLower.equals("csv");
    }
}