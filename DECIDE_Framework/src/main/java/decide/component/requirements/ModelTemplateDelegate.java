package decide.component.requirements;

import decide.configuration.Configuration;

public abstract class ModelTemplateDelegate {
	
	protected String modelTemplate;
	
	public ModelTemplateDelegate (String modelTemplate) {
		this.modelTemplate = modelTemplate;
	}
	
		
	public void setModelTemplate (String modelTemplate) {
		this.modelTemplate = modelTemplate;
	}
	
	
	
	public abstract String getModelTemplate(Configuration configuration);

}
