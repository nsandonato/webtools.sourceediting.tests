/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsp.core.tests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.StructuredModelManager;

/**
 * This class tests basic creation of IModelManager plugin and the
 * ModelManger.
 * 
 * Appropriate for BVT.
 */
public class TestModelManager extends TestCase {

	/**
	 * Constructor for TestModelManager.
	 * 
	 * @param name
	 */
	public TestModelManager(String name) {
		super(name);
	}

	public void testModelManager() throws IOException {
		IStructuredModel model = null;
		IModelManager modelManager = StructuredModelManager.getModelManager();
		assertTrue("modelManager must not be null", modelManager != null);

		try {
			model = modelManager.getModelForEdit("test.xml", new NullInputStream(), null);
			assertTrue("basic XML empty model could not be created", model != null);
		} finally {
			if (model != null) {
				model.releaseFromEdit();
			}
		}

	}

	public void testNullArgument() throws UnsupportedEncodingException, IOException {
		IStructuredModel model = null;
		Exception e = null;
		IModelManager modelManager = StructuredModelManager.getModelManager();
		try {
			model = modelManager.getModelForEdit((String) null, null, null);
		} catch (IllegalArgumentException exception) {
			e = exception;
			assertTrue("illegal argument failed to throw IllegalArgumentException", e instanceof IllegalArgumentException);
		} finally {
			if (model != null) {
				model.releaseFromEdit();
			}
		}
	}
}