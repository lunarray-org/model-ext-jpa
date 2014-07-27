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

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.lunarray.model.descriptor.accessor.property.DescribedProperty;
import org.lunarray.model.descriptor.builder.annotation.resolver.property.PropertyAttributeResolverStrategy;
import org.lunarray.model.descriptor.util.StringUtil;

/**
 * JPA based property attribute resolver.
 * 
 * Uses the following annotations:
 * <ul>
 * <li>Name uses {@link Column},</li>
 * <li>Key uses {@link Id} or {@link EmbeddedId},</li>
 * <li>Embedded uses {@link Embedded} or {@link EmbeddedId}.</li>
 * </ul>
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class JpaAttributePropertyResolverStrategy
		implements PropertyAttributeResolverStrategy {

	/** The attribute resolver delegate. */
	private final transient PropertyAttributeResolverStrategy delegate;

	/**
	 * Default constructor.
	 * 
	 * @param delegate
	 *            The attribute resolver delegate.
	 */
	public JpaAttributePropertyResolverStrategy(final PropertyAttributeResolverStrategy delegate) {
		this.delegate = delegate;
	}

	/** {@inheritDoc} */
	@Override
	public String getAlias(final DescribedProperty<?> property) {
		return this.delegate.getAlias(property);
	}

	/** {@inheritDoc} */
	@Override
	public String getName(final DescribedProperty<?> property) {
		String name = "";
		if (property.isAnnotationPresent(Column.class)) {
			final Iterator<Column> candidates = property.getAnnotation(Column.class).iterator();
			while ("".equals(name) && candidates.hasNext()) {
				name = candidates.next().name();
			}
		}
		if (StringUtil.isEmptyString(name)) {
			name = this.delegate.getName(property);
		}
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getReferenceRelation(final DescribedProperty<?> property) {
		return this.delegate.getReferenceRelation(property);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAlias(final DescribedProperty<?> property) {
		return this.delegate.isAlias(property);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmbedded(final DescribedProperty<?> property) {
		boolean embedded = false;
		embedded |= property.isAnnotationPresent(Embedded.class);
		return embedded || property.isAnnotationPresent(EmbeddedId.class);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isIgnore(final DescribedProperty<?> property) {
		return this.delegate.isIgnore(property);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isKey(final DescribedProperty<?> property) {
		boolean identifier = false;
		identifier |= property.isAnnotationPresent(Id.class);
		return identifier || property.isAnnotationPresent(EmbeddedId.class);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReference(final DescribedProperty<?> property) {
		return this.delegate.isReference(property);
	}
}
