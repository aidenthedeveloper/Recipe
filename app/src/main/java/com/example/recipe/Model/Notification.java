package com.example.recipe.Model;

public class Notification {

    private String userid;
    private String notifid;
    private String txtComment;
    private String postid;
    private String publisher;
    private boolean ispost;

    public Notification(String userid, String txtComment, String postid,String publisher, boolean ispost,String notifid){
        this.userid = userid;
        this.notifid = notifid;
        this.txtComment = txtComment;
        this.publisher = publisher;
        this.postid = postid;
        this.ispost = ispost;

    }

    public Notification(){

    }

    public String getNotifid() {
        return notifid;
    }

    public void setNotifid(String notifid) {
        this.notifid = notifid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTxtComment() {
        return txtComment;
    }

    public void setTxtComment(String txtComment) {
        this.txtComment = txtComment;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}

