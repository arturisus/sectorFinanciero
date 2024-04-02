package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonFileReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJsonFile(File file, Class<T> valueType) throws IOException {
        return objectMapper.readValue(file, valueType);
    }

    public static <T> List<T> readJsonFileToList(File file, TypeReference<List<T>> typeReference) throws IOException {
        return objectMapper.readValue(file, typeReference);
    }
}
