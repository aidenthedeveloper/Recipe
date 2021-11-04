package com.example.recipe.Model;

public class Comment {

    private String comment;
    private String commentid;
    private String publisher;

    public Comment(String comment,String commentid,String publisher){
        this.comment = comment;
        //this.username = username;
        this.commentid = commentid;
        this.publisher = publisher;
    }

    public Comment(){

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentid;
    }

    public void setCommentId(String commentId) {
        this.commentid = commentid;
    }
}
