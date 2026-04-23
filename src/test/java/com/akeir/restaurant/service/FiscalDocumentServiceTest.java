package com.akeir.restaurant.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;
import com.akeir.restaurant.model.FiscalDocument;
import com.akeir.restaurant.repository.FiscalDocumentRepository;

public class FiscalDocumentServiceTest {

    @Test
    public void recordNFeSuccessShouldPersistAuditEntry() throws SQLException {
        StubFiscalDocumentRepository repository = new StubFiscalDocumentRepository();
        FiscalDocumentService service = new FiscalDocumentService(repository);

        NFeEmissionRequest request = new NFeEmissionRequest("  Maria Silva  ", " 12345678910 ", 2250, "note");
        NFeEmissionResult result = new NFeEmissionResult("AUTHORIZED_MOCK", "KEY", "PROTO", "<nfe/>");

        FiscalDocument created = service.recordNFeSuccess(request, result);

        assertNotNull(created);
        assertEquals(Long.valueOf(801L), created.getId());
        assertEquals("NFE", created.getDocumentType());
        assertEquals("AUTHORIZED_MOCK", created.getStatus());
        assertEquals("Maria Silva", repository.getLastCreated().getCustomerName());
        assertEquals("12345678910", repository.getLastCreated().getCustomerDocument());
    }

    @Test(expected = IllegalArgumentException.class)
    public void recordNFeSuccessShouldRejectNullRequest() throws SQLException {
        FiscalDocumentService service = new FiscalDocumentService(new StubFiscalDocumentRepository());
        NFeEmissionResult result = new NFeEmissionResult("AUTHORIZED_MOCK", "KEY", "PROTO", "<nfe/>");
        service.recordNFeSuccess(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void recordNFeSuccessShouldRejectInvalidTotal() throws SQLException {
        FiscalDocumentService service = new FiscalDocumentService(new StubFiscalDocumentRepository());
        NFeEmissionRequest request = new NFeEmissionRequest("Maria", "123", 0, null);
        NFeEmissionResult result = new NFeEmissionResult("AUTHORIZED_MOCK", "KEY", "PROTO", "<nfe/>");
        service.recordNFeSuccess(request, result);
    }

    private static final class StubFiscalDocumentRepository extends FiscalDocumentRepository {

        private final List<FiscalDocument> documents = new ArrayList<FiscalDocument>();
        private FiscalDocument lastCreated;

        @Override
        public long create(FiscalDocument fiscalDocument) {
            FiscalDocument stored = new FiscalDocument(
                801L,
                fiscalDocument.getDocumentType(),
                fiscalDocument.getStatus(),
                fiscalDocument.getCustomerName(),
                fiscalDocument.getCustomerDocument(),
                fiscalDocument.getTotalCents(),
                fiscalDocument.getAccessKey(),
                fiscalDocument.getProtocol(),
                fiscalDocument.getXml(),
                fiscalDocument.getErrorMessage(),
                fiscalDocument.getCreatedAt()
            );

            lastCreated = stored;
            documents.add(stored);
            return 801L;
        }

        @Override
        public List<FiscalDocument> findRecent(int limit) {
            return new ArrayList<FiscalDocument>(documents);
        }

        @Override
        public int countAll() {
            return documents.size();
        }

        private FiscalDocument getLastCreated() {
            return lastCreated;
        }
    }
}
