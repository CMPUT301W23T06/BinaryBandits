package com.example.binarybandits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.binarybandits.models.Comment;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Comment model class
 */
public class CommentTest {

    /**
     * Create a mock Comment object for testing purposes
     * @return Return a mock Comment object to perform unit tests on
     */
    public Comment mockComment() {
        return new Comment("That QR code is sick!", "Josh123");
    }

    /**
     * Test getters and setters for contents
     */
    @Test
    public void testContents() {
        Comment mockComment = mockComment();
        assertEquals("That QR code is sick!", mockComment.getContent());

        mockComment.setContent("I also have that one!");
        assertEquals("I also have that one!", mockComment.getContent());
    }

    /**
     * Test getters and setters for author
     */
    @Test
    public void testAuthor() {
        Comment mockComment = mockComment();
        assertEquals("Josh123", mockComment.getAuthor());

        mockComment.setAuthor("PieceOfPi");
        assertEquals("PieceOfPi", mockComment.getAuthor());
    }
}