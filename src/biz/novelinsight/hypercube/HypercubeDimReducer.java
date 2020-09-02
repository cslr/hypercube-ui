package biz.novelinsight.hypercube;

public class HypercubeDimReducer implements VstDimReducer {
	
	static {
		System.loadLibrary("hypercubejni"); // hypercubejni.dll must contain implementation of interface functions
	}
	
	// get messages from dimension reducer that are not already read by someone
	// User Interface thread will call this every 500ms to update messages GUI.
	native public String getUnreadMessages();
	
	// checks if we have write access to vst directory or file's directory (vstFile may be directory or filename)
	native public boolean hasWriteAccess(String vstFile);
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// scan VST file status. If file is compatible and parameter reduction can be computed returns true
	// after this call get unread messages from getUnreadMessages()
	native public boolean scanVSTFile(String vstFile);
	
	// returns true if internal thread for scanning is running
	native public boolean isScanningComputing();

	// stops internal scanning thread, returns false if scanning is not running
	native public boolean stopScanning();
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// starts C++ thread for calculating parameter reduction: call getUnreadMessages() to get status of computation
	native public boolean 
		startCalculateVSTParameterReduction(String vstFile, float quality, int method, boolean skipExisting);
	
	// returns true if parameter reducer is still computing
	native public boolean isParameterReductionComputing();
	
	// stops computation: updates getUnreadMessages()
	native public boolean stopCalculateVSTParameterReduction();

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// starts removal of generated vst files
	native public boolean removeVSTParameterReductionFiles(String vstFile);
		
	// returns true if is removing parameter reduction VST and data files
	native public boolean isRemoveComputing();
	
	// stops removal process
	native public boolean stopRemoveComputing();
}
