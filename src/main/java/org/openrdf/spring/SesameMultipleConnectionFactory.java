package org.openrdf.spring;

import org.openrdf.repository.RepositoryException;

public interface SesameMultipleConnectionFactory extends SesameConnectionFactory {
	/**
	 * @deprecated use {@link #endTransaction(SesameTransactionObject)} instead.
	 */
	@Deprecated
	void endTransaction(boolean rollback) throws RepositoryException;

	void endTransaction(SesameTransactionObject transaction) throws RepositoryException;
}
