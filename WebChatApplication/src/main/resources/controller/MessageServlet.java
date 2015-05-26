package controller;

import static util.MessUtil.MESSAGES;
import static util.MessUtil.TOKEN;
import static util.MessUtil.getIndex;
import static util.MessUtil.getToken;
import static util.MessUtil.jsonToMessage;
import static util.MessUtil.stringToJson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        private static Map<String, Boolean> users = Collections.synchronizedMap(new HashMap<String,Boolean>());

	@Override
	public void init() throws ServletException {
		try {
			loadHistory();
                        for(int i = 0; i < MessageStorage.getSize(); i++) {
                            logger.info(MessageStorage.getStorage().get(i));
                        }
		} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
			logger.error(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            logger.info("doGet");
		String token = request.getParameter(TOKEN);
                String user = request.getParameter("name");
                if(!users.containsKey(user)){
                    users.put(user, Boolean.FALSE);
                }
                if(getIndex(token) == 0){
                    users.put(user, Boolean.FALSE);
                }
                if(users.get(user) == true) {
                    response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
		if (token != null && !"".equals(token)) {
			String messages = formResponse();
                        logger.info(messages);
			response.setContentType(ServletUtil.APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.print(messages);
			out.flush();
                        users.put(user, Boolean.TRUE);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                for (Map.Entry<String, Boolean> entry : users.entrySet())
                {
                    entry.setValue(Boolean.FALSE);
                }
		logger.info("doPost");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Message mess = jsonToMessage(json);
			MessageStorage.addMess(mess);
                        logger.error(mess);
			XMLHistoryUtil.addData(mess); 
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (Map.Entry<String, Boolean> entry : users.entrySet())
                {
                    entry.setValue(Boolean.FALSE);
                }
                logger.info("doPut");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Message mess = jsonToMessage(json);
			String id = mess.getId();
			Message messToUpdate = MessageStorage.getMessById(id);
			if (messToUpdate != null) {
				messToUpdate.setDescription(mess.getDescription());
				messToUpdate.setDone(mess.isDone());
                                messToUpdate.setName(mess.getName());
				XMLHistoryUtil.updateData(messToUpdate);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

        @Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for (Map.Entry<String, Boolean> entry : users.entrySet())
                {
                    entry.setValue(Boolean.FALSE);
                }
                logger.info("doDelete");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Message mess = jsonToMessage(json);
			String id = mess.getId();
			Message messToUpdate = MessageStorage.getMessById(id);
			if (messToUpdate != null) {
				messToUpdate.setDone(mess.isDone());
				XMLHistoryUtil.updateData(messToUpdate);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
        
	@SuppressWarnings("unchecked")
	private String formResponse() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(MESSAGES, MessageStorage.getSubMessagesByIndex(0));
		jsonObject.put(TOKEN, getToken(MessageStorage.getSize()));
		return jsonObject.toJSONString();
	}

	private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
		if (XMLHistoryUtil.doesStorageExist()) {
			MessageStorage.addAll(XMLHistoryUtil.getMessages());
		} else {
			XMLHistoryUtil.createStorage();
		}
	}

}