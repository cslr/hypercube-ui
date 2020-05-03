package biz.novelinsight.hypercube;

public class HypercubeDimReducer implements VstDimReducer {
	
	static {
		System.loadLibrary("hypercubejni"); // hypercubejni.dll must contain implementation of interface functions
	}
	
	// get messages from dimension reducer that are not already read by someone
	// User Interface thread will call this every 500ms to update messages GUI.
	native public String getUnreadMessages();
	
	// NOTE: vstFile parameter can contain wildcards to process one by one all possible vstFiles.

	// scan VST file status. If file is compatible and parameter reduction can be computed returns true
	// after this call get unread messages from getUnreadMessages()
	native public boolean scanVSTFile(String vstFile);
	
	// starts C++ thread for calculating parameter reduction: call getUnreadMessages() to get status of computation
	native public boolean 
		startCalculateVSTParameterReduction(String vstFile, float quality, boolean useVAE, boolean skipExisting);
	
	// returns true if parameter reducer is still computing
	native public boolean isParameterReductionComputing();
	
	// stops computation: updates getUnreadMessages()
	native public boolean stopCalculateVSTParameterReduction();
	
	// remove generated vst files
	native public boolean removeVSTParameterReductionFiles(String vstFile);

}
