package biz.novelinsight.hypercube;

import java.io.File;
import java.io.IOException;


// stores state of the Hypercube UI data model
public class HypercubeUIModel {
	
	// or directory if all files in directory should be processed
	private File vstFile; 
	
	// do we use computational expensive methods (Variational Autoencoder)
	private boolean useVAE;
	
	// model complexity is value between ]1,10.0]
	private float modelComplexity;
	
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
	
	public float getModelComplexity(){
		return this.modelComplexity;
	}
	
	public boolean setModelComplexity(float c) {
		if(c < 1.0f) return false;
		if(c > 10.0f) return false;
		
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
