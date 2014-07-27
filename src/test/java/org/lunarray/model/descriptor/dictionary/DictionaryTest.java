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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.AbstractJpaTest;
import org.lunarray.model.descriptor.builder.annotation.resolver.attribute.jpa.JpaAttributeEntityResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.resolver.attribute.jpa.JpaAttributePropertyResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.resolver.entity.def.DefaultEntityAttributeResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.resolver.property.def.DefaultPropertyAttributeResolverStrategy;
import org.lunarray.model.descriptor.builder.annotation.simple.SimpleBuilder;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.SampleEntity01;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.resource.Resource;
import org.lunarray.model.descriptor.resource.jparesource.JpaResource;

/**
 * Test the JPA dictionary.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see DictionaryJpaImpl
 */
public class DictionaryTest
		extends AbstractJpaTest {

	/** The descriptor. */
	private KeyedEntityDescriptor<SampleEntity01, Long> descriptor;
	/** The JPA dictionary. */
	private DictionaryJpaImpl dictionary;
	/** The model. */
	private Model<Object> model;
	/** The resource. */
	private Resource<Class<? extends Object>> resource;

	/** Build the model and insert 500 elements. */
	@SuppressWarnings("unchecked")
	@Override
	@Before
	public void init() throws Exception {
		super.init();
		this.resource = new JpaResource<Object>("default-unit");
		final SimpleBuilder<Object> builder = SimpleBuilder.createBuilder();
		this.dictionary = new DictionaryJpaImpl("default-unit");
		builder.attributePropertyResolver(new JpaAttributePropertyResolverStrategy(new DefaultPropertyAttributeResolverStrategy()))
				.attributeEntityResolver(new JpaAttributeEntityResolverStrategy(new DefaultEntityAttributeResolverStrategy<Object>()))
				.resources(this.resource);
		this.model = builder.build();
		this.descriptor = this.model.getEntity(SampleEntity01.class).adapt(KeyedEntityDescriptor.class);
		final EntityManager em = this.getEmf().createEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		for (int i = 0; i < 500; i++) {
			final SampleEntity01 entity = new SampleEntity01();
			entity.setSample(new StringBuilder("entity-instance-").append(i).toString());
			em.persist(entity);
		}
		et.commit();
	}

	/**
	 * Test finding all elements.
	 * 
	 * @see PaginatedDictionary#lookupTotals(EntityDescriptor)
	 */
	@Test
	public void testCount() throws DictionaryException {
		Assert.assertEquals(500, this.dictionary.lookupTotals(this.descriptor));
	}

	/**
	 * Fetch all elements.
	 * 
	 * @see Dictionary#lookup(EntityDescriptor)
	 */
	@Test
	public void testFindAll() throws DictionaryException {
		Assert.assertEquals(500, this.dictionary.lookup(this.descriptor).size());
	}

	/**
	 * A single element.
	 * 
	 * @see Dictionary#lookup(KeyedEntityDescriptor, Serializable)
	 */
	@Test
	public void testFindOne() throws DictionaryException {
		final SampleEntity01 entity = this.dictionary.lookup(this.descriptor, Long.valueOf(50l));
		Assert.assertNotNull(entity);
		Assert.assertEquals("entity-instance-0", entity.getSample());
		Assert.assertEquals(50l, entity.getIdentifier().longValue());
	}

	/**
	 * Find 50 elements, starting with number 50.
	 * 
	 * @see PaginatedDictionary#lookupPaginated(EntityDescriptor, int, int)
	 */
	@Test
	public void testFindPaginated() throws DictionaryException {
		final List<SampleEntity01> entities = this.dictionary.lookupPaginated(this.descriptor, 50, 50);
		Assert.assertEquals(50, entities.size());
		Assert.assertEquals("entity-instance-50", entities.iterator().next().getSample());
	}
}
