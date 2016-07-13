package org.eclipse.rdf4j.spring;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/repositoryTestContext.xml")
public abstract class BaseTest {
    @Autowired
    protected SesameConnectionFactory repositoryConnectionFactory;

    @Autowired
    protected SesameConnectionFactory repositoryManagerConnectionFactory;

    static void assertDataPresent(SesameConnectionFactory sesameConnectionFactory) throws Exception {
        RepositoryConnection connection = sesameConnectionFactory.getConnection();
        final TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, "SELECT ?s ?o WHERE { ?s <http://example.com/b> ?o . }");
        TupleQueryResult result = tupleQuery.evaluate();

        withTupleQueryResult(result, tupleQueryResult -> {
            Assert.assertTrue(tupleQueryResult.hasNext());

            BindingSet bindingSet = tupleQueryResult.next();

            Assert.assertEquals("http://example.com/a", bindingSet.getBinding("s").getValue().stringValue());
            Assert.assertEquals("http://example.com/c", bindingSet.getBinding("o").getValue().stringValue());
        });
    }

    private static void withTupleQueryResult(TupleQueryResult tupleQueryResult,
                                             TupleQueryResultHandler tupleQueryResultHandler) {
        try {
            tupleQueryResultHandler.handle(tupleQueryResult);
        } finally {
            tupleQueryResult.close();
        }
    }

    static void addData(SesameConnectionFactory sesameConnectionFactory) throws RepositoryException {
        ValueFactory f = SimpleValueFactory.getInstance();
        IRI a = f.createIRI("http://example.com/a");
        IRI b = f.createIRI("http://example.com/b");
        IRI c = f.createIRI("http://example.com/c");

        RepositoryConnection connection = sesameConnectionFactory.getConnection();
        connection.add(a, b, c);
    }

    interface TupleQueryResultHandler {
        void handle(TupleQueryResult tupleQueryResult);
    }
}
