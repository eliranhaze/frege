package servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Question;
import data.QuestionDB;
import data.QuestionList;

/**
 * Servlet implementation class LogicoreServlet
 */
@WebServlet("/LogicoreServlet")
public class LogicoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogicoreServlet() {
        super();
        System.out.println("Constructing.");
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("Initializing.");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Getting. From: " + request.getRemoteAddr() + ". Time is " + new Date());
		response.getWriter().append("What are you trying to do? Nothing for you to see here.");
		response.getWriter().flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Posting. From: " + request.getRemoteAddr() + ". Time is " + new Date());
		String data = request.getReader().readLine();
		System.out.println("- Data received: " + data);
		int chapter = Integer.parseInt(data.substring(data.indexOf('=') + 1));
		System.out.println("- Fetching chapter " + chapter);
		Collection<Question> qs = QuestionDB.INSTANCE.getByChapter(chapter);
		QuestionList ql = new QuestionList(qs);
		String json = ql.toJson();
		System.out.println("Sending back: " + json);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append(json);
		response.getWriter().flush();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		System.out.println("Destroying.");
	}

}
