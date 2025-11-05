package com.dataprocessor.userfileprocessor.parser;

import com.dataprocessor.userfileprocessor.enums.FileType;
import org.springframework.stereotype.Component;

@Component
public class FileParserFactory {

    private final CsvFileParser csvFileParser;
    private final JsonFileParser jsonFileParser;
    private final XmlFileParser xmlFileParser;

    public FileParserFactory(CsvFileParser csvFileParser,
                             JsonFileParser jsonFileParser,
                             XmlFileParser xmlFileParser) {
        this.csvFileParser = csvFileParser;
        this.jsonFileParser = jsonFileParser;
        this.xmlFileParser = xmlFileParser;
    }

    public FileParser getParser(FileType fileType) {
        return switch (fileType) {
            case CSV -> csvFileParser;
            case JSON -> jsonFileParser;
            case XML -> xmlFileParser;
        };
    }
}