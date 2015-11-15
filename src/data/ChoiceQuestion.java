package data;

import java.util.List;

public class ChoiceQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1900289555673767686L;

	public List<String> choices;
	public int correctChoice;
	
	@Override
	protected StringBuffer innerToJson(StringBuffer addTo) {
		super.innerToJson(addTo).append(",").append("\"choices\":[");
		boolean first = true;
		for (String choice : choices) {
			if (first) first = false; else addTo.append(",");
			addTo.append("\"").append(jsonize(choice)).append("\"");
		}
		return addTo.append("]")
				.append(",")
				.append("\"correctChoice\":")
				.append("\"")
				.append(correctChoice)
				.append("\"");
	}
}
