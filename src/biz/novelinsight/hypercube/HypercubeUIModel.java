package biz.novelinsight.hypercube;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;


// stores state of the Hypercube UI data model
public class HypercubeUIModel {
	
	// software information
	
	final protected String appName = "Hypercube VST";
	final protected String appVersion = "v0.93b";
	
	// or directory if all files in directory should be processed
	private File vstFile; 
	
	// do we use computational expensive methods (Variational Autoencoder)
	private boolean useVAE;
	
	// model complexity is value between ]1,100.0]
	private float modelComplexity;
	
	final protected float modelComplexityMinimum = 1.0f;
	final protected float modelComplexityMaximum = 40.0f;
	
	// true if we skip computing already existing models
	private boolean skipExistingModels;
	
	
	public HypercubeUIModel(){
		vstFile = null;
		useVAE  = false;
		modelComplexity = 1.0f;
		skipExistingModels = true;
	}
	
	public HypercubeUIModel(HypercubeUIModel model) {
		this.vstFile = model.vstFile;
		this.useVAE  = model.useVAE;
		this.modelComplexity = model.modelComplexity;
		this.skipExistingModels = model.skipExistingModels;
	}
	
	public String getAppName() {
		return this.appName;
	}
	
	
	public String getAppVersion() {
		return this.appVersion;
	}

	
	public File getVSTFile() {
		return vstFile; // can be NULL
	}
	
	public boolean setVSTFile(String filename) {
		if(filename == null) return false;
		File f = new File(filename);
		if(f.exists() == false) return false;
		
		vstFile = f;
		return true;
	}
	
	public String getFullFilename() 
	{
		try {
			if(vstFile == null) {
				return "";
			}
			
			if(vstFile.isDirectory() == false) {
				String filename = vstFile.getCanonicalFile().toString();				
				return filename;
			}
			else {
				String directory = vstFile.getCanonicalPath();
				return directory;
			}
		}
		catch(IOException ioe) { return ""; }

	}
	
	public boolean getUseVAE() {
		return useVAE;
	}
	
	public void setUseVAE(boolean useVAE) {
		this.useVAE = useVAE;
	}
	
	public float getModelComplexityMaximum() {
		return this.modelComplexityMaximum;
	}
	
	public float getModelComplexityMinimum() {
		return this.modelComplexityMinimum;
	}
	
	public float getModelComplexity(){
		return this.modelComplexity;
	}
	
	public boolean setModelComplexity(float c) {
		if(c < this.modelComplexityMinimum)
			c = this.modelComplexityMinimum;
		
		if(c > this.modelComplexityMaximum)
			c = this.modelComplexityMaximum;
		
		modelComplexity = c;
		
		return true;
	}
	
	public boolean getSkipExistingModels() {
		return this.skipExistingModels;
	}
	
	public void setSkipExistingModels(boolean skip) {
		this.skipExistingModels = skip;
	}


}
