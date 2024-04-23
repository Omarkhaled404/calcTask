package ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class service {
	 @PersistenceContext(unitName="hello")
     private EntityManager entityManager;
	 	
	  @POST
	  @Path("calc")
	  public Response performCalculation(calculator calculation) {
	    int result = 0;
	    switch (calculation.getOperation()) {
	      case "+":
	        result = calculation.getNumber1() + calculation.getNumber2();
	        break;
	      case "-":
	        result = calculation.getNumber1() - calculation.getNumber2();
	        break;
	      case "*":
	        result = calculation.getNumber1() * calculation.getNumber2();
	        break;
	      case "/":
	          if (calculation.getNumber2() == 0) {
	            return Response
	                .status(Response.Status.BAD_REQUEST)
	                .entity("Division by zero is not allowed")
	                .build();
	          }
	          result = calculation.getNumber1() / calculation.getNumber2();
	          break;
	      default:
	    	  return Response
	                    .status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Unsupported operation")
	                    .build();	    }
	    entityManager.persist(calculation);
        return Response.ok(new result(result)).build();
	  }
	  
	  @GET
	  @Path("calculations")
	  public Response getCalculations() {
		
		        try {
		        	 String simpleQuery="SELECT c from calculator c";
		   		  	TypedQuery<calculator> query = entityManager.createQuery(simpleQuery, calculator.class);
		   		  	List<calculator> calculations = query.getResultList();
		   		  	
		            return Response.ok(calculations).build();
		        } catch (RuntimeException err) {
		            return Response
		                    .status(Response.Status.INTERNAL_SERVER_ERROR)
		                    .entity(err.getMessage())
		                    .build();
		        }
	  }
	  
}