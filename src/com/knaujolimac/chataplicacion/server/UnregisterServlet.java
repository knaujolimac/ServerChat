package com.knaujolimac.chataplicacion.server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;


@SuppressWarnings("serial")
public class UnregisterServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(UnregisterServlet.class.getCanonicalName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Unregisters a device from the Demo server.");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter(Constantes.SENDER_EMAIL);
		
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Contacto contact = Contacto.find(email, em);
			if (contact != null) {
				em.remove(contact);
				logger.log(Level.WARNING, "Unregistered: " + contact.getId());
			}
		} finally {
			em.close();
		}		
	}
	
}
