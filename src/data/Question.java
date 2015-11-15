package data;

import java.io.Serializable;
import java.util.Comparator;

public abstract class Question implements Serializable, Jsonable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 243460442544407984L;

	public String subject;
	public String title;
	public int chapter;
	public int number;
	public String text;
	
	public static Comparator<Question> COMPARATOR = new Comparator<Question>() {
		
		@Override
		public int compare(Question q1, Question q2) {
			if (q1.chapter == q2.chapter) {
				return Integer.compare(q1.number, q2.number);
			}
			return Integer.compare(q1.chapter, q2.chapter);
		}
	};
	@Override
	public String toJson() {
		StringBuffer jsonObject = new StringBuffer();
		jsonObject.append("{");
		innerToJson(jsonObject).append("}");
		return jsonObject.toString();
	}
	
	protected StringBuffer innerToJson(StringBuffer addTo) {
		return addTo
				.append("\"subject\":")
				.append("\"")
				.append(jsonize(subject))
				.append("\",")
				.append("\"title\":")
				.append("\"")
				.append(jsonize(title))
				.append("\",")
				.append("\"chapter\":")
				.append("\"")
				.append(chapter)
				.append("\",")
				.append("\"number\":")
				.append("\"")
				.append(number)
				.append("\",")
				.append("\"text\":")
				.append("\"")
				.append(jsonize(text))
				.append("\",")
				.append("\"type\":")
				.append("\"")
				.append(this.getClass().getSimpleName())
				.append("\"");
	}
	
	protected String jsonize(String s) {
		return s.replace("\n", "\\n").replace("\"", "\\\"");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chapter;
		result = prime * result + number;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (chapter != other.chapter)
			return false;
		if (number != other.number)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}	
}
