package com.sprint.mission.discodeit.store;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class Store {
    public static final Path USER_DATA_DIR = Path.of(System.getProperty("user.dir"), "data");
    public static final Path USER_DATA_FILE = Path.of(USER_DATA_DIR.toString(), "users.ser");
    public static final Path CHANNEL_DATA_FILE = Path.of(USER_DATA_DIR.toString(), "channels.ser");
    public static final Path MESSAGE_DATA_FILE = Path.of(USER_DATA_DIR.toString(), "messages.ser");


    public static <K extends Serializable, V extends Serializable> void saveMap(Path file, Map<K, V> map) {
        try {
            Path parent = file.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            try (var oos = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(file)))) {
                oos.writeObject(map); // Map 자체 직렬화
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K extends Serializable, V extends Serializable> Map<K, V> loadMap(Path file) {
        if (!Files.exists(file)) {
            return new HashMap<>();
        }
        try (var ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            Object obj = ois.readObject();
            if ((obj instanceof Map<?, ?> map)) {
                return new HashMap<>((Map<K, V>) map);
            }
            throw new IllegalStateException("저장된 데이터가 Map이 아님!");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
