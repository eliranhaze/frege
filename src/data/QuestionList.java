package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionList implements Jsonable {

	private List<Question> _questions;
	
	public QuestionList(Collection<Question> questions) {
		_questions = new ArrayList<>(questions);
	}

	@Override
	public String toJson() {
		StringBuffer json = new StringBuffer();
		json.append("{\"questions\":[");
		boolean first = true;
		for (Question question : _questions) {
			if (first) first = false; else json.append(",");
			json.append(question.toJson());
		}
		return json.append("]}").toString();
	}

}
