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


    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFruit(@PathParam("id") Integer id) {
        System.out.println("Student/get/" + id);
        JSONObject item = new JSONObject();
        try {
            if (id == null) {
                throw new Exception("Student's 'id' is missing in the HTTP request's URL.");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT id, name, age, email FROM Student WHERE id = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("age", results.getInt(3));
                item.put("email", results.getString(4));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get Student, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertThing(@FormDataParam("id") Integer id,
                             @FormDataParam("name") String name,
                             @FormDataParam("age") Integer age,
                             @FormDataParam("email") String email) {


       try {
           if (id == null || name == null || age == null || email == null) {
               throw new Exception("One or more form data parameters are missing in the HTTP request.");
           }
           System.out.println("Student/new id=" + id);


           PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Student(id, name, age, email) VALUES (?, ?, ?, ?)");

           ps.setInt(1, id);
           ps.setString(2, name);
           ps.setInt(3, age);
           ps.setString(4, email);
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

           PreparedStatement ps = Main.db.prepareStatement("SELECT id, name, age ,email FROM Student");

           ResultSet results = ps.executeQuery();
           while (results.next()) {
               JSONObject item = new JSONObject();
               item.put("id", results.getInt(1));
               item.put("name", results.getString(2));
               item.put("age", results.getInt(3));
               item.put("email", results.getString(4));
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
    public String updateThing(@FormDataParam("id") Integer id,
                             @FormDataParam("name") String name,
                             @FormDataParam("age") Integer age,
                             @FormDataParam("email") String email) {

       try {
           if (id == null || name == null || age == null || email == null) {
               throw new Exception("One or more form data parameters are missing in the HTTP request.");
           }
           System.out.println("Student/update id=" + id);

           PreparedStatement ps = Main.db.prepareStatement("UPDATE Student SET name = ?, age = ? , email = ?   WHERE id = ?");
           ps.setString(1, name);
           ps.setInt(2, age);
           ps.setString(2,email);
           ps.setInt(4, id);
           ps.execute();
           return "{\"status\": \"OK\"}";



       } catch (Exception exception) {
           System.out.println("Database error: " + exception.getMessage());
           return "{\"error\": \"Unable to update Student, please see server console for more info.\"}";
       }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteThing(@FormDataParam("id") Integer id) {

       try {
           if (id == null) {
               throw new Exception("One or more form data parameters are missing in the HTTP request.");
           }
           System.out.println("Student/delete id=" + id);

           PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Student WHERE id = ?");

           ps.setInt(1, id);

           ps.execute();
           return "{\"status\": \"OK\"}";

       } catch (Exception exception) {
           System.out.println("Database error: " + exception.getMessage());
           return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
       }
    }








}





