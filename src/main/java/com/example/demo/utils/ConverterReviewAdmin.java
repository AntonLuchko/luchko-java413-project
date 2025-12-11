package com.example.demo.utils;

import com.example.demo.dto.ReviewAdminDto;
import com.example.demo.entity.Reviews;
import org.springframework.stereotype.Component;

@Component
public class ConverterReviewAdmin {
public ReviewAdminDto convert(Reviews reviews) {
    return new ReviewAdminDto(reviews);
};
}
