package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Alquiler;
import com.example.adrentar.service.ContratoPdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service

public class ContratoPdfServiceImpl implements ContratoPdfService {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final float MARGIN = 50;
    private static final float LEADING = 18;

    /**
     * Genera el PDF del contrato de alquiler con los datos reales de la entidad.
     * No depende de ningún archivo local: se construye enteramente en memoria
     * en el momento en que se llama.
     */
    public byte[] generarContratoPdf(Alquiler alquiler) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float y = page.getMediaBox().getHeight() - MARGIN;

            String direccion = alquiler.getPropiedad() != null ? alquiler.getPropiedad().getDireccion() : "-";
            String tipo = alquiler.getPropiedad() != null ? alquiler.getPropiedad().getTipo() : "-";
            int ambientes = alquiler.getPropiedad() != null ? alquiler.getPropiedad().getAmbientes() : 0;

            String propietarioNombre = alquiler.getPropietario() != null ? alquiler.getPropietario().getNombre() : "-";
            String propietarioEmail = alquiler.getPropietario() != null ? alquiler.getPropietario().getEmail() : "-";
            String inquilinoNombre = alquiler.getInquilino() != null ? alquiler.getInquilino().getNombre() : "-";
            String inquilinoEmail = alquiler.getInquilino() != null ? alquiler.getInquilino().getEmail() : "-";

            String fechaInicio = alquiler.getFechaInicio() != null ? DATE_FORMAT.format(alquiler.getFechaInicio()) : "-";
            String fechaFin = alquiler.getFechaFin() != null ? DATE_FORMAT.format(alquiler.getFechaFin()) : "-";

            List<String> lineas = new ArrayList<>();
            lineas.add("Entre las partes mencionadas a continuacion se celebra el presente contrato de");
            lineas.add("alquiler, sujeto a las clausulas y condiciones que se detallan a continuacion:");
            lineas.add("");
            lineas.add("PROPIEDAD");
            lineas.add("Direccion: " + direccion);
            lineas.add("Tipo: " + tipo + "   Ambientes: " + ambientes);
            lineas.add("");
            lineas.add("PROPIETARIO");
            lineas.add("Nombre: " + propietarioNombre);
            lineas.add("Email: " + propietarioEmail);
            lineas.add("");
            lineas.add("INQUILINO");
            lineas.add("Nombre: " + inquilinoNombre);
            lineas.add("Email: " + inquilinoEmail);
            lineas.add("");
            lineas.add("CONDICIONES");
            lineas.add("Fecha de inicio: " + fechaInicio);
            lineas.add("Fecha de fin: " + fechaFin);
            lineas.add("Precio mensual: $" + String.format("%.2f", alquiler.getPrecio()));
            lineas.add("");
            lineas.add("Ambas partes declaran haber leido y aceptado los terminos del presente");
            lineas.add("contrato, comprometiendose a su cumplimiento durante la vigencia del alquiler.");

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                // Titulo
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 16);
                content.newLineAtOffset(MARGIN, y);
                content.showText("CONTRATO DE ALQUILER");
                content.endText();
                y -= LEADING * 2;

                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 16);
                for (String linea : lineas) {
                    if (y < MARGIN + 140) {
                        // Contrato simple de una pagina; si crece, convendria paginar.
                        break;
                    }
                    content.beginText();
                    content.newLineAtOffset(MARGIN, y);
                    content.showText(sanitizar(linea));
                    content.endText();
                    y -= LEADING;
                }

                // Marcadores de texto que DocuSign usa como anclas para ubicar
                // los tabs de firma (mismo patron que el PDF de prueba anterior).
                y -= LEADING * 2;
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 16);

                content.beginText();
                content.newLineAtOffset(MARGIN, y);
                content.showText("Firma del propietario:");
                content.endText();

                content.beginText();
                content.newLineAtOffset(MARGIN, y - LEADING);
                content.showText("/firma-propietario/");
                content.endText();

                y -= LEADING * 4;

                content.beginText();
                content.newLineAtOffset(MARGIN, y);
                content.showText("Firma del inquilino:");
                content.endText();

                content.beginText();
                content.newLineAtOffset(MARGIN, y - LEADING);
                content.showText("/firma-inquilino/");
                content.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }

    /**
     * PDType1Font.HELVETICA solo soporta WinAnsiEncoding (Latin-1). Reemplaza
     * acentos/eñes por su equivalente sin tilde para evitar errores de codificacion.
     */
    public String sanitizar(String texto) {
        return texto
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u")
                .replace("Á", "A").replace("É", "E").replace("Í", "I")
                .replace("Ó", "O").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N");
    }
}
