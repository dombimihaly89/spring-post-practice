package hu.flowacademy.springgyak4.service;

import hu.flowacademy.springgyak4.exceptions.ValidationException;
import hu.flowacademy.springgyak4.persistence.entity.Post;
import hu.flowacademy.springgyak4.persistence.entity.Status;
import hu.flowacademy.springgyak4.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(UUID id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        if (post.getId() != null) {
            throw new ValidationException("ID is not null");
        }
        postValidation(post);
        post.setCreatedAt(LocalDateTime.now());
        post.setStatus(Status.IN_PROGRESS);
        post.setVersion(0);
        return postRepository.savePost(post);
    }

    public Post updatePost(Post post) {
        if (post.getId() == null) {
            throw new ValidationException("ID is null");
        }
        Post postToUpdate = postRepository.findById(post.getId());
        post.setCreatedAt(postToUpdate.getCreatedAt());
        post.setStatus(postToUpdate.getStatus());
        postValidation(post);
        statusValidation(postToUpdate);
        post.setUpdatedAt(LocalDateTime.now());
        int version = versionCheck(post, postToUpdate);
        post.setVersion(version);

        return postRepository.updatePost(post);
    }

    public Post patchPost(UUID id) {
        Post post = postRepository.findById(id);
        postValidation(post);
        statusValidation(post);
        post.setStatus(Status.PUBLISHED);
        return post;
    }

    public Post deletePost(UUID id) {
        Post post = postRepository.findById(id);
        post.setDeletedAt(LocalDateTime.now());
        return postRepository.deletePost(post);
    }

    public Post archivePost(UUID id) {
        Post post = postRepository.findById(id);
        publishedValidation(post);
        deletedValidation(post);
        post.setStatus(Status.ARCHIVED);
        return post;
    }

    public void publishedValidation(Post post) {
        if (post.getStatus() != Status.PUBLISHED) {
            throw new ValidationException("The post is not published!");
        }
    }

    public void deletedValidation(Post post) {
        if (post.getDeletedAt() != null) {
            throw new ValidationException("It's deleted.");
        }
    }

    public void postValidation(Post post) {
        // majd csekkoljuk már, hogy ""-re és null-ra is jó e, vagy csak ""-re.
        if (StringUtils.isEmpty(post.getTitle())) {
            throw new ValidationException("Title is empty");
        }
        if (StringUtils.isEmpty(post.getContent())) {
            throw new ValidationException("Content is empty");
        }
    }

    public void statusValidation(Post post) {
        if (post.getStatus() != Status.IN_PROGRESS) {
            throw new ValidationException("The post is " + post.getStatus().toString().toLowerCase() + ". It has to be in progress.");
        }
    }

    public int versionCheck(Post postToUpdate, Post newPost) {
        if (postToUpdate.getVersion() >= newPost.getVersion()) {
            int version = postToUpdate.getVersion();
            return ++version;
        } else {
            return newPost.getVersion();
        }
    }


}
