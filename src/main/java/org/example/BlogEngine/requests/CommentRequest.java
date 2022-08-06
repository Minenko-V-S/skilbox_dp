package org.example.BlogEngine.requests;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CommentRequest implements Serializable {
    Integer parent_id;
    Integer post_id;
    String text;

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer postId) {
        this.post_id = postId;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parentId) {
        this.parent_id = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
