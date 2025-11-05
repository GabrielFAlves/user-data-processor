package com.dataprocessor.userfileprocessor.enums;

public enum FileType {
    CSV,
    JSON,
    XML;

    public static FileType fromString(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de arquivo não pode ser nulo ou vazio");
        }

        try {
            return FileType.valueOf(type.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo de arquivo inválido: " + type + ". Tipos aceitos: CSV, JSON, XML"
            );
        }
    }
}