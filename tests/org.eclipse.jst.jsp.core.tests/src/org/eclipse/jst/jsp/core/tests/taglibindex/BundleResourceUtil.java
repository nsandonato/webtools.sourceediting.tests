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
package org.eclipse.jst.jsp.core.tests.taglibindex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.jsp.core.tests.JSPCoreTestsPlugin;

public class BundleResourceUtil {

	public static void _copyBundleEntriesIntoWorkspace(final String rootEntry, final String fullTargetPath) throws CoreException {
		Enumeration entries = JSPCoreTestsPlugin.getDefault().getBundle().getEntryPaths(rootEntry);
		while (entries != null && entries.hasMoreElements()) {
			String entryPath = entries.nextElement().toString();
			String targetPath = new Path(fullTargetPath + "/" + entryPath.substring(rootEntry.length())).toString();
			if (entryPath.endsWith("/")) {
				IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(targetPath));
				if (!folder.exists()) {
					folder.create(true, true, new NullProgressMonitor());
				}
				_copyBundleEntriesIntoWorkspace(entryPath, targetPath);
			}
			else {
				_copyBundleEntryIntoWorkspace(entryPath, targetPath);
			}
			// System.out.println(entryPath + " -> " + targetPath);
		}
	}

	public static IFile _copyBundleEntryIntoWorkspace(String entryname, String fullPath) throws CoreException {
		IFile file = null;
		URL entry = JSPCoreTestsPlugin.getDefault().getBundle().getEntry(entryname);
		if (entry != null) {
			try {
				byte[] b = new byte[2048];
				InputStream input = entry.openStream();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				int i = -1;
				while ((i = input.read(b)) > -1) {
					output.write(b, 0, i);
				}
				file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fullPath));
				if (file != null) {
					if (!file.exists()) {
						file.create(new ByteArrayInputStream(output.toByteArray()), true, new NullProgressMonitor());
					}
					else {
						file.setContents(new ByteArrayInputStream(output.toByteArray()), true, false, new NullProgressMonitor());
					}
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void copyBundleEntriesIntoWorkspace(final String rootEntry, final String fullTargetPath) throws CoreException {
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				_copyBundleEntriesIntoWorkspace(rootEntry, fullTargetPath);
				ResourcesPlugin.getWorkspace().checkpoint(ResourcesPlugin.getWorkspace().isAutoBuilding());
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, new NullProgressMonitor());
	}

	public static IFile copyBundleEntryIntoWorkspace(final String entryname, final String fullPath) throws CoreException {
		final IFile file[] = new IFile[1];
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				file[0] = _copyBundleEntryIntoWorkspace(entryname, fullPath);
				ResourcesPlugin.getWorkspace().checkpoint(ResourcesPlugin.getWorkspace().isAutoBuilding());
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, new NullProgressMonitor());
		return file[0];
	}

	public static IProject createSimpleProject(String name, IPath location, String[] natureIds) {
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(name);
		if (location != null) {
			description.setLocation(location);
		}
		if (natureIds != null) {
			description.setNatureIds(natureIds);
		}
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		try {
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		return project;
	}
}