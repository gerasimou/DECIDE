package caseStudies.healthcare;

import decide.evaluator.BinaryEvaluator;

public class RobotBinaryEvaluator extends BinaryEvaluator {

	public RobotBinaryEvaluator(String parameterName, double ub, double maxSteps) {
		super(parameterName, ub, maxSteps);
	}
	
	
	// To be substituted by model checking invocation of property
	// P>=0.095[F<=t "end"]
	@Override
	protected boolean eval (String model, String property, double parameterValue){
		property = property.replaceFirst(parameterName, parameterValue +""); 				
		double result = prism.run(model, property); 
		if (result == 1.0)
			return true;

		return false;		
	}

}
