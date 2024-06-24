package com.example.project.controller;

import com.example.project.dto.PostDto;
import com.example.project.entity.Post;
import com.example.project.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

 //@GetMapping("/board"): /board 경로로 들어오는 HTTP GET 요청을 처리하도록 지정합니다.
//따라서, 사용자가 웹 브라우저에서 /board 경로를 요청하거나 HTML에서 적절한 방법을 사용해 /board 경로로 요청을 보낼 수 있습니다.
@Controller
@RequiredArgsConstructor
public class BoardController {
    private final PostService postService;

    @GetMapping("/board")
    public String board(Model model, Pageable pageable) {
        Page<Post> posts = postService.getAllPosts(pageable); //Post: 애플리케이션에서 사용하는 게시글(Entity) 객체입니다.
        model.addAttribute("posts", posts);
        return "board";           //*Model**은 컨트롤러가 뷰로 데이터를 전달할 때 사용됩니다.
    }

    @GetMapping("/board/create")
    public String createPostPage(Model model, HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);

        if (userId == null || userId.isEmpty()) {
            return "redirect:/user/login";
        }

        model.addAttribute("postDto", new PostDto());
        return "create";
    }

    @PostMapping("/board/create")
    public String createPost(@ModelAttribute PostDto postDto, HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);//String userId = getUserIdFromRequest(request); 문장에서 getUserIdFromRequest(request)
        // 메서드는 현재 로그인된 사용자의 ID를 가져오기 위한 메서드로 보입니다.



        if (userId == null || userId.isEmpty()) {
            return "redirect:/user/login";
        }
        postDto.setUserId(userId);  //userId는 로그인한 사용자의 ID를 나타내는 변수
        postService.save(postDto); //저장하는게 아니라 service save메서드를 호출하는것이다.
        // service에서 이미 디비에 저장할걸 호출
        return "redirect:/board";
        //postDto.setUserId(userId);: 이 부분에서 postDto 객체에 로그인한 사용자의 ID를 설정합니다.
        //postService.save(postDto);: 설정된 postDto 객체를 서비스 계층으로 전달하여 데이터베이스에 저장합니다.

    }

    @GetMapping("/board/{postId}")
    public String viewPost(@PathVariable Long postId, Model model, HttpServletRequest request) {
        Post post = postService.getPostById(postId); // postId에 해당하는 게시물을 데이터베이스에서 조회합니다.
        String currentUserId = getUserIdFromRequest(request);// 현재 사용자의 ID를 가져옵니다.
        model.addAttribute("post", post); // "post"라는 이름으로 조회된 게시물을 모델에 추가합니다post는 Post 클래스의 객체입니다.
        // 이 Post 객체는 보통 데이터베이스에서 조회한 게시물 정보를 담고 있습니다.
        model.addAttribute("currentUserId", currentUserId); // "currentUserId"라는 이름으로 현재 사용자의 ID를 모델에 추가합니다.
        return "viewpost";
    }

    @GetMapping("/board/edit/{postId}")
    public String editPostPage(@PathVariable Long postId, Model model, HttpServletRequest request) {
        String currentUserId = getUserIdFromRequest(request);
        //getPostById(postId): postService의 메서드로, 주어진 postId에 해당하는 게시물을 조회
        Post post = postService.getPostById(postId);

        if (currentUserId == null || !post.getUser().getUserId().equals(currentUserId)) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/board";
        }

        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        model.addAttribute("postDto", postDto);
        model.addAttribute("postId", postId);
        return "edit";
    }

    @PostMapping("/board/edit/{postId}")
    public String editPost(@PathVariable Long postId, @ModelAttribute("postDto") PostDto postDto, HttpServletRequest request, Model model) {
        String currentUserId = getUserIdFromRequest(request); //HttpServletRequest: 현재 요청을 나타내며, 현재 로그인된 사용자 ID를 가져오는 데 사용
        Post existingPost = postService.getPostById(postId);

        if (currentUserId == null || !existingPost.getUser().getUserId().equals(currentUserId)) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/board";
        }

        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());

        postService.updatePost(existingPost);

        return "redirect:/board/" + postId;
    }

    @PostMapping("/board/delete/{postId}")
    public String deletePost(@PathVariable Long postId, HttpServletRequest request, Model model) {
        String currentUserId = getUserIdFromRequest(request);
        Post post = postService.getPostById(postId);

        if (currentUserId == null || !post.getUser().getUserId().equals(currentUserId)) {
            model.addAttribute("error", "삭제 권한이 없습니다.");
            return "redirect:/board/" + postId;
        }

        postService.deletePostById(postId);
        return "redirect:/board";
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("userId");
    }
}
