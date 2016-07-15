package org.eclipse.rdf4j.spring;

import org.eclipse.rdf4j.IsolationLevel;
import org.eclipse.rdf4j.IsolationLevels;
import org.eclipse.rdf4j.sail.Sail;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;

/**
 * <p>Adapter to convert spring {@link TransactionDefinition} isolation levels to corresponding OpenRDF
 * {@link org.eclipse.rdf4j.IsolationLevel}s.</p>
 * <p/>
 * <p>The conversion depends on the provided {@link org.eclipse.rdf4j.sail.Sail} and its transaction capabilities.
 * If the {@link org.eclipse.rdf4j.sail.Sail} is not compatible with the provided isolation level, an
 * {@link org.springframework.transaction.InvalidIsolationLevelException} is thrown.<p/>
 *
 * @author ameingast@gmail.com
 */
class IsolationLevelAdapter {
    static IsolationLevel adaptToRdfIsolation(Sail sail, int springIsolation) {
        switch (springIsolation) {
            case TransactionDefinition.ISOLATION_DEFAULT:
                return sail.getDefaultIsolationLevel();
            case TransactionDefinition.ISOLATION_READ_COMMITTED:
                return determineIsolationLevel(sail, IsolationLevels.READ_COMMITTED);
            case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
                return determineIsolationLevel(sail, IsolationLevels.READ_UNCOMMITTED);
            case TransactionDefinition.ISOLATION_REPEATABLE_READ:
                throw new InvalidIsolationLevelException("Unsupported isolation level for sail: " + sail + ": " + springIsolation);
            case TransactionDefinition.ISOLATION_SERIALIZABLE:
                return determineIsolationLevel(sail, IsolationLevels.SERIALIZABLE);
            default:
                throw new InvalidIsolationLevelException("Unsupported isolation level for sail: " + sail + ": " + springIsolation);
        }
    }

    private static IsolationLevel determineIsolationLevel(Sail sail, IsolationLevel isolationLevel) {
        if (sail.getSupportedIsolationLevels().contains(isolationLevel)) {
            return isolationLevel;
        } else {
            throw new InvalidIsolationLevelException("Unsupported isolation level for sail: " + sail + ": " + isolationLevel);
        }
    }
}