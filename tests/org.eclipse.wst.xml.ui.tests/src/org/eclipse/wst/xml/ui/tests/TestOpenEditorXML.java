/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.ui.tests;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

/**
 * Test misc editor functions with an open xml editor.
 */
public class TestOpenEditorXML extends TestCase {
	private final String PROJECT_NAME = "TestOpenEditorXML";
	private final String FILE_NAME = "testStructuredTextEditorXML.xml";

	private static IEditorPart fEditor;
	private static IFile fFile;
	private static boolean fIsSetup = false;

	public TestOpenEditorXML() {
		super("TestStructredTextEditorXML");
	}

	protected void setUp() throws Exception {
		// only create project and file once
		if (!fIsSetup) {
			// create project
			createProject(PROJECT_NAME);
			fFile = getOrCreateFile(PROJECT_NAME + "/" + FILE_NAME);
			fIsSetup = true;
		}

		// editor is opened each time
		if (fIsSetup && fEditor == null) {
			IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			fEditor = IDE.openEditor(page, fFile, true, true);
			if (!((fEditor instanceof XMLMultiPageEditorPart) || (fEditor instanceof StructuredTextEditor)))
				assertTrue("Unable to open structured text editor", false);
		}
	}

	protected void tearDown() throws Exception {
		// editor is closed each time
		if (fEditor != null) {
			IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			page.closeEditor(fEditor, false);
			assertTrue("Unable to close editor", true);
			fEditor = null;
		}
	}

	private void createProject(String projName) {
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projName);

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
		try {
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private IFile getOrCreateFile(String filePath) {
		IFile blankFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
		if (blankFile != null && !blankFile.exists()) {
			try {
				blankFile.create(new ByteArrayInputStream(new byte[0]), true, new NullProgressMonitor());
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return blankFile;
	}

	/**
	 * Test setting text in document of open editor.
	 */
	public void testSetDocument() {
		IDocument document = (IDocument) fEditor.getAdapter(IDocument.class);
		try {
			document.set("<hello></hello>");
		}
		catch (Exception e) {
			assertTrue("Unable to set text in editor: " + e, false);
		}
	}
}