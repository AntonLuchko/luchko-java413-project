package com.example.demo.utils;

import com.example.demo.dto.UsersDTO;
import com.example.demo.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class ConverterDTO {

    public UsersDTO convert(Users user) {
        return new UsersDTO(user);
    }

}
