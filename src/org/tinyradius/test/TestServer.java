/**
 * $Id: TestServer.java,v 1.6 2006/02/17 18:14:54 wuttke Exp $
 * Created on 08.04.2005
 * @author Matthias Wuttke
 * @version $Revision: 1.6 $
 */
package org.tinyradius.test;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;
import org.tinyradius.util.RadiusServer;

import java.sql.*;

/**
 * Test server which terminates after 30 s.
 * Knows only the client "localhost" with secret "testing123" and
 * the user "mw" with the password "test".
 */
public class TestServer {
	
	public static void main(String[] args) 
	throws IOException, Exception {
		RadiusServer server = new RadiusServer() {
			// Authorize localhost/testing123
			public String getSharedSecret(InetSocketAddress client) {
//				if (client.getAddress().getHostAddress().equals("127.0.0.1"))
//					return "testing123";
//				else
//					return null;
				
				
//				if (getauthen("userLY").equals("success"))  {
//					//System.out.println("should allow authen \n");
//					return "testing123";
//				}
//				else   {
//					return null;
//				}
				
				
				return "testing123";    //this mean allow any client IP    .LY
				
			}
			
			// Authenticate mw
			public String getUserPassword(String userName) {
				if (userName.equals("mw"))
					return "test";
				else
					return null;
				
			}
			
			// Adds an attribute to the Access-Accept packet
			public RadiusPacket accessRequestReceived(AccessRequest accessRequest, InetSocketAddress client) 
			throws RadiusException {
				System.out.println("Received Access-Request:\n" + accessRequest);
				RadiusPacket packet = super.accessRequestReceived(accessRequest, client);
				if (packet.getPacketType() == RadiusPacket.ACCESS_ACCEPT)
					packet.addAttribute("Reply-Message", "Welcome " + accessRequest.getUserName() + "!");
				if (packet == null)
					System.out.println("Ignore packet.");
				else
					System.out.println("Answer:\n" + packet);
				return packet;
			}
			
			
			//
			public String getauthen(String username) {
				 
			    CallableStatement cstmt = null;

				// Create a variable for the connection string.
				//String connectionUrl = "jdbc:sqlserver://hk.worldhubcom.cn:1026;" +
					//"databaseName=Pack2018;user=sa;password=Passw0rdPassw0rd123";

				// Declare the JDBC objects.
				Connection con = null;
				
				String sStatus =null;
				
				
			    //List&lt;String&gt; employeeFullName = new ArrayList&lt;&gt;();
			 
			    try {
			    	
			    	con = DataSource.getInstance().getConnection();
			           
//			    	
//			         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
//			         con = DriverManager.getConnection(connectionUrl);  
			    	
			         
			         System.out.println("connection success \n");
			         
			        cstmt = con.prepareCall(
			                "{call sp_simple_sp_ly(?,?)}",
			                ResultSet.TYPE_SCROLL_INSENSITIVE,
			                ResultSet.CONCUR_READ_ONLY);
			 
			        cstmt.setString("I_username", username);
			        cstmt.registerOutParameter("O_sMsg", java.sql.Types.VARCHAR);
			        
			        
			        cstmt.execute();
			        
			        System.out.println("exec success \n");
			        
			        sStatus = cstmt.getNString("O_sMsg");
			        
			    	System.out.println("sStatus:\n" + sStatus);
			        
			 
			    } catch (Exception ex) {
			    	System.out.println("exec sp raise error \n");
			    } finally {
			        if (cstmt != null) {
			            try {
			                cstmt.close();
			                con.close();
			            } catch (SQLException ex) {
			              
			            }
			        }
			    	
			    	
			    }
			    return sStatus;
			}
			
			
			
			
			
			
			
			
			//
			
			
			
			
			
			
			
		};
		if (args.length >= 1)
			server.setAuthPort(Integer.parseInt(args[0]));
		if (args.length >= 2)
			server.setAcctPort(Integer.parseInt(args[1]));
		
		server.start(true, true);
		
		System.out.println("Server started.");
		
		Thread.sleep(1000*60*30);
		System.out.println("Stop server");
		server.stop();
	}
	
}
