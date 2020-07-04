package kr.hnu.ock.messaging_app;

import java.io.Serializable;

public class Message implements Serializable {
    private String receiver;
    private String sender;
    private String title;
    private String content;
    private String date;
    private String reply_status;
    private String read_status;

    public Message() {
    }

    public Message(String receiver, String sender, String title, String content, String date, String reply_status, String read_status) {
        this.receiver = receiver;
        this.sender = sender;
        this.title = title;
        this.content = content;
        this.date = date;
        this.reply_status = reply_status;
        this.read_status = read_status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReply_status() {
        return reply_status;
    }

    public void setReply_status(String reply_status) {
        this.reply_status = reply_status;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }
}
