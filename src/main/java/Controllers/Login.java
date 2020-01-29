package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;


@Path("Login/")

public class Login {


    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLogin(@PathParam("id") Integer id) {
        System.out.println("Login/get/" + id);
        JSONObject item = new JSONObject();
        try {
            if (id == null) {
                throw new Exception("User's 'id' is missing in the HTTP request's URL.");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT id, password FROM Login WHERE id = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("id", results.getInt(1));
                item.put("password", results.getString(2));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get Login, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertLogin(@FormDataParam("id") Integer id,
                              @FormDataParam("password") String password){


        try {
            if (id == null || password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Login/new id=" + id);


            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Login(id, password) VALUES (?, ?)");

            ps.setInt(1, id);
            ps.setString(2, password);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new Login, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listThings () {
        System.out.println("Login/list");
        JSONArray list = new JSONArray();

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT id, password FROM Login");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("password", results.getString(2));
                list.add(item);

            }
            return list.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list Login, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateLogin(@FormDataParam("id") Integer id,
                              @FormDataParam("password") String password) {

        try {
            if (id == null || password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Login/update id=" + id);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Login SET password = ? WHERE id = ?");
            ps.setString(1, password);
            ps.setInt(2,id);
            ps.execute();
            return "{\"status\": \"OK\"}";



        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update the Login, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteLogin(@FormDataParam("id") Integer id) {

        try {
            if (id == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Login/delete id=" + id);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Login WHERE id = ?");

            ps.setInt(1, id);

            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete Login, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("id") Integer id, @FormDataParam("password") String password) {

        try {

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT password FROM Login WHERE id = ?");
            ps1.setInt(1, id);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {

                String correctPassword = loginResults.getString(1);

                if (password.equals(correctPassword)) {

                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Login SET token = ? WHERE id = ?");
                    ps2.setString(1, token);
                    ps2.setInt(2, id);
                    ps2.executeUpdate();

                    return "{\"token\": \""+ token + "\"}";

                } else {

                    return "{\"error\": \"Incorrect password!\"}";

                }

            } else {

                return "{\"error\": \"Unknown user!\"}";

            }

        }catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }


    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("Login/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT id FROM Login WHERE token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Login SET token = NULL WHERE id = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";
            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT id FROM Login WHERE token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /Login/logout: " + exception.getMessage());
            return false;
        }
    }
}



















