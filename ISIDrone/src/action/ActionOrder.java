package action;

import java.util.List;

import entities.Cart;
import entities.Order;
import entities.User;
import java.lang.management.MemoryUsage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.MOrder;
import manager.MSendMail;
import manager.MUser;

public class ActionOrder {
	
	// Methode executé lorse qu'une commande est completé
	public static int process(User user, Cart cart){
		
		// Ajoute la commande a la base de donnée
		int orderId = MOrder.add(user, cart);
		
		// Envoie d'un email de confirmation
		String to = user.getEmail();
		sendEmail(to);
		
		return orderId;
	}
	
	private static void sendEmail(String to){
		
		// Envoie d'un email de confirmation
		String sujet = "Achat completé";
		String message = "Félicitation, vous avez commander quelquechose.";
		
		MSendMail.sendEmail(message, to, sujet);
		
	}
	
	public static List<Order> getHistoryByUserID(int userId){
		List<Order> orderList = MOrder.getAllOrdersByUserId(userId);
		
		return orderList;
	}

    public static void getOrders(HttpServletRequest request, HttpServletResponse response) {
            request.setAttribute("orders", MOrder.getAll());
            request.setAttribute("users", MUser.getAllPlusAdmins());
    }
    
    
    public static void deleteOrderById(int idOrder, HttpServletRequest request, HttpServletResponse response) {
                        MOrder.deleteOrderById(idOrder);
                        getOrders(request, response);
    }
    
    
     public static void shipOrderById(int idOrder, int isShipped, HttpServletRequest request, HttpServletResponse response) {
        MOrder.shipOrderById(idOrder,isShipped);
        getOrders(request, response);
    }
     
    
}
