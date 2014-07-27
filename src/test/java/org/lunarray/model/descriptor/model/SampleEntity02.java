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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Test sample entity.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
@Entity
@Table(name = "sampleentity02")
@SequenceGenerator(name = "sample-entity-02-sequence", sequenceName = "sampleentity02sequence")
public class SampleEntity02 {

	@OneToOne
	private SampleEntity01 entity;

	@EmbeddedId
	private SampleKey01 key;

	@Column(name = "testColumn")
	private String test;

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	public SampleEntity01 getEntity() {
		return this.entity;
	}

	/**
	 * Gets the value for the key field.
	 * 
	 * @return The value for the key field.
	 */
	public SampleKey01 getKey() {
		return this.key;
	}

	/**
	 * Gets the value for the test field.
	 * 
	 * @return The value for the test field.
	 */
	public String getTest() {
		return this.test;
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public void setEntity(final SampleEntity01 entity) {
		this.entity = entity;
	}

	/**
	 * Sets a new value for the key field.
	 * 
	 * @param key
	 *            The new value for the key field.
	 */
	public void setKey(final SampleKey01 key) {
		this.key = key;
	}

	/**
	 * Sets a new value for the test field.
	 * 
	 * @param test
	 *            The new value for the test field.
	 */
	public void setTest(final String test) {
		this.test = test;
	}
}
