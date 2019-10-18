package Controllers;

import Server.Main;
import com.sun.jersey.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Path("Student/")
public class Student {

        /*@GET
        @Path("list")
        @Produces(MediaType.APPLICATION_JSON)
        public String listThings () {
            System.out.println("Student/list");
            JSONArray list = new JSONArray();

            try {

                PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Name, Age ,Email FROM Student");

                ResultSet results = ps.executeQuery();
                while (results.next()) {
                    JSONObject item = new JSONObject();
                    item.put("UserID", results.getInt(1));
                    item.put("Name", results.getString(2));
                    item.put("Age", results.getInt(3));
                    item.put("Email", results.getString(4));
                    list.add(item);

                }
                return list.toString();

            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
            }
        }*/




        @POST
        @Path("delete")
        @Consumes(MediaType.MULTIPART_FORM_DATA)
        @Produces(MediaType.APPLICATION_JSON)
        public String deleteThing(@FormDataParam("UserID") Integer UserID) {

            try {
                if (UserID == null) {
                    throw new Exception("One or more form data parameters are missing in the HTTP request.");
                }
                System.out.println("Student/delete UserID=" + UserID);

                PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Student WHERE UserID = ?");

                ps.setInt(1, UserID);

                ps.execute();
                return "{\"status\": \"OK\"}";

            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
            }
        }
}




