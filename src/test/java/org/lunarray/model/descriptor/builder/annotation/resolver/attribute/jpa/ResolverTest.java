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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.AbstractJpaTest;
import org.lunarray.model.descriptor.accessor.entity.DescribedEntity;
import org.lunarray.model.descriptor.accessor.property.DescribedProperty;
import org.lunarray.model.descriptor.builder.annotation.resolver.entity.EntityAttributeResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.resolver.entity.def.DefaultEntityAttributeResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.resolver.property.PropertyAttributeResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.resolver.property.def.DefaultPropertyAttributeResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.simple.SimpleBuilder;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.SampleEntity01;
import org.lunarray.model.descriptor.model.SampleEntity02;
import org.lunarray.model.descriptor.model.SampleKey01;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.resource.Resource;
import org.lunarray.model.descriptor.resource.jparesource.JpaResource;

/**
 * Test the resolver.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see JpaAttributeEntityResolverStrategy
 * @see JpaAttributePropertyResolverStrategy
 */
public class ResolverTest
		extends AbstractJpaTest {
	/** The model. */
	private Model<Object> model;
	/** The resource. */
	private Resource<Class<? extends Object>> resource;

	/** Initialize the test. */
	@Override
	@Before
	public void init() throws Exception {
		super.init();
		this.resource = new JpaResource<Object>("default-unit");
		final SimpleBuilder<Object> builder = SimpleBuilder.createBuilder();
		builder.attributePropertyResolver(new JpaAttributePropertyResolverStrategy(new DefaultPropertyAttributeResolverStrategy()))
				.attributeEntityResolver(new JpaAttributeEntityResolverStrategy(new DefaultEntityAttributeResolverStrategy<Object>()))
				.resources(this.resource);
		this.model = builder.build();
	}

	/**
	 * Test name resolving.
	 * 
	 * @see Column
	 * @see PropertyAttributeResolverStrategy#getName(DescribedProperty)
	 */
	@Test
	public void testColumnname() {
		final EntityDescriptor<SampleEntity02> descriptor02 = this.model.getEntity(SampleEntity02.class);
		Assert.assertNotNull(descriptor02);
		Assert.assertNotNull(descriptor02.getProperty("testColumn"));
	}

	/**
	 * Test key resolving.
	 * 
	 * @see EmbeddedId
	 * @see PropertyAttributeResolverStrategy#isKey(DescribedProperty)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEmbeddedKey() {
		final EntityDescriptor<SampleEntity02> descriptor01 = this.model.getEntity(SampleEntity02.class);
		Assert.assertNotNull(descriptor01);
		final KeyedEntityDescriptor<SampleEntity02, Long> descriptor02 = descriptor01.adapt(KeyedEntityDescriptor.class);
		Assert.assertNotNull(descriptor02);
		Assert.assertNotNull(descriptor02.getKeyProperty());
		final PropertyDescriptor<Long, SampleEntity02> descriptor03 = descriptor02.getKeyProperty();
		Assert.assertNotNull(descriptor03);
		Assert.assertEquals(SampleKey01.class, descriptor03.getPropertyType());
	}

	/**
	 * Test key resolving.
	 * 
	 * @see Id
	 * @see PropertyAttributeResolverStrategy#isKey(DescribedProperty)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testKey() {
		final EntityDescriptor<SampleEntity01> descriptor01 = this.model.getEntity(SampleEntity01.class);
		Assert.assertNotNull(descriptor01);
		final KeyedEntityDescriptor<SampleEntity01, Long> descriptor02 = descriptor01.adapt(KeyedEntityDescriptor.class);
		Assert.assertNotNull(descriptor02);
		Assert.assertNotNull(descriptor02.getKeyProperty());
		final PropertyDescriptor<Long, SampleEntity01> descriptor03 = descriptor02.getKeyProperty();
		Assert.assertNotNull(descriptor03);
		Assert.assertEquals(Long.class, descriptor03.getPropertyType());
	}

	/**
	 * Test name resolving.
	 * 
	 * @see Entity
	 * @see EntityAttributeResolverStrategy#getName(DescribedEntity)
	 */
	@Test
	public void testName() {
		final EntityDescriptor<SampleEntity01> descriptor01 = this.model.getEntity(SampleEntity01.class);
		Assert.assertNotNull(descriptor01);
		Assert.assertEquals("sample-entity-01", descriptor01.getName());
	}
}
