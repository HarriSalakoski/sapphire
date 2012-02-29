/******************************************************************************
 * Copyright (c) 2012 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shenxue Zhou - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.ui.diagram.layout.standard.internal;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.sapphire.modeling.StatusException;
import org.eclipse.sapphire.modeling.util.internal.FileUtil;
import org.eclipse.sapphire.modeling.xml.RootXmlResource;
import org.eclipse.sapphire.modeling.xml.XmlResourceStore;
import org.eclipse.sapphire.ui.diagram.editor.SapphireDiagramEditorPagePart;
import org.eclipse.sapphire.ui.diagram.layout.standard.StandardDiagramLayout;
import org.eclipse.sapphire.ui.internal.SapphireUiFrameworkPlugin;
import org.eclipse.sapphire.workspace.WorkspaceFileResourceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

/**
 * @author <a href="mailto:shenxue.zhou@oracle.com">Shenxue Zhou</a>
 */

public class ProjectDiagramLayoutPersistenceService extends LazyLoadLayoutPersistenceService 
{	
	public ProjectDiagramLayoutPersistenceService(IEditorInput editorInput, SapphireDiagramEditorPagePart diagramPart)
	{
		super(editorInput, diagramPart);
	}

	@Override
	protected StandardDiagramLayout initLayoutModel() 
	{
		StandardDiagramLayout layoutModel = null;
		try
		{
			String fileName = computeLayoutFileName(this.editorInput);
			if (fileName != null)
			{
				IFile layoutFile = getLayoutPersistenceFile(fileName);
				if (layoutFile != null)
				{
					final XmlResourceStore resourceStore = new XmlResourceStore( new WorkspaceFileResourceStore(layoutFile));
					layoutModel = StandardDiagramLayout.TYPE.instantiate(new RootXmlResource( resourceStore ));
				}
			}
		}
		catch (Exception e)
		{
			SapphireUiFrameworkPlugin.log( e );
		}
		return layoutModel;
	}
	
	protected IFile getLayoutPersistenceFile(String fileName) throws StatusException, CoreException
	{
		if (this.editorInput instanceof IFileEditorInput)
		{
			IFileEditorInput fileInput = (IFileEditorInput)this.editorInput;
			IProject proj = fileInput.getFile().getProject();
			if (proj != null)
			{
				IFolder layoutIFolder = proj.getFolder(".settings/org.eclipse.sapphire.ui.diagram/layouts");
				File layoutFolder = layoutIFolder.getLocation().toFile();
	            if (!layoutFolder.exists())
	            {
	                FileUtil.mkdirs(layoutFolder);
	                layoutIFolder.refreshLocal(IResource.DEPTH_ONE, null);
	            }
	            IFile layoutFile = layoutIFolder.getFile(fileName);
	            return layoutFile;
			}
		}
		return null;
	}

}
