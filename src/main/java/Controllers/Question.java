package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Path("Question/")

public class Question {


    @GET
    @Path("get/{questionID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuestion(@PathParam("questionID") Integer questionID) {
        System.out.println("Question/get/" + questionID);

        try {
            if (questionID == null) {
                throw new Exception("Question's 'id' is missing in the HTTP request's URL.");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT question FROM Question WHERE questionID = ?");
            ps.setInt(1, questionID);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                JSONObject item = new JSONObject();
                item.put("question", results.getString(1));
                return item.toString();
            }else{
                return "{\"error\": \"Unable to find question with that id.\"}";
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get question, please see server console for more info.\"}";
        }
    }





    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertQuestion(@FormDataParam("questionID") Integer questionID,
                              @FormDataParam("question") String question,
                              @FormDataParam("quizID") Integer quizID
                              ) {

        try {
            if (questionID == null || question == null || quizID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Question/new questionID=" + questionID);


            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Question(questionID, question, quizID) VALUES (?, ?, ?)");

            ps.setInt(1, questionID);
            ps.setString(2, question);
            ps.setInt(3, quizID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new question, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listQuestion () {
        System.out.println("Question/list");
        JSONArray list = new JSONArray();

        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT questionID, question, quizID FROM Question");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("questionID", results.getInt(1));
                item.put("question", results.getString(2));
                item.put("quizID", results.getInt(3));
                list.add(item);

            }
            return list.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list questions, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateQuestion(@FormDataParam("questionID") Integer questionID,
                                @FormDataParam("question") String question){

        try {
            if (questionID == null || question == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Question/update questionID=" + questionID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Question SET question = ?   WHERE id = ?");
            ps.setString(1, question);
            ps.setInt(2, questionID);
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
    public String deleteQuestion(@FormDataParam("questionID") Integer questionID) {

        try {
            if (questionID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Question/delete questionID=" + questionID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Question WHERE questionID = ?");

            ps.setInt(1, questionID);

            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete question, please see server console for more info.\"}";
        }
    }








}





