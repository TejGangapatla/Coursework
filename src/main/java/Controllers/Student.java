package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Student/")
public class Student {

        @GET
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
        }
    }




    /*static void deleteThing(int UserID) {

        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Student WHERE UserID = ?");

            ps.setInt(1, UserID);

            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }*/




