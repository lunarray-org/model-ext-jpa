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
package org.lunarray.model.descriptor.resource.jparesource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource retrieves all resources based on a JPA persistence unit.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            A marker type.
 */
public final class JpaResource<S>
		implements Resource<Class<? extends S>> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaResource.class);
	/** The marker type. */
	private final transient Class<S> markerType;
	/** The persistence unit name. */
	private final transient String persistenceUnitName;
	/** The resource cache. */
	private transient Set<Class<? extends S>> resourceCache;

	/**
	 * Construct the jpa resource with a given persistence unit name.
	 * 
	 * @param persistentUnitName
	 *            The persistence unit name. May not be null.
	 */
	public JpaResource(final String persistentUnitName) {
		this(persistentUnitName, null);
	}

	/**
	 * Constructs the jpa resource with both a persistence unit name and a
	 * marker type.
	 * 
	 * @param persistentUnitName
	 *            The unit name. May not be null.
	 * @param markerType
	 *            The marker type.
	 */
	public JpaResource(final String persistentUnitName, final Class<S> markerType) {
		Validate.notEmpty(persistentUnitName, "Persistent unit may not be null.");
		this.persistenceUnitName = persistentUnitName;
		this.markerType = markerType;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Class<? extends S>> getResources() {
		if (CheckUtil.isNull(this.resourceCache)) {
			this.init();
		}
		return this.resourceCache;
	}

	/**
	 * Gets the JPA meta model.
	 * 
	 * @return The JPA meta model.
	 */
	private Metamodel getModel() {
		return Persistence.createEntityManagerFactory(this.persistenceUnitName).getMetamodel();
	}

	/**
	 * Initializes the resource cache.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		final Set<Class<? extends S>> temporaryClazzCache = new HashSet<Class<? extends S>>();
		for (final EntityType<?> entityType : this.getModel().getEntities()) {
			if (CheckUtil.isNull(this.markerType) || this.markerType.isAssignableFrom(entityType.getJavaType())) {
				temporaryClazzCache.add((Class<? extends S>) entityType.getJavaType());
			}
		}
		JpaResource.LOGGER.debug("Found {} entity descriptors for persistence unit '{}'.", temporaryClazzCache.size(),
				this.persistenceUnitName);
		this.resourceCache = temporaryClazzCache;
	}
}
