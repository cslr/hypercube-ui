package biz.novelinsight.hypercube;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Link;

public class HypercubeAboutDialog extends Dialog {

	protected Object result;
	protected Shell shlAboutNovelInsight;
	
	static final protected String hypercubeVersion = "v0.80";

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public HypercubeAboutDialog(Shell parent, int style) {
		super(parent, style);
		setText("About Novel Insight Hypercube VST..");	
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		
		shlAboutNovelInsight.open();
		shlAboutNovelInsight.layout();
		Display display = getParent().getDisplay();
		
		Image icon = new Image(display, "cube-icon-small.jpg");
		if(shlAboutNovelInsight != null)
			shlAboutNovelInsight.setImage(icon);
		
		while (!shlAboutNovelInsight.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAboutNovelInsight = new Shell(getParent(), SWT.SHELL_TRIM);
		shlAboutNovelInsight.setSize(450, 300);
		shlAboutNovelInsight.setText("About Novel Insight Hypercube VST..");
		shlAboutNovelInsight.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label lblNewLabel = new Label(shlAboutNovelInsight, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setText("Hypercube VST " + this.hypercubeVersion + " is developed\n by Novel Insight / Tomas Ukkonen (© Copyright 2020).");
		
		Label lblTheUserMust = new Label(shlAboutNovelInsight, SWT.NONE);
		lblTheUserMust.setAlignment(SWT.CENTER);
		lblTheUserMust.setText("Internal use is free but the user must buy a license to distribute audio/music in which this software has been used.");
		
		Label lblHttpswwwnovelinsightbiz = new Label(shlAboutNovelInsight, SWT.NONE);
		lblHttpswwwnovelinsightbiz.setAlignment(SWT.CENTER);
		lblHttpswwwnovelinsightbiz.setText("https://www.novelinsight.biz/");
		
		Label lblBusinessnovelinsightbiz = new Label(shlAboutNovelInsight, SWT.NONE);
		lblBusinessnovelinsightbiz.setAlignment(SWT.CENTER);
		lblBusinessnovelinsightbiz.setText("business@novelinsight.biz");
		
		Button btnOk = new Button(shlAboutNovelInsight, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				shlAboutNovelInsight.close();
			}
		});
		btnOk.setText("OK");

	}

}
