package decide.selection.mdp;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismDevNullLog;
import prism.PrismException;
import prism.PrismLog;
import prism.PrismSettings;
import prism.Result;
import prism.Preprocessor;


public class MDPAdversaryGeneration {


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


	public String getM_in_properties_file() {
		return m_in_properties_file;
	}


	public void setM_in_properties_file(String m_in_properties_file) {
		this.m_in_properties_file = m_in_properties_file;
	}


	public String getM_out_adv_file() {
		return m_out_adv_file;
	}


	public void setM_out_adv_file(String m_out_adv_file) {
		this.m_out_adv_file = m_out_adv_file;
	}

	public ArrayList<String> getAdv(){
		return m_pr.getPlan();
	}
	
	private String[] m_pp_in_args;
	private String m_out_model_file;
	private String m_in_properties_file;
	private String m_out_adv_file;
	private Prism m_prism;
	private PrismPolicyReader m_pr;

		
	public MDPAdversaryGeneration(String[] ppArgs, String modelExportFile, String propertiesFile, String advExportFile) {
		m_pp_in_args = ppArgs;
		m_out_model_file = modelExportFile;
		m_in_properties_file = propertiesFile;
		m_out_adv_file = advExportFile;	
		
		try {
			// Create a log for PRISM output (hidden or stdout)
			PrismLog mainLog = new PrismDevNullLog();
			//PrismLog mainLog = new PrismFileLog("stdout");
	
			// Init Prism 
			m_prism = new Prism(mainLog);
			m_prism.initialise();
		} catch (PrismException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
		
		m_pr = new PrismPolicyReader(m_out_adv_file);
	}
	
	
	public void shutDown() {
		try {
			m_prism.closeDown();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public String preprocess(String[] args) {
		try {
			Preprocessor pp = new Preprocessor(m_prism, new File(args[0]));
			pp.setParameters(args);
			String s = pp.preprocess();
			if (s == null) {
				System.out.println("Error: No preprocessing information.");
			} else {
	//			System.out.print(s);
				return s;
			}
		} catch (PrismException e) {
			System.err.println("Error: " + e.getMessage());
			return e.getMessage();
		}
		return "";
	}
	
	
	public void run() {
		try {
				
			// Parse and load a PRISM model from a file
			ModulesFile modulesFile = m_prism.parseModelFile(new File(m_out_model_file));
			m_prism.loadPRISMModel(modulesFile);
			// Parse and load a properties model for the model
			PropertiesFile propertiesFile = m_prism.parsePropertiesFile(modulesFile, new File(m_in_properties_file));
	
			// Configure PRISM to export an optimal adversary to a file when model checking an MDP 
			m_prism.getSettings().set(PrismSettings.PRISM_EXPORT_ADV, "MDP");
			m_prism.getSettings().set(PrismSettings.PRISM_EXPORT_ADV_FILENAME, m_out_adv_file);
			
			// Model check the first property from the file
	//		System.out.println(propertiesFile.getPropertyObject(1));
			
			Result result = m_prism.modelCheck(propertiesFile, propertiesFile.getPropertyObject(1));
	//		System.out.println(result.getResult());
	
	        m_pr.readPolicy();  
					
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} catch (PrismException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public ArrayList<String> getPlan(){
		return m_pr.getPlan();
	}


}