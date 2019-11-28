package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Path("Student/")

public class Student {





       @POST
       @Path("new")
       @Consumes(MediaType.MULTIPART_FORM_DATA)
       @Produces(MediaType.APPLICATION_JSON)
       public String insertThing(@FormDataParam("UserID") Integer UserID, @FormDataParam("Name") String Name, @FormDataParam("Age") Integer Age, @FormDataParam("Email") String Email) {


           try {
               if (UserID == null || Name == null || Age == null || Email == null) {
                   throw new Exception("One or more form data parameters are missing in the HTTP request.");
               }
               System.out.println("Student/new UserID=" + UserID);


               PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Student(UserID, Name, Age, Email) VALUES (?, ?, ?, ?)");

               ps.setInt(1, UserID);
               ps.setString(2, Name);
               ps.setInt(3, Age);
               ps.setString(4, Email);
               ps.execute();
               return "{\"status\": \"OK\"}";


           } catch (Exception exception) {
               System.out.println("Database error: " + exception.getMessage());
               return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
           }
       }

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

       @POST
       @Path("update")
       @Consumes(MediaType.MULTIPART_FORM_DATA)
       @Produces(MediaType.APPLICATION_JSON)
       public String updateThing(@FormDataParam("UserID") Integer UserID,  @FormDataParam("Name") String Name, @FormDataParam("Agw") Integer Age, @FormDataParam("Email") String Email) {

           try {
               if (UserID == null || Name == null || Age == null || Email == null) {
                   throw new Exception("One or more form data parameters are missing in the HTTP request.");
               }
               System.out.println("Student/update UserID=" + UserID);

               PreparedStatement ps = Main.db.prepareStatement(
                       "UPDATE Student SET Name = ?, Age = ? , Email = ?   WHERE UserID = ?");
               ps.setString(1, Name);
               ps.setInt(2, Age);
               ps.setString(2,Email);
               ps.setInt(4, UserID);
               ps.execute();
               return "{\"status\": \"OK\"}";



           } catch (Exception exception) {
               System.out.println("Database error: " + exception.getMessage());
               return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
           }
       }

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





