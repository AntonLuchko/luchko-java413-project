package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.entity.Users;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/panel")
public String userPanel(){
        return "/office/office";
}

    @GetMapping("/reviewPanel")
    public String rewiewPanel(){
        return "/office/reviews";
    }

    @GetMapping("/notificationPanel")
    public String notificaationPanel(){
        return "/office/notifications";
    }

    @GetMapping("/ticketPanel")
    public String ticketPanel(){
        return "/office/ticket-panel";
    }

    @GetMapping("/myTicketPanel")
    public String myTicketPanel(){
        return "/office/my-ticket";
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


     @ResponseBody
    @GetMapping("/myNotific")
public ResponseEntity<List<NotificDTO>> myNotific(@AuthenticationPrincipal UserDetails userDetails){
         List<NotificDTO> list =userService.notifications(userDetails);
         return list.size()==0?ResponseEntity.noContent().build():ResponseEntity.ok(list);
}

    @ResponseBody
    @GetMapping("/countMyNotific")
    public ResponseEntity<Integer> countMyNotific(@AuthenticationPrincipal UserDetails userDetails){
        int count=userService.countNotifications(userDetails);
        return count==0?ResponseEntity.noContent().build():ResponseEntity.ok(count);
    }

    @PostMapping("/notification/read")
    public ResponseEntity<?> noActive(@RequestParam long id){
        int count=userService.noActive(id);
        if(count==0){return ResponseEntity.noContent().build();}
        return ResponseEntity.ok(count);
    }

    @PostMapping("/notification/delete")
    public ResponseEntity<?> delNotific(@RequestParam long id){
        int count=userService.delNotific(id);
        if(count==0){return ResponseEntity.noContent().build();}
        return ResponseEntity.ok(count);
    }


    @GetMapping("/activeNotificationsCount")
    public ResponseEntity<Integer> activeNotificationsCount(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.activeNotificationsCount(userDetails));
    }

    @ResponseBody
    @PostMapping("/saveTicket")
    public ResponseEntity<Integer> saveTicket(@RequestBody TicketDT0  ticketDT0,@AuthenticationPrincipal UserDetails userDetails){
        int count=userService.saveTicket(ticketDT0,userDetails);
        if(count==0){return ResponseEntity.noContent().build();}
        return ResponseEntity.ok(count);
    }

    @ResponseBody
    @GetMapping("/myTicket")
    public ResponseEntity<List<TicketDT0>> savseTicket(@AuthenticationPrincipal UserDetails userDetails){
       List<TicketDT0> list=userService.myTickets(userDetails);
        if(list.isEmpty()){return ResponseEntity.noContent().build();}
        return ResponseEntity.ok(list);
    }
}
