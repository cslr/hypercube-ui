package biz.novelinsight.hypercube;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.StyledText;

import java.io.File;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class HypercubeWindow {

	protected Shell shlHypercubeVst;
	protected Display display;
	
	private MenuItem mntmVaeModeslow;
	private MenuItem mntmSkipAlreadyExisting;
	private Spinner complexitySpinner;
	private Text filenameText;
	private StyledText messagesText;
	
	private Button scanButton;
	private Button calculateButton;
	private Button stopComputationButton;
	
	private Thread uiUpdateThread; // thread to keep UI updated from reducer computations
	private boolean uiThreadRunning;
	
	protected HypercubeUIModel model;
	protected VstDimReducer reducer;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HypercubeWindow window = new HypercubeWindow();
			
			Display.setAppName(window.model.getAppName());
			Display.setAppVersion(window.model.getAppVersion());
			
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HypercubeWindow()
	{
		model = new HypercubeUIModel();
		// reducer = new DimReducerStub(); // currently just uses stub
		reducer = new HypercubeDimReducer(); // C++ implementation
		uiThreadRunning = false;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		
		Image icon = new Image(display, "cube-icon-small.jpg");
		shlHypercubeVst.setImage(icon);
		
		shlHypercubeVst.open();
		shlHypercubeVst.layout();
		while (!shlHypercubeVst.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlHypercubeVst = new Shell();
		shlHypercubeVst.setSize(689, 674);
		shlHypercubeVst.setText("Hypercube VST");
		shlHypercubeVst.setLayout(new GridLayout(1, false));
		
		Menu menu = new Menu(shlHypercubeVst, SWT.BAR);
		shlHypercubeVst.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("SELECT VST DIRECTORY.");
				
			    DirectoryDialog dialog = new DirectoryDialog(shlHypercubeVst);
			    dialog.setText("Open VST folder");
			    dialog.setFilterPath("c:\\"); // Windows specific

			    String filename = dialog.open();
			    boolean filenameUpdated = model.setVSTFile(filename);
			    
			    System.out.println("MODEL FILE " + filename + " SET: " + filenameUpdated);
			    
			    if(reducer.hasWriteAccess(filename) == false) {			    	
			    	MessageBox dialog2 = new MessageBox(shlHypercubeVst, SWT.ICON_WARNING | SWT.OK);
			    	dialog2.setText("No Directory Write Access");
			    	dialog2.setMessage("There is no directory write access which is required to generate files.\nPlease enable write access for VST directory.");
			    	dialog2.open();
			    }
			    
			    if(filenameUpdated)
			    	filenameText.setText(model.getFullFilename());
			}
		});
		mntmNewItem.setText("Select VST folder..");
		mntmNewItem.setToolTipText("Selects a VST folder. Calculates VST parameter reduction for VST files in the selected folder.");
		
		MenuItem mntmSelectOneVst = new MenuItem(menu_1, SWT.NONE);
		mntmSelectOneVst.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("SELECT VST INSTRU.");
				
				FileDialog fd = new FileDialog(shlHypercubeVst, SWT.OPEN);
		        fd.setText("Open VST instrument/effect");
		        fd.setFilterPath("C:\\");
		        String[] filterExt = { "*.dll", "*.*" };
		        fd.setFilterExtensions(filterExt);
		        
			    String filename = fd.open();
			    boolean filenameUpdated = model.setVSTFile(filename);

			    System.out.println("MODEL FILE " + filename + " SET: " + filenameUpdated);

			    if(reducer.hasWriteAccess(filename) == false) {			    	
			    	MessageBox dialog2 = new MessageBox(shlHypercubeVst, SWT.ICON_WARNING | SWT.OK);
			    	dialog2.setText("No Directory Write Access");
			    	dialog2.setMessage("There is no directory write access which is required to generate files.\nPlease enable write access for VST directory.");
			    	dialog2.open();
			    }
			    
			    if(filenameUpdated)
			    	filenameText.setText(model.getFullFilename());
			}
		});
		mntmSelectOneVst.setText("Select one VST effect..");
		mntmSelectOneVst.setToolTipText("Selects a single VST file for which to compute parameter reduction.");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		mntmSkipAlreadyExisting = new MenuItem(menu_1, SWT.CHECK);
		mntmSkipAlreadyExisting.setSelection(model.getSkipExistingModels());
		mntmSkipAlreadyExisting.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem item = mntmSkipAlreadyExisting;
				boolean skip = item.getSelection();
				
				model.setSkipExistingModels(skip);
				
				System.out.println("SKIP EXISTING MODEL: " + model.getSkipExistingModels());
			}
		});
		mntmSkipAlreadyExisting.setText("Skip already existing VSTs");
		mntmSkipAlreadyExisting.setToolTipText("Set skip already computed Hypercube VST files.");
		
		mntmVaeModeslow = new MenuItem(menu_1, SWT.CHECK);
		mntmVaeModeslow.setSelection(model.getUseVAE());
		mntmVaeModeslow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem item = mntmVaeModeslow;
				
				model.setUseVAE(item.getSelection());
				
				System.out.println("VAE SELECTED: " + model.getUseVAE());
			}
		});
		mntmVaeModeslow.setText("Deep mode (beta)");
		mntmVaeModeslow.setToolTipText("Enables use of deep learning  (beta/doesn't always give good results).");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmQuit = new MenuItem(menu_1, SWT.NONE);
		mntmQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("QUIT COMMAND");
				
				uiThreadRunning = false;
				
				System.exit(0);
			}
		});
		mntmQuit.setText("Quit");
		mntmQuit.setToolTipText("Shutdowns the program.");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);
		
		MenuItem mntmHelpContents = new MenuItem(menu_2, SWT.NONE);
		mntmHelpContents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("SHOW HTML HELP DOCUMENT");
				String path = System.getProperty("user.dir");
				org.eclipse.swt.program.Program.launch(path + "/help/index.html");
			}
		});
		mntmHelpContents.setText("Help contents");
		mntmHelpContents.setToolTipText("How to use Hypercube VST software.");
		
		new MenuItem(menu_2, SWT.SEPARATOR);
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_2, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("ABOUT DIALOG SELECTED");
				
				HypercubeAboutDialog dialog = new HypercubeAboutDialog(shlHypercubeVst, model, SWT.NONE);
				dialog.open();
			}
		});
		mntmNewItem_1.setText("About Hypercube VST");
		mntmNewItem_1.setToolTipText("Hypercube VST software information.");
		
		Composite file_composite = new Composite(shlHypercubeVst, SWT.NONE);
		file_composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		file_composite.setLayout(new GridLayout(2, false));
		
		Composite composite_3 = new Composite(file_composite, SWT.NONE);
		composite_3.setLayout(new GridLayout(1, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		filenameText = new Text(composite_3, SWT.BORDER);
		filenameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		filenameText.setEnabled(false);
		
		{
			File f = (File)model.getVSTFile();
			if(f == null)
				filenameText.setText("<select vst file or folder>");
			else if(f.exists() == false) {
				filenameText.setText("<select vst file or folder>");
			}
			else {
				filenameText.setText(model.getFullFilename());
			}
		}	
		
		filenameText.setEditable(false);
		filenameText.setToolTipText("A VST file or directory to process.");
		
		Composite composite_2 = new Composite(file_composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		scanButton = new Button(composite_2, SWT.NONE);
		scanButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("SCAN VST FILES FOR COMPATIBILITY");
				
				String filename = model.getFullFilename();
					
				if(filename.compareTo("") == 0) {
					System.out.println("Starting scan failed. NO FILENAME.");
					return;
				}
				
				System.out.println("Scan returned: " + reducer.scanVSTFile(filename) );			
			}
		});
		scanButton.setText("Scan VST");
		scanButton.setToolTipText("Checks if parameter reduction can be computed for a VST file(s).");
		
		calculateButton = new Button(composite_2, SWT.NONE);
		calculateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("CALCULATE HYPERCUBE PARAMETER REDUCTION FOR VST FILES");
				
				String filename = model.getFullFilename();
					
				if(filename == null) {
					System.out.println("Starting computation failed. NO FILENAME.");
					return;
				}
				else if(filename.compareTo("") == 0) {
					System.out.println("Starting computation failed. NO FILENAME.");
					return;						
				}
				
				if(reducer.isParameterReductionComputing()) {
					System.out.println("Starting computation failed. Already computing.");
					return;
				}
					
				System.out.println("Start computation returned: " + 
					reducer.startCalculateVSTParameterReduction
					(filename, model.getModelComplexity(), model.getUseVAE(), model.getSkipExistingModels()));

				
			}
		});
		calculateButton.setText("Calculate VST");
		calculateButton.setToolTipText("Computes parameter reduction.");
		
		stopComputationButton = new Button(composite_2, SWT.NONE);
		stopComputationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(reducer.isScanningComputing()) {
					System.out.println("Stoping scanning: " + reducer.stopScanning());
				}
				
				if(reducer.isParameterReductionComputing()) {
					System.out.println("Stoping parameter reduction computation: " + reducer.stopCalculateVSTParameterReduction());
				}	
			}
		});
		stopComputationButton.setText("Stop");
		stopComputationButton.setToolTipText("Stops computation.");
		
		Composite model_composite = new Composite(shlHypercubeVst, SWT.NONE);
		model_composite.setLayout(new GridLayout(2, false));
		model_composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(model_composite, SWT.NONE);
		lblNewLabel.setText("Model complexity: ");
		
		complexitySpinner = new Spinner(model_composite, SWT.BORDER);
		complexitySpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String valueStr = complexitySpinner.getText();
				
				try {
					int value = Integer.parseInt(valueStr);
					
					model.setModelComplexity((float)value);
					
					System.out.println("NEW MODEL COMPLEXITY: " + model.getModelComplexity());					
				}
				catch(NumberFormatException nfe) { /* do nothing */ }
				
				
			}
		});
		complexitySpinner.setPageIncrement(1);
		complexitySpinner.setMaximum((int)model.getModelComplexityMaximum());
		complexitySpinner.setMinimum((int)model.getModelComplexityMinimum());
		complexitySpinner.setToolTipText("Sets model size.");
		complexitySpinner.setSelection((int)model.getModelComplexity());
		
		
		Composite text_composite = new Composite(shlHypercubeVst, SWT.NONE);
		text_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text_composite.setLayout(new GridLayout(1, false));
		
		TextViewer textViewer = new TextViewer(text_composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		messagesText = textViewer.getTextWidget();
		messagesText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				messagesText.setTopIndex(messagesText.getLineCount() - 1);
			}
		});
		messagesText.setTouchEnabled(true);
		messagesText.setAlignment(SWT.CENTER);
		messagesText.setEditable(false);
		messagesText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		
		// UI internal thread:
		// 1. update messagesText from computational messages
		// 2. update start computation button to change to disable computation button during computation
		
		uiUpdateThread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	while (uiThreadRunning) {
		    		display.asyncExec(new Runnable() {
		    			public void run() {
		    				/**
		    				 * this is in case that the thread starts before the
		    				 * UI is created
		    				 */
		    				if (messagesText == null || messagesText.isDisposed())
		    					return;
		    				
		    				// updates buttons 
		    				boolean computing = reducer.isParameterReductionComputing() == true || 
		    						reducer.isScanningComputing() == true;
		    				
		    				scanButton.setEnabled(computing == false);
		    				calculateButton.setEnabled(computing == false);
		    				stopComputationButton.setEnabled(computing == true);
		    				complexitySpinner.setEnabled(computing == false);
		    				
		    				mntmNewItem.setEnabled(computing == false);
		    				mntmSelectOneVst.setEnabled(computing == false);
		    				mntmSkipAlreadyExisting.setEnabled(computing == false);
		    				mntmVaeModeslow.setEnabled(computing == false);
		    				

		    				// updates message area
		    				String txt  = messagesText.getText();
		    				String msgs = reducer.getUnreadMessages();
				    
		    				if(msgs.length() <= 0) return; // nothing to do
				    
		    				{
		    					String newText = txt + msgs;
		    					
		    					String[] lines = newText.split("\n");
		    					
		    					if(lines.length < 5000) {
		    						messagesText.setText(newText);
		    					}
		    					else {
		    						// drops more than 5000 lines
		    						newText = "";
		    						
		    						for(int i=lines.length-5000;i<lines.length;i++)
		    							newText = newText + lines[i];
		    						
		    						messagesText.setText(newText);
		    					}
		    				}		    				
		    			}
		    		});
			    
		    		try {
		    			Thread.sleep(250);
		    		} catch (InterruptedException e) {
		    			e.printStackTrace();
		    		}
		    	}
		    }
		});
		
		uiUpdateThread.setDaemon(true);
		uiThreadRunning = true;
		uiUpdateThread.start();
		
	}
}
