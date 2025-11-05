package com.dataprocessor.userfileprocessor.service;

import com.dataprocessor.userfileprocessor.dto.UserDTO;
import com.dataprocessor.userfileprocessor.dto.UserResponseDTO;
import com.dataprocessor.userfileprocessor.enums.FileType;
import com.dataprocessor.userfileprocessor.model.User;
import com.dataprocessor.userfileprocessor.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final FileProcessorService fileProcessorService;

    public UserService(UserRepository userRepository, FileProcessorService fileProcessorService) {
        this.userRepository = userRepository;
        this.fileProcessorService = fileProcessorService;
    }

    @Transactional
    public List<UserResponseDTO> uploadAndProcessFile(MultipartFile file, FileType fileType) {
        log.info("Iniciando upload e processamento de arquivo tipo: {}", fileType);

        List<UserDTO> userDTOs = fileProcessorService.processFile(file, fileType);

        List<User> users = userDTOs.stream()
                .map(dto -> convertToEntity(dto, fileType.name()))
                .collect(Collectors.toList());

        List<User> savedUsers = userRepository.saveAll(users);

        log.info("Total de {} usuários salvos no banco de dados", savedUsers.size());

        return savedUsers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllUsers() {
        log.info("Buscando todos os usuários");

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        log.info("Buscando usuário com ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        return convertToResponseDTO(user);
    }

    public List<UserResponseDTO> getUsersBySource(String source) {
        log.info("Buscando usuários por source: {}", source);

        List<User> users = userRepository.findBySource(source.toUpperCase());

        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Removendo usuário com ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }

        userRepository.deleteById(id);
    }

    private User convertToEntity(@Valid UserDTO dto, String source) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .source(source)
                .build();
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .source(user.getSource())
                .createdAt(user.getCreatedAt())
                .build();
    }
}