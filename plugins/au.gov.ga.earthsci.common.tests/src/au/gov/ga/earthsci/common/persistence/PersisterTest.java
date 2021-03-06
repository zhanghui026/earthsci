/*******************************************************************************
 * Copyright 2012 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.earthsci.common.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nasa.worldwind.util.WWXML;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.gov.ga.earthsci.common.persistence.IPersistentAdapter;
import au.gov.ga.earthsci.common.persistence.PersistenceException;
import au.gov.ga.earthsci.common.persistence.Persister;
import au.gov.ga.earthsci.common.persistence.ExportableWithAdapter.Adaptable;
import au.gov.ga.earthsci.common.util.XmlUtil;

/**
 * JUnit tests for the {@link Persister} class.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class PersisterTest
{
	@Test
	public void testArray() throws PersistenceException
	{
		ExportableWithArray array = new ExportableWithArray();
		array.setArray(new double[] { 1, 3, 5, 7, 9, 38742.5463 });
		performTest(array, "testArray.xml");
	}

	@Test
	public void testCollection() throws PersistenceException
	{
		ExportableWithCollection collection = new ExportableWithCollection();
		List<Integer> list = new ArrayList<Integer>();
		list.add(5);
		list.add(7);
		list.add(435);
		collection.setCollection(list);
		performTest(collection, "testCollection.xml");
	}

	@Test
	public void testArrayList() throws PersistenceException
	{
		ExportableWithArrayList arrayList = new ExportableWithArrayList();
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(5);
		list.add(7);
		list.add(435);
		arrayList.setArrayList(list);
		performTest(arrayList, "testArrayList.xml");
	}

	@Test
	public void testDoubleArray() throws PersistenceException
	{
		ExportableWithDoubleArray doubleArray = new ExportableWithDoubleArray();
		doubleArray.setCollectionArray(new ExportableWithCollection[3][5]);
		performTest(doubleArray, "testDoubleArray.xml");
	}

	@Test
	public void testAttribute() throws PersistenceException
	{
		ExportableWithAttribute attribute = new ExportableWithAttribute();
		attribute.setAttribute(5);
		performTest(attribute, "testAttribute.xml");
	}

	@Test
	public void testAdapter() throws PersistenceException
	{
		ExportableWithAdapter adapter = new ExportableWithAdapter();
		Adaptable adaptable = new Adaptable();
		adaptable.setValue("adaptable value");
		adapter.setAdaptable(adaptable);
		performTest(adapter, "testAdapter.xml");
	}

	@Test
	public void testMethods() throws PersistenceException
	{
		ExportableWithMethods methods = new ExportableWithMethods();
		methods.setField(5);
		methods.setMethod(10);
		methods.setSetterMethodOther(15);
		performTest(methods, "testMethods.xml");
	}

	@Test
	public void testNamedExportable() throws PersistenceException
	{
		ExportableWithMethods methods = new ExportableWithMethods();
		methods.setField(5);
		methods.setMethod(10);
		methods.setSetterMethodOther(15);
		Persister persister = new Persister();
		persister.registerNamedExportable(ExportableWithMethods.class, "exportableWithMethods");
		performTest(methods, persister, "testNamedExportable.xml");
	}

	@Test
	public void testExportableObject() throws PersistenceException
	{
		ExportableWithObject object = new ExportableWithObject();
		ExportableWithAttribute attribute = new ExportableWithAttribute();
		attribute.setAttribute(3465);
		object.setExportableObject(attribute);
		performTest(object, "testExportableObject.xml");
	}

	@Test
	public void testNull() throws PersistenceException
	{
		ExportableWithNull nul = new ExportableWithNull();
		nul.setString(null);
		performTest(nul, "testNull.xml");
	}

	@Test
	public void testNamedPersistant() throws PersistenceException
	{
		ExportableWithNamedPersistant named = new ExportableWithNamedPersistant();
		named.setField(3254235);
		performTest(named, "testNamedPersistant.xml");
	}

	@Test
	public void testNamedMethodPersistant() throws PersistenceException
	{
		ExportableWithNamedMethodPersistant named = new ExportableWithNamedMethodPersistant();
		named.setField(3254235);
		performTest(named, "testNamedMethodPersistant.xml");
	}

	@Test(expected = PersistenceException.class)
	public void testNonExportable() throws PersistenceException
	{
		performTest(new ArrayList<Integer>(), "testNonExportable.xml");
	}

	@Test
	public void testInterfaceArray() throws PersistenceException
	{
		ExportableWithInterfaceArray array = new ExportableWithInterfaceArray();
		performTest(array, "testInterfaceArray.xml");
	}

	@Test
	public void testAdapterAndCollection() throws PersistenceException
	{
		ExportableWithAdapterAndCollection adapter = new ExportableWithAdapterAndCollection();
		List<ExportableWithAdapterAndCollection.Adaptable> adaptables =
				new ArrayList<ExportableWithAdapterAndCollection.Adaptable>();
		ExportableWithAdapterAndCollection.Adaptable adaptable1 = new ExportableWithAdapterAndCollection.Adaptable();
		adaptable1.setValue("adaptable value 1");
		ExportableWithAdapterAndCollection.Adaptable adaptable2 = new ExportableWithAdapterAndCollection.Adaptable();
		adaptable2.setValue("adaptable value 2");
		adaptables.add(adaptable1);
		adaptables.add(adaptable2);
		adapter.setAdaptables(adaptables);
		performTest(adapter, "testAdapterAndCollection.xml");
	}

	@Test
	public void testAdapterAndArray() throws PersistenceException
	{
		ExportableWithAdapterAndArray adapter = new ExportableWithAdapterAndArray();
		ExportableWithAdapterAndArray.Adaptable[] adaptables = new ExportableWithAdapterAndArray.Adaptable[2];
		adaptables[0] = new ExportableWithAdapterAndArray.Adaptable();
		adaptables[0].setValue("adaptable value 1");
		adaptables[1] = new ExportableWithAdapterAndArray.Adaptable();
		adaptables[1].setValue("adaptable value 2");
		adapter.setAdaptables(adaptables);
		performTest(adapter, "testAdapterAndArray.xml");
	}

	@Test
	public void testNamedNonExportableWithAdapter() throws PersistenceException
	{
		IPersistentAdapter<Date> adapter = new IPersistentAdapter<Date>()
		{
			@Override
			public void toXML(Date object, Element element, URI context)
			{
			}

			@Override
			public Date fromXML(Element element, URI context)
			{
				return new Date(123456);
			}
		};

		Persister p = new Persister();
		p.registerAdapter(Date.class, adapter);
		p.registerNamedExportable(Date.class, "myDate");

		Object loaded =
				p.load(WWXML.openDocument(this.getClass().getResourceAsStream("testNamedNonExportableWithAdapter.xml"))
						.getDocumentElement(), null);

		assertNotNull(loaded);
		assertEquals(new Date(123456), loaded);
	}

	protected void performTest(Object o, String expectedResourceName) throws PersistenceException
	{
		performTest(o, new Persister(), expectedResourceName);
	}

	protected void performTest(Object saved, Persister persister, String expectedResourceName)
			throws PersistenceException
	{
		persister.registerClassLoader(getClass().getClassLoader());

		try
		{
			DocumentBuilder documentBuilder = WWXML.createDocumentBuilder(false);
			Document document = documentBuilder.newDocument();
			Element element = document.createElement("root");
			document.appendChild(element);
			persister.save(saved, element, null);

			//XMLUtil.saveDocumentToFormattedFile(document, expectedResourceName);

			Element child = XmlUtil.getFirstChildElement(element);
			Object loaded = persister.load(child, null);
			Assert.assertEquals(saved, loaded);

			XMLUnit.setIgnoreWhitespace(true);
			XMLUnit.setIgnoreAttributeOrder(true);
			Diff diff =
					new Diff(XmlUtil.openDocument(this.getClass().getResourceAsStream(expectedResourceName)), document);
			DetailedDiff dd = new DetailedDiff(diff);

			StringBuffer msg = new StringBuffer();
			for (Object o : dd.getAllDifferences())
			{
				msg.append(o);
			}
			assertTrue(msg.toString(), dd.similar());
		}
		catch (PersistenceException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	protected static long copyLarge(InputStream input, OutputStream output) throws IOException
	{
		byte[] buffer = new byte[8192];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer)))
		{
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
