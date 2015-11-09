package org.sipr.couchdb.dao;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.sipr.core.dao.RegistrationBindingsRepository;
import org.sipr.couchdb.domain.CouchDBRegistrationBinding;

import java.util.ArrayList;
import java.util.List;

public class CouchDBRegistrationsRepository extends CouchDbRepositorySupport<CouchDBRegistrationBinding> implements RegistrationBindingsRepository<CouchDBRegistrationBinding> {

    public CouchDBRegistrationsRepository(CouchDbConnector db) {
        super(CouchDBRegistrationBinding.class, db);
        initStandardDesignDocument();
    }

    public void deleteBindings(List<CouchDBRegistrationBinding> bindings) {
        List<BulkDeleteDocument> bulkDocs = new ArrayList<BulkDeleteDocument>();

        for (CouchDBRegistrationBinding regBinding : bindings) {
            bulkDocs.add(BulkDeleteDocument.of(regBinding));
        }
        db.executeBulk(bulkDocs).size();
    }

    public void saveBindings(List<CouchDBRegistrationBinding> bindings) {
        db.executeBulk(bindings);
    }

    @Override
    public Long deleteByUserName(String userName) {
        List<CouchDBRegistrationBinding> regBindings = findByUserName(userName);
        List<BulkDeleteDocument> bulkDocs = new ArrayList<BulkDeleteDocument>();

        for (CouchDBRegistrationBinding regBinding : regBindings) {
            bulkDocs.add(BulkDeleteDocument.of(regBinding));
        }
        return Long.valueOf(db.executeBulk(bulkDocs).size());
    }

    @Override
    @GenerateView
    public List<CouchDBRegistrationBinding> findByUserName(String userName) {
        List<CouchDBRegistrationBinding> regBindings = queryView("by_userName", userName);
        return regBindings;
    }

    @Override
    @GenerateView
    public List<CouchDBRegistrationBinding> findByContact(String contact) {
        List<CouchDBRegistrationBinding> regBindings = queryView("by_contact", contact);
        return regBindings;
    }

    public void deleteBinding(CouchDBRegistrationBinding binding) {
        remove(binding);
    }

    public void saveBinding(CouchDBRegistrationBinding binding) {
        db.create(binding);
    }
}
