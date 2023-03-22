package com.example.binarybandits.models;


/**
 * Model class representing a Comment on a QR code
 */
public class Comment {


    private String content;
    private String author;

    /**
     * Constructor for Comment object
     * @param content Contains content of comment
     * @param author Username of Player who wrote the comment
     */
    public Comment(String content, String author) {
        this.content = content;
        this.author = author;
    }

    /**
     * Get the content of a Comment
     * @return Return the content of a Comment
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of a Comment
     * @param content Contains content of comment
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the author of a Comment
     * @return Return the author of a comment
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author of a Comment
     * @param author Username of Player who wrote the comment
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
