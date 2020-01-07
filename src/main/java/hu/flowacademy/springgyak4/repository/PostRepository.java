package hu.flowacademy.springgyak4.repository;

import hu.flowacademy.springgyak4.persistence.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

    private Map<UUID, Post> mapOfPosts = new HashMap<>();

    public List<Post> findAll() {
        return new ArrayList<>(mapOfPosts.values().stream().filter(p -> p.getDeletedAt() == null).collect(Collectors.toList()));
    }

    public Post findById(UUID id) {
        Iterator<Map.Entry<UUID, Post>> iterator = mapOfPosts.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<UUID, Post> entry = iterator.next();
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Post savePost(Post post) {
        UUID id = UUID.randomUUID();
        post.setId(id);
        mapOfPosts.put(post.getId(), post);
        return post;
    }

    public Post updatePost(Post post) {
        Iterator<Map.Entry<UUID, Post>> iterator = mapOfPosts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Post> entry = iterator.next();
            if (post.getId().equals(entry.getKey())) {
                entry.setValue(post);
                return post;
            }
        }
        return null;
    }

    public Post deletePost(Post post) {
        mapOfPosts.put(post.getId(), post);
        return post;
    }
}
