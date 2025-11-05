package com.dataprocessor.userfileprocessor.parser;

import com.dataprocessor.userfileprocessor.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileParser {
    List<UserDTO> parse(MultipartFile file) throws Exception;
}