package hu.flowacademy.springgyak4.controller;

import hu.flowacademy.springgyak4.persistence.entity.Post;
import hu.flowacademy.springgyak4.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping
    List<Post> findAll() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    Post findById(@PathVariable UUID id) {
        return postService.findById(id);
    }

    @PostMapping
    Post addPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping
    Post updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    Post deletePost(@PathVariable UUID id) {
        return postService.deletePost(id);
    }

    @PatchMapping
    Post patchPost(@RequestParam UUID id) {
        return postService.patchPost(id);
    }

}
