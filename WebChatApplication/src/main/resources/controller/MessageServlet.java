package controller;

import static util.MessageUtil.TASKS;
import static util.MessageUtil.TOKEN;
import static util.MessageUtil.getIndex;
import static util.MessageUtil.getToken;
import static util.MessageUtil.jsonToTask;
import static util.MessageUtil.stringToJson;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import model.Message;
import model.MessageStorage;
import storage.xml.XMLHistoryUtil;
import util.ServletUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

@WebServlet("/WebChatApplication")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MessageServlet.class.getName());

	@Override
	public void init() throws ServletException {
		try {
			loadHistory();
                        
		} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
			logger.error(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("doGet");
		String token = request.getParameter(TOKEN);
		logger.info("Token " + token);

		if (token != null && !"".equals(token)) {
			int index = getIndex(token);
			logger.info("Index " + index);
			String tasks = formResponse(index);
			response.setContentType(ServletUtil.APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.print(tasks);
			out.flush();
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPost");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Message task = jsonToTask(json);
			MessageStorage.addTask(task);
			XMLHistoryUtil.addData(task);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPut");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Message task = jsonToTask(json);
			String id = task.getId();
			Message taskToUpdate = MessageStorage.getTaskById(id);
			if (taskToUpdate != null) {
				taskToUpdate.setDescription(task.getDescription());
				taskToUpdate.setDone(task.isDone());
				XMLHistoryUtil.updateData(taskToUpdate);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
			}
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

        @Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("doDelete");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Message task = jsonToTask(json);
			String id = task.getId();
			Message taskToUpdate = MessageStorage.getTaskById(id);
			if (taskToUpdate != null) {
				taskToUpdate.setDone(task.isDone());
				XMLHistoryUtil.updateData(taskToUpdate);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
			}
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
        
	@SuppressWarnings("unchecked")
	private String formResponse(int index) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(TASKS, MessageStorage.getSubTasksByIndex(index));
		jsonObject.put(TOKEN, getToken(MessageStorage.getSize()));
		return jsonObject.toJSONString();
	}

	private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
		if (XMLHistoryUtil.doesStorageExist()) {
			MessageStorage.addAll(XMLHistoryUtil.getTasks());
		} else {
			XMLHistoryUtil.createStorage();
		}
	}
}
