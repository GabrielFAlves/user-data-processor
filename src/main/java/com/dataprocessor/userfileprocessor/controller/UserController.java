package com.dataprocessor.userfileprocessor.controller;

import com.dataprocessor.userfileprocessor.dto.UserResponseDTO;
import com.dataprocessor.userfileprocessor.enums.FileType;
import com.dataprocessor.userfileprocessor.service.ResponseFormatterService;
import com.dataprocessor.userfileprocessor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ResponseFormatterService formatterService;

    public UserController(UserService userService, ResponseFormatterService formatterService) {
        this.userService = userService;
        this.formatterService = formatterService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<UserResponseDTO>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType) {

        log.info("Recebendo requisição de upload - Tipo: {}, Arquivo: {}",
                fileType, file.getOriginalFilename());

        FileType type = FileType.fromString(fileType);
        List<UserResponseDTO> users = userService.uploadAndProcessFile(file, type);

        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @GetMapping
    public ResponseEntity<String> getAllUsers(
            @RequestParam(value = "format", defaultValue = "json") String format) {

        log.info("Consultando todos os usuários - Formato: {}", format);

        List<UserResponseDTO> users = userService.getAllUsers();

        try {
            String response = formatterService.formatUserResponse(users, format);
            MediaType mediaType = formatterService.getMediaType(format);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(response);

        } catch (Exception e) {
            log.error("Erro ao formatar resposta: {}", e.getMessage());
            throw new RuntimeException("Erro ao formatar resposta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("Buscando usuário com ID: {}", id);

        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/source/{source}")
    public ResponseEntity<List<UserResponseDTO>> getUsersBySource(@PathVariable String source) {
        log.info("Buscando usuários por source: {}", source);

        List<UserResponseDTO> users = userService.getUsersBySource(source);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deletando usuário com ID: {}", id);

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}