package org.eclipse.wst.html.ui.tests.viewer;

import junit.framework.TestCase;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.wst.html.core.internal.provisional.text.IHTMLPartitionTypes;
import org.eclipse.wst.html.ui.internal.HTMLUIPlugin;
import org.eclipse.wst.html.ui.internal.provisional.StructuredTextViewerConfigurationHTML;
import org.eclipse.wst.html.ui.tests.Logger;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.provisional.style.IHighlighter;

/**
 * @author pavery
 */
public class TestViewerConfigurationHTML extends TestCase {
    
	private StructuredTextViewer fViewer = null;
	private StructuredTextViewerConfigurationHTML fConfig = null;
	private IPreferenceStore fPreferenceStore = null;
	private boolean fDisplayExists = true;
	private boolean isSetup = false;
	
    public TestViewerConfigurationHTML() {
        super("TestViewerConfigurationHTML");
    }
    protected void setUp() throws Exception {
		
    	super.setUp();
		if(!this.isSetup){
			setUpPreferences();
			setUpViewerConfiguration();
			this.isSetup = true;
		}
    }
	
    private void setUpPreferences() {
		fPreferenceStore = HTMLUIPlugin.getDefault().getPreferenceStore();
		fPreferenceStore.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_HYPERLINKS_ENABLED, true);
	}
	
	private void setUpViewerConfiguration() {

		if(Display.getCurrent() != null) {
			
			Shell shell = null;
			Composite parent = null;
			
			if(PlatformUI.isWorkbenchRunning()) {
				shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			}
			else {	
				shell = new Shell(Display.getCurrent());
			}
			parent = new Composite(shell, SWT.NONE);
			
			// dummy viewer
			fViewer = new StructuredTextViewer(parent, null, null, false, SWT.NONE);
			fConfig = new StructuredTextViewerConfigurationHTML(fPreferenceStore);
		}
		else {
			fDisplayExists = false;
			Logger.log(Logger.INFO, "TestViewerConfigurationHTML tests cannot run because there is no DISPLAY available");
		}
	}
    
	public void testGetAutoEditStrategies() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IAutoEditStrategy[] strategies = fConfig.getAutoEditStrategies(fViewer, IHTMLPartitionTypes.HTML_DEFAULT);
		assertNotNull(strategies);
		assertTrue("there are no auto edit strategies", strategies.length>0);
	}
	
	public void testGetConfiguredContentTypes() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		String[] configuredContentTypes = fConfig.getConfiguredContentTypes(fViewer);
		assertNotNull(configuredContentTypes);
		assertTrue("there are no configured content types", configuredContentTypes.length > 1);
	}
	
	public void testGetContentAssistant() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IContentAssistant ca = fConfig.getContentAssistant(fViewer);
		assertNotNull("there is no content assistant", ca);
	}
	
	public void testGetCorrectionAssistant() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IContentAssistant ca = fConfig.getCorrectionAssistant(fViewer);
		assertNotNull("there is no correction assistant", ca);
	}
	
	public void testGetContentFormatter() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IContentFormatter cf = fConfig.getContentFormatter(fViewer);
		assertNotNull("there is no content formatter", cf);
	}
	
	public void testGetDoubleClickStrategy() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		String[] contentTypes = fConfig.getConfiguredContentTypes(fViewer);
		for (int i = 0; i < contentTypes.length; i++) {
			ITextDoubleClickStrategy strategy = fConfig.getDoubleClickStrategy(fViewer, contentTypes[i]);
			if(strategy != null) {
				return;
			}
		}
		assertTrue("there are no configured double click strategies", false);
	}
	
	public void testGetHighlighter() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IHighlighter highlighter = fConfig.getHighlighter(fViewer);
		assertNotNull("Highlighter is null", highlighter);
	}
	
	public void testGetInformationPresenter() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IInformationPresenter presenter = fConfig.getInformationPresenter(fViewer);
		assertNotNull("InformationPresenter is null", presenter);
	}
	
    public void testGetAnnotationHover() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IAnnotationHover hover = fConfig.getAnnotationHover(fViewer);
		assertNotNull("AnnotationHover is null", hover);
    }
	
	public void testUnconfigure() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		fConfig.unConfigure(fViewer);
		// need a good test here to make sure thing are really unconfigured
		
		// need to re-set up since it's likely
		// more tests are called after this one
		setUpViewerConfiguration();
	}
	
	public void testGetReconciler() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IReconciler r = fConfig.getReconciler(fViewer);
		assertNotNull("Reconciler is null", r);
	}
	
	public void testGetHyperlinkDetectors() {
		
		// probably no display
		if(!fDisplayExists)
			return;
		
		IHyperlinkDetector[] detectors = fConfig.getHyperlinkDetectors(fViewer);
		assertNotNull(detectors);
		assertTrue("there are no hyperlink detectors", detectors.length > 1);
	}
	
	public void testGetTextHover() {
		
		// probably no display
		if(!fDisplayExists)
			return;

		String[] hoverPartitions = new String[]{IHTMLPartitionTypes.HTML_DEFAULT, IHTMLPartitionTypes.SCRIPT};
		for (int i = 0; i < hoverPartitions.length; i++) {
			ITextHover hover = fConfig.getTextHover(fViewer, hoverPartitions[i], SWT.NONE);
			assertNotNull("hover was null for partition: " + hoverPartitions[i], hover);
		}
	}
	
	public void testGetIndentPrefixes() {
		// probably no display
		if(!fDisplayExists)
			return;
		
		String[] contentTypes = fConfig.getConfiguredContentTypes(fViewer);
		for (int i = 0; i < contentTypes.length; i++) {
			String prefixes[] = fConfig.getIndentPrefixes(fViewer, contentTypes[i]);
			if(prefixes != null) {
				return;
			}
		}
		assertTrue("there are no configured indent prefixes", false);
	}
}