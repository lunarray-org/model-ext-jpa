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
package org.lunarray.model.descriptor.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Test sample key.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class SampleKey01
		implements Serializable {

	/** The serial id. */
	private static final long serialVersionUID = 3927092577093795250L;

	private Long identifier;

	private String sample;

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * Gets the value for the identifier field.
	 * 
	 * @return The value for the identifier field.
	 */
	public final Long getIdentifier() {
		return this.identifier;
	}

	/**
	 * Gets the value for the sample field.
	 * 
	 * @return The value for the sample field.
	 */
	public final String getSample() {
		return this.sample;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Sets a new value for the identifier field.
	 * 
	 * @param identifier
	 *            The new value for the identifier field.
	 */
	public final void setIdentifier(final Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * Sets a new value for the sample field.
	 * 
	 * @param sample
	 *            The new value for the sample field.
	 */
	public final void setSample(final String sample) {
		this.sample = sample;
	}
}
