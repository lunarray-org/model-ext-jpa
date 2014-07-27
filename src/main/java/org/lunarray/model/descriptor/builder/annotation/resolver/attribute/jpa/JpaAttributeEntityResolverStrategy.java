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
package org.lunarray.model.descriptor.builder.annotation.resolver.attribute.jpa;

import java.util.Iterator;

import javax.persistence.Entity;

import org.lunarray.model.descriptor.accessor.entity.DescribedEntity;
import org.lunarray.model.descriptor.builder.annotation.resolver.entity.EntityAttributeResolverStrategy;
import org.lunarray.model.descriptor.util.StringUtil;

/**
 * JPA based entity resolver strategy.
 * 
 * Uses the following annotations:
 * <ul>
 * <li>Name uses {@link Entity}.</li>
 * </ul>
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class JpaAttributeEntityResolverStrategy
		implements EntityAttributeResolverStrategy {

	/** The attribute resolver delegate. */
	private final transient EntityAttributeResolverStrategy delegate;

	/**
	 * Default constructor.
	 * 
	 * @param delegate
	 *            The attribute resolver delegate.
	 */
	public JpaAttributeEntityResolverStrategy(final EntityAttributeResolverStrategy delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getName(final DescribedEntity<?> entityType) {
		String name = "";
		if (entityType.isAnnotationPresent(Entity.class)) {
			final Iterator<Entity> candidates = entityType.getAnnotation(Entity.class).iterator();
			while ("".equals(name) && candidates.hasNext()) {
				name = candidates.next().name();
			}
		}
		if (StringUtil.isEmptyString(name)) {
			name = this.delegate.getName(entityType);
		}
		return name;
	}
}
