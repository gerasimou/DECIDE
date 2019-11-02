package decide.component.requirements;

import decide.configuration.Configuration;

public class DefaultModelTemplateDelegate extends ModelTemplateDelegate{

	
	public DefaultModelTemplateDelegate (String modelTemplate) {
		super(modelTemplate);
	}
	
	@Override
	public String getModelTemplate(Configuration configuration) {
		return this.modelTemplate +"\n";
	}
	
	

}
