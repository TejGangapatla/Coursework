package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Path("Answer/")

public class Answer {


    @GET
    @Path("get/{answerID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnswer(@PathParam("answerID") Integer answerID) {
        System.out.println("Answer/get/" + answerID);

        try {
            if (answerID == null) {
                throw new Exception("Answer's 'id' is missing in the HTTP request's URL.");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT answer FROM Answer WHERE answerID = ?");
            ps.setInt(1, answerID);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                JSONObject item = new JSONObject();
                item.put("answer", results.getString(1));
                return item.toString();
            }else{
                return "{\"error\": \"Unable to find answer with that id.\"}";
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get answer, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertAnswer(@FormDataParam("answerID") Integer answerID,
                                 @FormDataParam("answer") String answer,
                                 @FormDataParam("questionID") Integer questionID,
                                 @FormDataParam("correct") Boolean correct) {

        try {
            if (answerID == null || answer == null || questionID == null || correct == null ) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Answer/new answerID=" + answerID);


            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Answer(answerID, answer, questionID, correct) VALUES (?, ?, ?, ?)");

            ps.setInt(1, answerID);
            ps.setString(2, answer);
            ps.setInt(3, questionID);
            ps.setBoolean(4, correct);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new answer, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateAnswer(@FormDataParam("answerID") Integer answerID,
                                 @FormDataParam("answer") String answer){

        try {
            if (answerID == null || answer == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Answer/update answerID=" + answerID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Answer SET answer = ?   WHERE answerID = ?");
            ps.setString(1, answer);
            ps.setInt(2, answerID);
            ps.execute();
            return "{\"status\": \"OK\"}";



        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update answer, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteAnswer(@FormDataParam("answer") Integer answerID) {

        try {
            if (answerID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("Answer/delete answerID=" + answerID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Answer WHERE answerID = ?");

            ps.setInt(1, answerID);

            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete answer, please see server console for more info.\"}";
        }
    }








}





