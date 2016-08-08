package com.knaujolimac.chataplicacion.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@SuppressWarnings("serial")
public class SendServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(SendServlet.class.getCanonicalName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Sends a message to the GCM server.");		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String msg = req.getParameter(Constantes.MESSAGE);
		String from = req.getParameter(Constantes.SENDER_EMAIL);
		String to = req.getParameter(Constantes.RECEIVER_EMAIL);
		
		Contacto contact = null;
		EntityManager em = EMFService.get().createEntityManager();
		try {
			contact = Contacto.find(to, em);
			if (contact == null){
				logger.log(Level.WARNING, "contact=0: ");
				return;
			}
		} finally {
			em.close();
		}
		logger.log(Level.WARNING, "msg= "+ msg);
		logger.log(Level.WARNING, "from= "+ from);
		logger.log(Level.WARNING, "to= "+ to);

		String regId = contact.getRegId();
		Sender sender = new Sender(Constantes.API_KEY);
		Message message = new Message.Builder()
//			.delayWhileIdle(true)
			.addData(Constantes.RECEIVER_EMAIL, to).addData(Constantes.SENDER_EMAIL, from).addData(Constantes.MESSAGE, msg)
			.build();
		
		
		try {
			Result result = sender.send(message, regId, 5);
/*			List<String> regIds = new ArrayList<String>();
			regIds.add(regId);
			MulticastResult result = sender.send(message, regIds, 5);*/
			
			logger.log(Level.WARNING, "Result: " + result.toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

}
