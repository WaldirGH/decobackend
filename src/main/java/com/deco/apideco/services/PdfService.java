package com.deco.apideco.services;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deco.apideco.model.DetallePedido;
import com.deco.apideco.model.Pedido;
import com.deco.apideco.model.Enums.TipoComprobante;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class PdfService {

    public byte[] generarComprobante(Pedido pedido) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(25, 25, 25, 25);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            DecimalFormat df = new DecimalFormat("0.00");

            String serie = pedido.getTipoComprobante() == TipoComprobante.FACTURA ? "F001" : "B001";
            String numero = String.format("%06d", pedido.getId());
            String tipoComp = pedido.getTipoComprobante() == TipoComprobante.FACTURA
                    ? "FACTURA DE VENTA"
                    : "BOLETA DE VENTA";

            String fecha = pedido.getFechaCreacion()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            double total = pedido.getTotal();
            double base = total / 1.18;
            double igv = total - base;

            // CONTENEDOR PRINCIPAL
            Table marco = new Table(1);
            marco.setWidth(UnitValue.createPercentValue(100));
            marco.setBorder(new SolidBorder(1));

            Cell contenido = new Cell().setBorder(null).setPadding(10);

            // CABECERA DOS COLUMNAS
            Table cabecera = new Table(UnitValue.createPercentArray(new float[]{65, 35}));
            cabecera.setWidth(UnitValue.createPercentValue(100));

            Cell empresa = new Cell().setBorder(new SolidBorder(1)).setPadding(10);
            empresa.add(new Paragraph("DECOBALLOONS").setFont(bold).setFontSize(14));
            empresa.add(new Paragraph("RUC: 20512345678").setFont(bold).setFontSize(10));
            empresa.add(new Paragraph("Correo: decoballoonss@gmail.com").setFont(font).setFontSize(9));
            empresa.add(new Paragraph("Teléfono: 978633059").setFont(font).setFontSize(9));
            empresa.add(new Paragraph("Dirección: Calle Victor Alzamora 112").setFont(font).setFontSize(9));
            empresa.add(new Paragraph("La Victoria - Lima").setFont(font).setFontSize(9));

            Cell comprobante = new Cell().setBorder(new SolidBorder(1)).setPadding(10);
            comprobante.add(new Paragraph("R.U.C. N° 20512345678")
                    .setFont(bold).setFontSize(11).setTextAlignment(TextAlignment.CENTER));
            comprobante.add(new Paragraph(tipoComp)
                    .setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            comprobante.add(new Paragraph(serie + "-" + numero)
                    .setFont(bold).setFontSize(13).setTextAlignment(TextAlignment.CENTER));

            cabecera.addCell(empresa);
            cabecera.addCell(comprobante);
            contenido.add(cabecera);
            contenido.add(new Paragraph("\n"));

            // DATOS CLIENTE
            Table clienteTable = new Table(2);
            clienteTable.setWidth(UnitValue.createPercentValue(100));

            clienteTable.addCell(celdaLabel("Cliente:", bold));
            clienteTable.addCell(celdaValor(pedido.getNombreCliente(), font));

            clienteTable.addCell(celdaLabel("Teléfono:", bold));
            clienteTable.addCell(celdaValor(pedido.getTelefono(), font));

            clienteTable.addCell(celdaLabel("Fecha emisión:", bold));
            clienteTable.addCell(celdaValor(fecha, font));

            clienteTable.addCell(celdaLabel("Moneda:", bold));
            clienteTable.addCell(celdaValor("SOLES", font));

            if (pedido.getTipoComprobante() == TipoComprobante.FACTURA) {
                clienteTable.addCell(celdaLabel("RUC:", bold));
                clienteTable.addCell(celdaValor(pedido.getRuc(), font));

                clienteTable.addCell(celdaLabel("Razón social:", bold));
                clienteTable.addCell(celdaValor(pedido.getRazonSocial(), font));
            }

            contenido.add(clienteTable);
            contenido.add(new Paragraph("\n"));

            // TABLA DETALLE
            Table detalle = new Table(UnitValue.createPercentArray(new float[]{12, 52, 18, 18}));
            detalle.setWidth(UnitValue.createPercentValue(100));

            detalle.addHeaderCell(headerCell("Cant.", bold));
            detalle.addHeaderCell(headerCell("Descripción", bold));
            detalle.addHeaderCell(headerCell("P. Unit", bold));
            detalle.addHeaderCell(headerCell("Importe", bold));

            List<DetallePedido> detalles = pedido.getDetalles() != null ? pedido.getDetalles() : List.of();
            for (DetallePedido d : detalles) {
                double subtotal = d.getCantidad() * d.getPrecioUnitario();
                detalle.addCell(bodyCell(String.valueOf(d.getCantidad()), font, TextAlignment.CENTER));
                detalle.addCell(bodyCell(d.getProducto().getNombre(), font, TextAlignment.LEFT));
                detalle.addCell(bodyCell("S/ " + df.format(d.getPrecioUnitario()), font, TextAlignment.RIGHT));
                detalle.addCell(bodyCell("S/ " + df.format(subtotal), font, TextAlignment.RIGHT));
            }

            contenido.add(detalle);
            contenido.add(new Paragraph("\n"));

            // TOTALES
            Table totales = new Table(UnitValue.createPercentArray(new float[]{70, 30}));
            totales.setWidth(UnitValue.createPercentValue(100));

            totales.addCell(new Cell(1, 1).setBorder(null)
                    .add(new Paragraph("Gracias por su compra").setFont(font).setFontSize(10)));

            Table bloqueTotales = new Table(UnitValue.createPercentArray(new float[]{55, 45}));
            bloqueTotales.setWidth(UnitValue.createPercentValue(100));

            bloqueTotales.addCell(totalLabelCell("Op. gravada:", bold));
            bloqueTotales.addCell(totalValueCell("S/ " + df.format(base), font));

            bloqueTotales.addCell(totalLabelCell("IGV (18%):", bold));
            bloqueTotales.addCell(totalValueCell("S/ " + df.format(igv), font));

            bloqueTotales.addCell(totalLabelCell("TOTAL:", bold));
            bloqueTotales.addCell(totalValueCell("S/ " + df.format(total), bold));

            totales.addCell(new Cell().setBorder(new SolidBorder(1)).setPadding(6).add(bloqueTotales));

            contenido.add(totales);
            contenido.add(new Paragraph("\n"));
            contenido.add(new Paragraph("Representación impresa del comprobante")
                    .setFont(font).setFontSize(9).setTextAlignment(TextAlignment.CENTER));

            marco.addCell(contenido);
            document.add(marco);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    // ── helpers ──────────────────────────────────────────────

    private Cell celdaLabel(String texto, PdfFont bold) {
        return new Cell().setBorder(null).setPadding(4)
                .add(new Paragraph(texto).setFont(bold).setFontSize(10));
    }

    private Cell celdaValor(String texto, PdfFont font) {
        return new Cell().setBorder(null).setPadding(4)
                .add(new Paragraph(texto != null ? texto : "").setFont(font).setFontSize(10));
    }

    private Cell headerCell(String texto, PdfFont bold) {
        return new Cell().setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER).setPadding(5)
                .add(new Paragraph(texto).setFont(bold).setFontSize(10));
    }

    private Cell bodyCell(String texto, PdfFont font, TextAlignment align) {
        return new Cell().setTextAlignment(align).setPadding(5)
                .add(new Paragraph(texto).setFont(font).setFontSize(10));
    }

    private Cell totalLabelCell(String texto, PdfFont bold) {
        return new Cell().setBorder(null).setTextAlignment(TextAlignment.RIGHT).setPadding(4)
                .add(new Paragraph(texto).setFont(bold).setFontSize(10));
    }

    private Cell totalValueCell(String texto, PdfFont font) {
        return new Cell().setBorder(null).setTextAlignment(TextAlignment.RIGHT).setPadding(4)
                .add(new Paragraph(texto).setFont(font).setFontSize(10));
    }
}