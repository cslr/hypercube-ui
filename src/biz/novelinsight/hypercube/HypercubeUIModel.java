package biz.novelinsight.hypercube;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;


// stores state of the Hypercube UI data model
public class HypercubeUIModel {
	
	// software information
	
	final protected String appName = "Hypercube VST";
	final protected String appVersion = "v0.991 BETA";
	
	// or directory if all files in directory should be processed
	private File vstFile; 
	
	// dimension reduction method (PCA, t-SNE, Variational Autoencoder)
	private int method;
	
	// model complexity is value between ]1,10.0]
	private float modelComplexity = 1.0f;
	
	final protected float modelComplexityMinimum = 1.0f;
	final protected float modelComplexityMaximum = 10.0f;
	
	// true if we skip computing already existing models
	private boolean skipExistingModels;
	
	
	public HypercubeUIModel(){
		vstFile = null;
		method = VstDimReducer.USE_PCA;
		modelComplexity = 1.0f;
		skipExistingModels = true;
	}
	
	public HypercubeUIModel(HypercubeUIModel model) {
		this.vstFile = model.vstFile;
		this.method  = model.method;
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
	
	public int getMethod() {
		return method;
	}
	
	public boolean setMethod(int method) {
		if(method == VstDimReducer.USE_PCA || 
			method == VstDimReducer.USE_TSNE || 
			method == VstDimReducer.USE_VAE) 
		{
			this.method = method;
			return true;
		}
		else return false;
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
