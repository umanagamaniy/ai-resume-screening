package com.uma.airesumescreening;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    /**
     * Extracts text content from a PDF file.
     *
     * @param pdfFile the PDF file to read
     * @return extracted text as a String
     * @throws IOException if the file cannot be read or is not a valid PDF
     */
    public String extractTextFromPdf(File pdfFile) throws IOException {

        // Load the PDF document
        try (PDDocument document = Loader.loadPDF(pdfFile)) {

            // Create a text stripper (converts PDF -> plain text)
            PDFTextStripper stripper = new PDFTextStripper();

            // Extract text from all pages
            String text = stripper.getText(document);

            return text;
        }
        // try-with-resources auto-closes the PDDocument (important - avoids memory leaks)
    }
}