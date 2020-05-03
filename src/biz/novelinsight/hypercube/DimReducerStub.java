package biz.novelinsight.hypercube;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;

public class DimReducerStub implements VstDimReducer {
	
	private ArrayList<String> messages = new ArrayList<String>();
	private Boolean computing = new Boolean(false);
	private Object computingLock = new Object();
	
	
	// get messages from dimension reducer that are not already read by someone
	// User Interface thread will call this every 500ms to update messages GUI.
	public String getUnreadMessages()
	{
		String msg = "";
		
		synchronized(messages) {
			Iterator<String> iter = messages.iterator();
			
			while(iter.hasNext()) {
				String s = iter.next();
				msg = msg + s + "\n";
			}
			
			messages.clear();
		}
		
		return msg;
	}
	
	// NOTE: vstFile parameter can contain wildcards to process one by one all possible vstFiles.

	// scan VST file status. If file is compatible and parameter reduction can be computed returns true
	// after this call get unread messages from getUnreadMessages()
	public boolean scanVSTFile(String vstFile)
	{
		File f = new File(vstFile);
		
		if(f.exists()) {
			synchronized(messages) {
				if(f.isDirectory() == false)
					messages.add("File " + vstFile + " exists, good.");
				else
					messages.add("Directory " + vstFile + " exists, good.");
				
				return true;
			}
		}
		else
			return false;
	}
	
	// starts C++ thread for calculating parameter reduction: call getUnreadMessages() to get status of computation
	public boolean startCalculateVSTParameterReduction(String vstFile, float quality, boolean useVAE, boolean skipExisting)
	{
		if(quality <= 0.0f || quality > 10.0f) return false;
		
		synchronized(computingLock) {
			if(computing.booleanValue() == true)
				return false; // silently fails if there is already computing going on.
			
			computing= new Boolean(true);
		}
		
		File f = new File(vstFile);
		
		if(f.exists()) {
			synchronized(messages) {
				if(f.isDirectory() == false) {
					messages.add("File " + vstFile + " exists for computation good.");
				}
				else {
					for(final File fileEntry : f.listFiles()) {
						if(fileEntry.isDirectory() == false) {
							messages.add("File " + fileEntry.getName() + " exists for computation good.");
						}
					}
				}
				
				synchronized(computingLock) {
					computing= new Boolean(false);
					return true;
				}
			}
		}
		else {
			synchronized(computingLock) {
				computing= new Boolean(false);
				return false;
			}
		}
		
		
	}
	
	// returns true if parameter reducer is still computing
	public boolean isParameterReductionComputing()
	{
		synchronized(computingLock) {
			if(computing.booleanValue() == true)
				return true;
			else
				return false;
		}		
	}
	
	// stops computation: updates getUnreadMessages()
	public boolean stopCalculateVSTParameterReduction()
	{
		synchronized(computingLock) {
			if(computing.booleanValue() == false)
				return false;
			
			synchronized(messages) {
				// nothing to do in stub
				messages.add("Stopped VST reducer computing");
			}
			
			return true;
		}		
		
	}
	
	// remove generated vst files
	public boolean removeVSTParameterReductionFiles(String vstFile)
	{
		File f = new File(vstFile);
		
		if(f.exists()) {
			// nothing to do in stub
			synchronized(messages) {
				messages.add("File " + vstFile + " VST reduction files REMOVED.");
				return true;
			}
		}
		else {
			return false;
		}

	}


	
}