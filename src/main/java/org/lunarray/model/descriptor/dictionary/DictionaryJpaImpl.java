/* 
 * Model Tools.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.model.descriptor.dictionary;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPA based dictionary implementation.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DictionaryJpaImpl
		implements PaginatedDictionary {

	/** Validation message. */
	private static final String ENTITY_DESCRIPTOR_NULL = "Entity descriptor may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryJpaImpl.class);
	/** The Entity manager. */
	private transient EntityManager entityManager;
	/** The JPA repository. */
	private transient EntityManagerFactory entityManagerFactory;

	/**
	 * Default constructor.
	 * 
	 * @param entityManager
	 *            The {@link EntityManager} to use.
	 */
	public DictionaryJpaImpl(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Default constructor.
	 * 
	 * @param entityManagerFactory
	 *            The {@link EntityManagerFactory} to get the
	 *            {@link EntityManager} for.
	 */
	public DictionaryJpaImpl(final EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * Default constructor.
	 * 
	 * @param unitName
	 *            The unit name to get the {@link EntityManagerFactory} for.
	 */
	public DictionaryJpaImpl(final String unitName) {
		this(Persistence.createEntityManagerFactory(unitName));
	}

	/** {@inheritDoc} */
	@Override
	public <E> Collection<E> lookup(final EntityDescriptor<E> entityDescriptor) throws DictionaryException {
		DictionaryJpaImpl.LOGGER.debug("Finding all entities for {}", entityDescriptor);
		Validate.notNull(entityDescriptor, DictionaryJpaImpl.ENTITY_DESCRIPTOR_NULL);
		final Class<E> entityType = entityDescriptor.getEntityType();
		final EntityManager manager = this.getEntityManager();
		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<E> query = builder.createQuery(entityType);
		query.select(query.from(entityType));
		final TypedQuery<E> typedQuery = manager.createQuery(query);
		return typedQuery.getResultList();
	}

	/** {@inheritDoc} */
	@Override
	public <E, K extends Serializable> E lookup(final KeyedEntityDescriptor<E, K> entityDescriptor, final K key) throws DictionaryException {
		DictionaryJpaImpl.LOGGER.debug("Finding entity with key '{}' for {}", key, entityDescriptor);
		Validate.notNull(entityDescriptor, DictionaryJpaImpl.ENTITY_DESCRIPTOR_NULL);
		final EntityManager manager = this.getEntityManager();
		return manager.find(entityDescriptor.getEntityType(), key);
	}

	/** {@inheritDoc} */
	@Override
	public <E> List<E> lookupPaginated(final EntityDescriptor<E> entityDescriptor, final int row, final int count)
			throws DictionaryException {
		DictionaryJpaImpl.LOGGER.debug("Finding {} entities starting at {} for {}", count, row, entityDescriptor);
		Validate.notNull(entityDescriptor, DictionaryJpaImpl.ENTITY_DESCRIPTOR_NULL);
		Validate.isTrue(CheckUtil.checkPositive(count), "Count is not positive.");
		Validate.isTrue(CheckUtil.checkPositive(row), "Row is not positive.");
		final Class<E> entityType = entityDescriptor.getEntityType();
		final EntityManager manager = this.getEntityManager();
		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<E> query = builder.createQuery(entityType);
		query.select(query.from(entityType));
		final TypedQuery<E> typedQuery = manager.createQuery(query);
		typedQuery.setFirstResult(row);
		typedQuery.setMaxResults(count);
		return typedQuery.getResultList();
	}

	/** {@inheritDoc} */
	@Override
	public <E> int lookupTotals(final EntityDescriptor<E> entityDescriptor) throws DictionaryException {
		DictionaryJpaImpl.LOGGER.debug("Counting entities for {}", entityDescriptor);
		Validate.notNull(entityDescriptor, DictionaryJpaImpl.ENTITY_DESCRIPTOR_NULL);
		final EntityManager manager = this.getEntityManager();
		final CriteriaBuilder builder = manager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(entityDescriptor.getEntityType())));
		final TypedQuery<Long> typedQuery = manager.createQuery(query);
		return typedQuery.getSingleResult().intValue();
	}

	/**
	 * Gets an entity manager.
	 * 
	 * @return The entity manager.
	 */
	private EntityManager getEntityManager() {
		EntityManager result = this.entityManager;
		if (CheckUtil.isNull(result)) {
			result = this.entityManagerFactory.createEntityManager();
		}
		Validate.notNull(result, "Entity manager was null.");
		return result;
	}
}
