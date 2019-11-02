package decide.evaluator;

import auxiliary.Utility;
import decide.DECIDEConstants;
import decide.qv.prism.PrismAPINew;

/**
 * 
 */

/**
 * @author Javier Camara
 *
 */
public class BinaryEvaluator implements AttributeEvaluatorNew{
	
	protected double ub;
	protected double maxSteps;
	protected String parameterName;

	/** PrismAPI handler */
	protected PrismAPINew prism;
	protected String RQV_OUTPUT_FILENAME	= Utility.getProperty(DECIDEConstants.PRISM_OUTPUT_FILENAME);

	
	
	
	public BinaryEvaluator (String parameterName, double ub, double maxSteps) {
		this.ub 			= ub;
		this.maxSteps 		= maxSteps;
		this. parameterName = parameterName;
		
		//init prism instance
		this.prism = new PrismAPINew(RQV_OUTPUT_FILENAME);		
	}
	
	
	
	/**
	 * @param ub - Upper bound for the search
	 * @param maxSteps - Maximum number of iterations
	 * @return - Overapproximation of t satisfying -eval- obtained within maxSteps iterations
	 */
	public double runBinaryEvaluation(String model, String property){
		int c=0;
		double l=Double.MIN_VALUE, h=ub, m=0;
		while (c<maxSteps){
			m=(h+l)/2.0;
			//System.out.println("L:"+l+" H:"+h +"   - M:"+m);
			if (eval(model, property, m))
				h=m;
			else
				l=m;
			c++;
		}
		if (eval(model, property, m)) return m;
		if (eval(model, property, h)) return h;
		return -1;
	}
	
	
	// To be substituted by model checking invocation of property
	// P>=0.095[F<=t "end"]
	protected boolean eval (String model, String property, double parameterValue){
		String finalModel = model +"\n\n"+ "const double " + parameterName +" = " + parameterValue +";"; 
		double result = prism.run(finalModel, property);
		if (result == 1.0)
			return true;

		return false;		
	}




	@Override
	public double run (String model, String property) {
		return runBinaryEvaluation(model, property);
	}



	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}





///**
// * @param args
// */
//public static void main(String[] args) {
//	// TODO Auto-generated method stub
//
////	BinaryEvaluator be = new BinaryEvaluator();
////	System.out.println(be.evalTime(5,20));
//}