package data;

import java.util.Set;

public class OpenQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1554334478230645250L;

	public Set<String> answers;
	
	@Override
	protected StringBuffer innerToJson(StringBuffer addTo) {
		super.innerToJson(addTo).append(",").append("\"answers\":[");
		boolean first = true;
		for (String answer : answers) {
			if (first) first = false; else addTo.append(",");
			addTo.append("\"").append(jsonize(answer)).append("\"");
		}
		return addTo.append("]");
	}
}
