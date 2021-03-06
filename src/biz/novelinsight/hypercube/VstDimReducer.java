package biz.novelinsight.hypercube;

/*
 * Interface for calculating VST parameter reductions.
 */
public interface VstDimReducer {
	
	// get messages from dimension reducer that are not already read by someone
	// User Interface thread will call this every 500ms to update messages GUI.
	public String getUnreadMessages();
	
	// checks if we have write access to vst directory or file's directory (vstFile may be directory or filename)
	public boolean hasWriteAccess(String vstFile);

	// scan VST file status. If file is compatible and parameter reduction can be computed returns true
	// after this call get unread messages from getUnreadMessages()
	// starts internal thread for scanning which reports what it finds through getUnreadMessages()
	public boolean scanVSTFile(String vstFile);
	
	// returns true if internal thread for scanning is running
	public boolean isScanningComputing();

	// stops internal scanning thread, returns false if scanning is not running
	public boolean stopScanning();

	// learning method for startCalculateVSTParameterReduction()
	static final int USE_PCA = 0;
	static final int USE_TSNE = 1;
	static final int USE_VAE = 2;
	
	// starts C++ thread for calculating parameter reduction: call getUnreadMessages() to get status of computation
	public boolean startCalculateVSTParameterReduction(String vstFile, float quality, int method, boolean skipExisting);
	
	// returns true if parameter reducer is still computing
	public boolean isParameterReductionComputing();
	
	// stops computation: updates getUnreadMessages()
	public boolean stopCalculateVSTParameterReduction();
	
	// starts removal of generated vst files
	public boolean removeVSTParameterReductionFiles(String vstFile);
	
	// returns true if is removing parameter reduction VST and data files
	public boolean isRemoveComputing();
	
	// stops removal process
	public boolean stopRemoveComputing();
	
}
