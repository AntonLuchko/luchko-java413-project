package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;


    public AdminController(AdminService adminService) {
       this.adminService = adminService;
    }

    @GetMapping("/panel")
    public String adminPanel(){
        return "/admin/admin-panel";
    }

    @ResponseBody
    @GetMapping("find")
    public ResponseEntity<List<UsersDTO>> find(@RequestParam int page, @RequestParam String param, @RequestParam String value){
        List<UsersDTO> list = adminService.find(page,param,value);
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @ResponseBody
    @GetMapping("countFind")
    public ResponseEntity<Integer> countFind(@RequestParam String param, @RequestParam String value){
        int count = adminService.countFind(param,value);
        if(count==0)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(count);
    }

    @ResponseBody
    @GetMapping("all")
    public ResponseEntity<List<UsersDTO>> allUsers(@RequestParam int page){
        List<UsersDTO> list = adminService.allUsers(page);
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @ResponseBody
    @GetMapping("allBlocked")
    public ResponseEntity<List<UsersDTO>> allBlockedUsers(@RequestParam int page){
        List<UsersDTO> list = adminService.allBlockedUsers(page);
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @ResponseBody
    @GetMapping("allUnBlocked")
    public ResponseEntity<List<UsersDTO>> allUnBlockedUsers(@RequestParam int page){
        List<UsersDTO> list = adminService.allUnBlockedUsers(page);
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }


    @ResponseBody
    @GetMapping("allLocked")
    public ResponseEntity<List<UsersDTO>> allLockedUsers(@RequestParam int page){
        List<UsersDTO> list = adminService.allLockedUsers(page);
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @ResponseBody
    @GetMapping("countAll")
    public ResponseEntity<Integer> countAll(){
       return ResponseEntity.ok(adminService.countAll());
    }

    @ResponseBody
    @GetMapping("countAllBlocked")
    public ResponseEntity<Integer> countAllBlocked(){
        return ResponseEntity.ok(adminService.countAllBlocked());
    }

    @ResponseBody
    @GetMapping("countAllUnBlocked")
    public ResponseEntity<Integer> countAllUnBlocked(){
        return ResponseEntity.ok(adminService.countAllUnBlocked());
    }

    @ResponseBody
    @GetMapping("countAllLocked")
    public ResponseEntity<Integer> countAllLocked(){
        return ResponseEntity.ok(adminService.countAllLocked());
    }

    @ResponseBody
    @PostMapping("blockUser")
    public ResponseEntity<?> blockUser(@RequestBody List<EmailBlocked> list){
        int count = adminService.blockUser(list);
        if(count == 0){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @ResponseBody
    @PostMapping("unBlockUser")
    public ResponseEntity<?> unBlockUser(@RequestBody List<EmailUnBlocked> list){
        int count = adminService.unBlockUser(list);
        if(count == 0){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

        @ResponseBody
        @PostMapping("unBlockLockedUser")
        public ResponseEntity<?> unblockLockedUser(@RequestBody List<EmailUnBlocked> list){
            int count = adminService.unblockLockedUser(list);
            if(count == 0){
                return ResponseEntity.status(404).build();
            } else {
                return ResponseEntity.ok().build();
            }
        }

        @ResponseBody
        @GetMapping("/filterReviews")
    public ResponseEntity<List<ReviewAdminDto>> filterReviews(@RequestParam int page){
List<ReviewAdminDto> list = adminService.filterReviews(page);
if(list.isEmpty()){return ResponseEntity.noContent().build();}
return ResponseEntity.ok(list);
        }

    @ResponseBody
    @GetMapping("/countReviewsByFilter")
    public ResponseEntity<Integer> countReviewsByFilter(){
        int count = adminService.countReviewsByFilter();
        if(count==0){return ResponseEntity.noContent().build();}
        return ResponseEntity.ok(count);
    }

    @ResponseBody
    @PostMapping("/goodReviews")
    public ResponseEntity<Integer> goodReview(@RequestBody List<Long> listId,@RequestParam String type){
        int count = adminService.approveReviews(listId,type);
        if(count==0){return ResponseEntity.badRequest().build();}
        return ResponseEntity.ok().build();
    }



}
