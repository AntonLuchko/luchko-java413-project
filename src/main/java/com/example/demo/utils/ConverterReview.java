package com.example.demo.utils;

import com.example.demo.dto.GetReview;
import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.Reviews;
import com.example.demo.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class ConverterReview {
    public GetReview convert(Reviews reviews) {
        return new GetReview(reviews);
    }
}
