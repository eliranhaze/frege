package data;

public class DeductionQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6609929850299251607L;

	public String argument;
	
	@Override
	protected StringBuffer innerToJson(StringBuffer addTo) {
		return super.innerToJson(addTo)
				.append(",")
				.append("\"argument\":")
				.append("\"")
				.append(argument)
				.append("\"");
	}
}
