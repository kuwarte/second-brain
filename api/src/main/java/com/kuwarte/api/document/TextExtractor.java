package com.kuwarte.api.document;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class TextExtractor {

    public static final Set<String> SUPPORTED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/markdown",
            "text/plain");

    public String extract(InputStream inputStream, String contentType) {
        try {
            return switch (contentType) {
                case "application/pdf" -> extractPdf(inputStream);
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                    extractDocx(inputStream);
                case "text/markdown", "text/plain" -> extractPlainText(inputStream);
                default -> throw new UnsupportedFileTypeException(contentType);
            };
        } catch (IOException e) {
            throw new TextExtractionException("Failed to extract text from file", e);
        }
    }

    private String extractPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            return new PDFTextStripper().getText(document);
        }
    }

    private String extractDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    private String extractPlainText(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static class UnsupportedFileTypeException extends RuntimeException {
        public UnsupportedFileTypeException(String contentType) {
            super("Unsupported file type: " + contentType);
        }
    }

    public static class TextExtractionException extends RuntimeException {
        public TextExtractionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
