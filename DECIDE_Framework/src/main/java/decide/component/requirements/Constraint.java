package decide.component.requirements;

public abstract class Constraint extends RequirementNew {
	

	/** constraint threshold */
	private Object threshold;
		
	private Object value;
	
	
	public Constraint(RequirementType reqType, String id, Object threshold) {
		super(reqType, id);
		this.threshold 					= threshold;
//		this.constraintExpressionType	= constExpressionType;
	}

//	public Object evaluate(List<Constraint> ) {
//		switch (constraintExpressionType) {
//			case ADDITION:{
//				
//				break;
//			}
//		}
//		
//		return this.value;
//	}
	
	
	public Object getValue() {
		return this.value;
	}
}
