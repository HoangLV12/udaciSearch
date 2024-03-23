package com.udacity.webcrawler.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class to write a {@link CrawlResult} to file.
 */
public final class CrawlResultWriter {
    private final CrawlResult result;

    /**
     * Creates a new {@link CrawlResultWriter} that will write the given {@link CrawlResult}.
     */
    public CrawlResultWriter(CrawlResult result) {
        this.result = Objects.requireNonNull(result);
    }

    /**
     * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Path}.
     *
     * <p>If a file already exists at the path, the existing file should not be deleted; new data
     * should be appended to it.
     *
     * @param path the file path where the crawl result data should be written.
     */
    public void write(Path path) {
        // This is here to get rid of the unused variable warning.
        try (FileWriter writer = new FileWriter(Objects.requireNonNull(path).toFile(), true)) {
            write(writer);
        } catch (IOException ex) {
            ex.getLocalizedMessage();
        }
    }

    /**
     * Formats the {@link CrawlResult} as JSON and writes it to the given {@link Writer}.
     *
     * @param writer the destination where the crawl result data should be written.
     */
    public void write(Writer writer) {
        // This is here to get rid of the unused variable warning.
        try {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.disable(com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET);
            objectMapper.writeValue(Objects.requireNonNull(writer), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
