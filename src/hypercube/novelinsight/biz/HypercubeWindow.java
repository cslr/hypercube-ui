package hypercube.novelinsight.biz;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
	private MenuItem mntmVaeModeslow;
	private MenuItem mntmSkipAlreadyExisting;
	private Spinner complexitySpinner;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HypercubeWindow window = new HypercubeWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
		shlHypercubeVst.setSize(571, 413);
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
			    System.out.println("RESULT=" + dialog.open());
			}
		});
		mntmNewItem.setText("Select VST folder..");
		
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
		        
		        System.out.println("RESULT=" + fd.open());
				
			}
		});
		mntmSelectOneVst.setText("Select one VST effect..");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		mntmSkipAlreadyExisting = new MenuItem(menu_1, SWT.CHECK);
		mntmSkipAlreadyExisting.setSelection(true);
		mntmSkipAlreadyExisting.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem item = mntmSkipAlreadyExisting;
				System.out.println("SKIP EXISTING: " + item.getSelection());
			}
		});
		mntmSkipAlreadyExisting.setText("Skip already existing VSTs");
		
		mntmVaeModeslow = new MenuItem(menu_1, SWT.CHECK);
		mntmVaeModeslow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem item = mntmVaeModeslow;
				System.out.println("VAE SELECTED: " + item.getSelection());
			}
		});
		mntmVaeModeslow.setText("VAE mode (slow)");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmQuit = new MenuItem(menu_1, SWT.NONE);
		mntmQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("QUIT COMMAND");
				
				System.exit(0);
			}
		});
		mntmQuit.setText("Quit");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);
		
		MenuItem mntmHelpContents = new MenuItem(menu_2, SWT.NONE);
		mntmHelpContents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("SHOW HTML HELP DOCUMENT");
			}
		});
		mntmHelpContents.setText("Help contents");
		
		new MenuItem(menu_2, SWT.SEPARATOR);
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_2, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("ABOUT DIALOG SELECTED");
				
				HypercubeAboutDialog dialog = new HypercubeAboutDialog(shlHypercubeVst, SWT.NONE);
				dialog.open();
			}
		});
		mntmNewItem_1.setText("About Hypercube VST");
		
		Composite file_composite = new Composite(shlHypercubeVst, SWT.NONE);
		file_composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		file_composite.setLayout(new GridLayout(2, false));
		
		Composite composite_3 = new Composite(file_composite, SWT.NONE);
		composite_3.setLayout(new GridLayout(1, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		text = new Text(composite_3, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setEnabled(false);
		text.setText("<select vst file or folder>");
		text.setEditable(false);
		
		Composite composite_2 = new Composite(file_composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnScan = new Button(composite_2, SWT.NONE);
		btnScan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("SCAN VST FILES FOR COMPATIBILITY");
				
			}
		});
		btnScan.setText("Scan VST");
		
		Button btnCalculate = new Button(composite_2, SWT.NONE);
		btnCalculate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("CALCULATE HYPERCUBE PARAMETER REDUCTION FOR VST FILES");
			}
		});
		btnCalculate.setText("Calculate VST");
		
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
					System.out.println("MODEL COMPLEXITY: " + value);
				}
				catch(NumberFormatException nfe) { /* do nothing */ }
				
				
			}
		});
		complexitySpinner.setPageIncrement(1);
		complexitySpinner.setMaximum(10);
		complexitySpinner.setMinimum(1);
		
		
		Composite text_composite = new Composite(shlHypercubeVst, SWT.NONE);
		text_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text_composite.setLayout(new GridLayout(1, false));
		
		TextViewer textViewer = new TextViewer(text_composite, SWT.BORDER);
		StyledText styledText = textViewer.getTextWidget();
		styledText.setTouchEnabled(true);
		styledText.setAlignment(SWT.CENTER);
		styledText.setEditable(false);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	}
}
