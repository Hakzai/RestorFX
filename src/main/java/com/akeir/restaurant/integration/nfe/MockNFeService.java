package com.akeir.restaurant.integration.nfe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;

public class MockNFeService implements NFeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockNFeService.class);
    private static final AtomicLong SEQUENCE = new AtomicLong(100000000000L);

    @Override
    public NFeEmissionResult emit(NFeEmissionRequest request) {
        validateRequest(request);

        String issueDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String accessKey = generateAccessKey();
        String protocol = "MOCK-" + SEQUENCE.incrementAndGet();
        String xml = buildMockXml(request, issueDate, accessKey, protocol);

        LOGGER.info("Mock NFe emitted with access key {}", accessKey);
        LOGGER.info("Mock NFe XML output:\n{}", xml);

        return new NFeEmissionResult("AUTHORIZED_MOCK", accessKey, protocol, xml);
    }

    private void validateRequest(NFeEmissionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("NFe request cannot be null");
        }

        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required for NFe emission");
        }

        if (request.getTotalCents() <= 0) {
            throw new IllegalArgumentException("NFe total amount must be greater than zero");
        }
    }

    private String generateAccessKey() {
        String number = String.valueOf(SEQUENCE.incrementAndGet());
        return leftPad(number, 44);
    }

    private String leftPad(String value, int length) {
        StringBuilder builder = new StringBuilder(value == null ? "" : value);
        while (builder.length() < length) {
            builder.insert(0, '0');
        }
        if (builder.length() > length) {
            return builder.substring(builder.length() - length);
        }
        return builder.toString();
    }

    private String buildMockXml(NFeEmissionRequest request, String issueDate, String accessKey, String protocol) {
        StringBuilder xml = new StringBuilder();
        xml.append("<nfeProc version=\"4.00\" mock=\"true\">\n");
        xml.append("  <NFe>\n");
        xml.append("    <infNFe Id=\"NFe").append(accessKey).append("\">\n");
        xml.append("      <ide><dhEmi>").append(issueDate).append("</dhEmi></ide>\n");
        xml.append("      <emit><xNome>Restaurant Management Mock Issuer</xNome></emit>\n");
        xml.append("      <dest>\n");
        xml.append("        <xNome>").append(escapeXml(request.getCustomerName())).append("</xNome>\n");
        if (request.getCustomerDocument() != null && !request.getCustomerDocument().trim().isEmpty()) {
            xml.append("        <CPF>").append(escapeXml(request.getCustomerDocument())).append("</CPF>\n");
        }
        xml.append("      </dest>\n");
        xml.append("      <total><vNF>").append(formatCents(request.getTotalCents())).append("</vNF></total>\n");
        if (request.getNotes() != null && !request.getNotes().trim().isEmpty()) {
            xml.append("      <infCpl>").append(escapeXml(request.getNotes())).append("</infCpl>\n");
        }
        xml.append("    </infNFe>\n");
        xml.append("  </NFe>\n");
        xml.append("  <protNFe><nProt>").append(protocol).append("</nProt></protNFe>\n");
        xml.append("</nfeProc>");
        return xml.toString();
    }

    private String formatCents(int cents) {
        return BigDecimal.valueOf(cents, 2).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String escapeXml(String value) {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }
}