package parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import data.ChoiceQuestion;
import data.DeductionQuestion;
import data.OpenQuestion;
import data.Question;
import data.QuestionDB;
import data.TruthTableQuestion;

public class DataParser {

	static int currentGroup = 0;
	static int groupCount = 0;
	static int currentQuestion = 0;
	
	public static void main(String[] args) throws IOException {
		Collection<Question> questions = new ArrayList<>();
		File file = new File("practice.qst");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("WINDOWS-1255")));
		List<String> lines = new ArrayList<>();
		String line = "";
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		Iterator<String> lineIterator = lines.iterator();
		// Go over the lines.
		while (lineIterator.hasNext()) {
			questions.add(parseQuestion(lineIterator));
		}
		System.out.println("Finished parsing " + questions.size() + " questions.");
		System.out.println("Writing data to file.");
		QuestionDB.INSTANCE.write(questions);
		System.out.println("Reading it back.");
		questions = QuestionDB.INSTANCE.read();
		for (Question qst : questions) {
			System.out.println("Question " + qst.chapter + "." + qst.number + ":");
			System.out.println(qst.text);
		}
	}

	private static Question parseQuestion(Iterator<String> iterator) {
		System.out.println("Start parsing question.");
		Question qst = null;
		currentQuestion++;
		if (currentQuestion > groupCount) {
			// Get group info.
			currentGroup = Integer.parseInt(iterator.next());
			groupCount = Integer.parseInt(iterator.next());
			currentQuestion = 1;
		}

		// Skip code and next 2 lines.
		iterator.next();
		iterator.next();
		iterator.next();
		
		// Get common data.
		int chapter = Integer.parseInt(iterator.next());
		int qnum = Integer.parseInt(iterator.next());
		String title = iterator.next();
		String subject = iterator.next();
		
		// Skip next 2 lines.
		iterator.next();
		iterator.next();
		
		// Next line gives a clue as to the type of question.
		String clueLine = iterator.next(); 
		Integer answerCount = getInt(clueLine);
		if (answerCount == null) {
			// Either Truth-Table or Deduction question.
			// The clue line is a formula in these cases.
			String formula = clueLine;
			// Get the question text;
			String text = iterator.next();
			if (isDeduction(chapter)) {
				qst = new DeductionQuestion();
				((DeductionQuestion)qst).argument = formula;
			} else {
				qst = new TruthTableQuestion();
				((TruthTableQuestion)qst).formula = formula;
			}
			qst.text = text;
			// Skip next 2 lines.
			iterator.next();
			iterator.next();
		} else {
			// Either Open or Choice question.
			// Get answers according to count.
			List<String> answers = new ArrayList<>(answerCount);
			for (int i = 0; i < answerCount; i++) {
				answers.add(iterator.next());
			}
			// Next line is either question lines count or a meaningless number.
			int lineCount = Integer.parseInt(iterator.next());
			// If that is a line count then the next line must be a String (and it's an Open question),
			// otherwise it will be another meaningless number (and it's a Choice question).
			clueLine = iterator.next();
			Integer clueNumber = getInt(clueLine);
			if (clueNumber == null) {
				// Open question.
				qst = new OpenQuestion();
				// Get the question text lines, starting with the first one we already have.
				String text = clueLine;
				for (int i = 0; i < lineCount - 1; i++) {
					text += '\n' + iterator.next();
				}
				// Skip next 4 lines.
				iterator.next();
				iterator.next();
				iterator.next();
				iterator.next();
				qst.text = text;
				((OpenQuestion) qst).answers = new HashSet<>(answers);
			} else {
				// Choice Question.
				qst = new ChoiceQuestion();
				// Skip numbers until we get to the question text. Save the last
				// two numbers since they are the correct answer and question line count.
				int correctAnswer = 0;
				lineCount = 0;
				String currentLine = iterator.next();
				Integer currentNumber;
				while ((currentNumber = getInt(currentLine)) != null) {
					// Set the previous number to correct answer.
					correctAnswer = lineCount;
					// And set the current number to line count.
					lineCount = currentNumber;
					// Next line.
					currentLine = iterator.next();
				}
				// Get the question text lines, starting with the first one we already have.
				String text = currentLine;
				for (int i = 0; i < lineCount - 1; i++) {
					text += '\n' + iterator.next();
				}
				qst.text = text;
				((ChoiceQuestion)qst).choices = answers;
				((ChoiceQuestion)qst).correctChoice = correctAnswer;
			}
		}
		
		// Set common data.
		qst.chapter = chapter;
		qst.number = qnum;
		qst.subject = subject;
		qst.title = title;
		
		System.out.println("Done parsing question " + qst.chapter + "." + qst.number);
		System.out.println(qst.text);
		return qst;
	}
	
	private static boolean isDeduction(int chapter) {
		return false;
	}

	private static Integer getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
