/**
 * <p>Sesame-Spring provides a simple Spring {@link org.springframework.transaction.PlatformTransactionManager} for
 * sesame {@link org.eclipse.rdf4j.repository.Repository}s and {@link org.eclipse.rdf4j.repository.manager.RepositoryManager}s.</p>
 * <p/>
 * <p>The transaction scope is thread-local.</p>
 * <p/>
 * <p>{@link org.eclipse.rdf4j.repository.RepositoryConnection}s to the underlying repository are automatically
 * opened when the transaction begins and they are always closed when the transaction terminates.</p>
 * <p/>
 * <p>Nested transactions are not supported; in case of re-opening a transaction, the current transaction will
 * be re-used.</p>
 *
 * @author ameingast@gmail.com
 */
package org.eclipse.rdf4j.spring;