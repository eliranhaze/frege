package data;

public class TruthTableQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1133524074131777450L;

	public String formula;
	
	@Override
	protected StringBuffer innerToJson(StringBuffer addTo) {
		return super.innerToJson(addTo)
				.append(",")
				.append("\"formula\":")
				.append("\"")
				.append(formula)
				.append("\"");
	}
}
