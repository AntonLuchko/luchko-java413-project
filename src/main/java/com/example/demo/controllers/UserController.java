package com.example.demo.controllers;

import com.example.demo.dto.GetReview;
import com.example.demo.dto.TextReview;
import com.example.demo.entity.Users;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/panel")
public String userPanel(){
        return "/office/office";
}

@ResponseBody
    @PostMapping("/saveReview")
    public ResponseEntity<Integer> saveReview(@RequestBody TextReview textReview, @AuthenticationPrincipal  UserDetails userDetails){
int ok=userService.saveReview(textReview,userDetails);
if(ok==1){return ResponseEntity.ok().body(ok);}
return ResponseEntity.badRequest().build();
    }

 @ResponseBody
    @GetMapping("/myReview")
public ResponseEntity<List<GetReview>> myReviews( @AuthenticationPrincipal UserDetails userDetails,@RequestParam Integer page){
    List<GetReview> list =userService.myReviews(userDetails,page);
   return list.size()==0?ResponseEntity.noContent().build():ResponseEntity.ok().body(list);
}

    @ResponseBody
    @GetMapping("/allReview")
    public ResponseEntity<List<GetReview>> allReviews(@RequestParam Integer page){
        List<GetReview> list =userService.allReviews(page);
        return list.size()==0?ResponseEntity.noContent().build():ResponseEntity.ok().body(list);
    }

    @ResponseBody
    @GetMapping("/countMyReview")
    public ResponseEntity<Integer> countMyReviews(@AuthenticationPrincipal UserDetails userDetails){
       return ResponseEntity.ok(userService.countMyReview(userDetails));
    }

    @ResponseBody
    @GetMapping("/countAllReview")
    public ResponseEntity<Integer> countAllReviews(){
        return ResponseEntity.ok(userService.countAllReview());
    }

@PostMapping("lockUser")
    public ResponseEntity<?> lockUser(@RequestParam String email){
        int count= userService.lockedUser(email);
     if(count==1){
        return ResponseEntity.ok().build();
     }
     else return ResponseEntity.notFound().build();
    }
}
