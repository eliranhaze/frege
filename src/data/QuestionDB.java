package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class QuestionDB {

	private final static File DATA_FILE = new File("/home/eliranh/workspace-mars/Logicore/questions.dat");
	
	public final static QuestionDB INSTANCE = new QuestionDB();
	
	private Collection<Question> _questions = null;
	private Map<Integer, Collection<Question>> _questionsByChapter = null;
	
	private QuestionDB() {}
	
	public void write(Collection<Question> questions) {
		try {
			if (!DATA_FILE.exists()) {
				DATA_FILE.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(DATA_FILE);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(questions);
			oout.flush();
			oout.close();
			fout.close();
			updateData(questions);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Question> read() {
		Collection<Question> questions = new ArrayList<>();
		try {
			FileInputStream fin = new FileInputStream(DATA_FILE);
			ObjectInputStream oin = new ObjectInputStream(fin);
			questions = (Collection<Question>) oin.readObject();
			oin.close();
			fin.close();
			updateData(questions);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return questions;
	}
	
	public Collection<Question> getAll() {
		if (_questions == null) {
			read();
		}
		return _questions;
	}
	
	public Collection<Question> getByChapter(int chapter) {
		if (_questionsByChapter == null) {
			read();
		}
		return _questionsByChapter.get(chapter);
	}
	
	private void updateData(Collection<Question> questions) {
		 _questions = questions;
		 _questionsByChapter = new HashMap<>();
		 for (Question question : questions) {
			 int chapter = question.chapter;
			 Collection<Question> byChapter = _questionsByChapter.get(chapter);
			 if (byChapter == null) {
				byChapter = new TreeSet<>(Question.COMPARATOR);
				_questionsByChapter.put(chapter, byChapter);
			 }
			 byChapter.add(question);
		}
	}
}
