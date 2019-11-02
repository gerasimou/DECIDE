package caseStudies.healthcare;

import prism.Prism;
import prism.PrismDevNullLog;
import prism.PrismException;
import prism.PrismFileLog;
import prism.PrismLog;

import java.io.File;

import decide.selection.mdp.TextFileHandler;
import prism.Preprocessor;

public class LocalModelBuilder {
	
	private String[] m_pp_in_args;
	private String m_out_model_file;
	private Prism m_prism;

	
	public String[] getM_pp_in_args() {
		return m_pp_in_args;
	}

	
	public void setM_pp_in_args(String[] m_pp_in_args) {
		this.m_pp_in_args = m_pp_in_args;
	}

	
	public String getM_out_model_file() {
		return m_out_model_file;
	}


	public void setM_out_model_file(String m_out_model_file) {
		this.m_out_model_file = m_out_model_file;
	}
	
	
	public LocalModelBuilder(String[] ppArgs, String modelExportFile) {
		m_pp_in_args = ppArgs;
		m_out_model_file = modelExportFile;
		
		try {
			// Create a log for PRISM output (hidden or stdout)
//			PrismLog mainLog = new PrismDevNullLog();
			PrismLog mainLog = new PrismFileLog("stdout");

			// Init Prism 
			m_prism = new Prism(mainLog);
			m_prism.initialise();
		} catch (PrismException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	
	public LocalModelBuilder(String[] ppArgs) {
		m_pp_in_args = ppArgs;
		
		try {
			// Create a log for PRISM output (hidden or stdout)
//			PrismLog mainLog = new PrismDevNullLog();
			PrismLog mainLog = new PrismFileLog("stdout");

			// Init Prism 
			m_prism = new Prism(mainLog);
			m_prism.initialise();
		} catch (PrismException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	
	public LocalModelBuilder() {		
		try {
			// Create a log for PRISM output (hidden or stdout)
//			PrismLog mainLog = new PrismDevNullLog();
			PrismLog mainLog = new PrismDevNullLog();// FileLog("stdout");

			// Init Prism 
			m_prism = new Prism(mainLog);
			m_prism.initialise();
		} catch (PrismException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	
	public void shutDown() {
		try {
			m_prism.closeDown();
		} catch (Exception | NoClassDefFoundError e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	
	public String preprocess(String[] ppArgs) {
		m_pp_in_args = ppArgs;
		return preprocess();
	}
	
	public String preprocess() {
		try {
			Preprocessor pp = new Preprocessor(m_prism, new File(m_pp_in_args[0]));
			
			String[] t = new String[]{m_pp_in_args[1], m_pp_in_args[2]};
			pp.setParameters(m_pp_in_args);
			String s = pp.preprocess();
			if (s == null) {
				System.out.println("Error: No preprocessing information.");
			} else {
//				System.out.print(s);
				return s;
			}
		} catch (PrismException e) {
			System.err.println("Error: " + e.getMessage());
			return e.getMessage();
		}
		return "";
	}
	

	public void exportModel(String code, String fileName) {
		TextFileHandler fh = new TextFileHandler(fileName);
		fh.exportFile(code);	
	}

	// Class test
	public static void main(String[] args) {
						
		String workPath = "models/healthcare/local/";//Users/sgerasimou/Documents/Git/DECIDE/DECIDE_Framework/models/healthcare/local/";
		String[] ppArgs = {workPath+"rooms.pp", "3", "5"};
		String modelFile = workPath+"rooms.prism";
		LocalModelBuilder lmb = new LocalModelBuilder(ppArgs,modelFile);
		lmb.exportModel(lmb.preprocess(), lmb.getM_out_model_file());
		lmb.shutDown(); 
		
		
	}
	
}
