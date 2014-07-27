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

import junit.framework.Assert;

import org.junit.Test;
import org.lunarray.model.descriptor.AbstractJpaTest;
import org.lunarray.model.descriptor.model.SampleEntity01;
import org.lunarray.model.descriptor.model.SampleEntity02;
import org.lunarray.model.descriptor.resource.Resource;

/**
 * Test resource resolving.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see JpaResource
 */
public class ResourceTest
		extends AbstractJpaTest {

	/**
	 * Test resource resolving.
	 * 
	 * @see Resource#getResources()
	 */
	@Test
	public void testResource() throws Exception {
		final Resource<Class<? extends Object>> resource = new JpaResource<Object>("default-unit");
		Assert.assertTrue(resource.getResources().contains(SampleEntity01.class));
		Assert.assertTrue(resource.getResources().contains(SampleEntity02.class));
	}
}
