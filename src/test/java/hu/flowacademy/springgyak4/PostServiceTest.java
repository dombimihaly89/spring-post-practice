package hu.flowacademy.springgyak4;

import hu.flowacademy.springgyak4.persistence.entity.Post;
import hu.flowacademy.springgyak4.repository.PostRepository;
import hu.flowacademy.springgyak4.service.PostService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        UUID id = UUID.randomUUID();
        Post post = getAPost(id);
        when(postRepository.findById(id)).thenReturn(post);
        Assert.assertEquals(post, postService.findById(id));
    }

    // miért nem működik?
    @Test
    public void testFindAll() {
        Post post = getAPost(UUID.randomUUID());
        post.setDeletedAt(LocalDateTime.now());
        when(postRepository.findAll()).thenReturn(List.of(post));
        Assert.assertEquals(0, postService.findAll().size());
        System.out.println(postService.findAll());
    }

    @Test
    public void createPost() {
        Post post = getAPost(null);
        when(postRepository.savePost(post)).thenReturn(getAPost(UUID.randomUUID()));
        Post createdPost = postService.createPost(post);
        Assert.assertNotNull(createdPost);
        Assert.assertNotNull(createdPost.getId());
        Assert.assertNotNull(createdPost.getContent());
        Assert.assertNotNull(createdPost.getTitle());
    }


    public Post getAPost (UUID id) {
        return new Post(id, "American trip", "Some instructions about american trip", null, null, null, null, 0);
    }

}
