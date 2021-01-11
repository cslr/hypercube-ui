package biz.novelinsight.hypercube;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Link;

public class HypercubeAboutDialog extends Dialog {

	protected Object result;
	protected Shell shlAboutNovelInsight;
	
	protected HypercubeUIModel model;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public HypercubeAboutDialog(Shell parent, HypercubeUIModel model, int style) {
		super(parent, style);
		this.model = model;
		
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
		shlAboutNovelInsight.setSize(467, 414);
		shlAboutNovelInsight.setText("About Novel Insight Hypercube VST..");
		shlAboutNovelInsight.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label lblNewLabel = new Label(shlAboutNovelInsight, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setText(model.getAppName() + " " + model.getAppVersion() + " is developed\n by Tomas Ukkonen (© Copyright 2020).");
		
		Label lblTheUserMust = new Label(shlAboutNovelInsight, SWT.NONE);
		lblTheUserMust.setAlignment(SWT.CENTER);
		lblTheUserMust.setText("You can use this beta software freely, but please help me to get a job as Scientist or Data Scientist again. Mental health sector says I'm (a bit) crazy but it's their medicines that have made me crazy. Wake me back to alive / Resurrect me if I'm dead!");
		
		Label lblHttpswwwnovelinsightbiz = new Label(shlAboutNovelInsight, SWT.NONE);
		lblHttpswwwnovelinsightbiz.setAlignment(SWT.CENTER);
		lblHttpswwwnovelinsightbiz.setText("https://www.novelinsight.fi/");
		
		Label lblBusinessnovelinsightbiz = new Label(shlAboutNovelInsight, SWT.NONE);
		lblBusinessnovelinsightbiz.setAlignment(SWT.CENTER);
		lblBusinessnovelinsightbiz.setText("tomas.ukkonen@iki.fi");
		
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
