package com.shivamdenge.Module4.controller;

import com.shivamdenge.Module4.dto.PostDTO;
import com.shivamdenge.Module4.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    ResponseEntity<PostDTO> createNewPost(@RequestBody PostDTO postDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createNewPost(postDTO));
    }

    @GetMapping("/getAllPost")
    ResponseEntity<List<PostDTO>> getAllPost(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable(name = "postId") Long id){
        return postService.getPostById(id);
    }
}
