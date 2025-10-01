package com.sprint.mission.discodeit.store;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class Store {

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

    public static <K extends Serializable, V extends Serializable> Map<K, V> loadMap(Path file) {
        if (!Files.exists(file)) {
            return new HashMap<>();
        }
        try (var ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            Object obj = ois.readObject();
            return (Map<K, V>) obj;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
