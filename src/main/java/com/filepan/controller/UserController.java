package com.filepan.controller;

import com.filepan.entity.User;
import com.filepan.service.JwtService;
import com.filepan.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        User tmp = userService.login(user.getUser_name(), user.getUser_pwd());
        if(tmp != null){
            String token = jwtService.generateToken(String.valueOf(tmp.getId()));
            Map<String, String> response = new HashMap<>();
            response.put("message", "登录成功");
            response.put("token", token);
            System.out.println("token: " + token);
            return ResponseEntity.ok().body(response);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }

    // 注册接口，通过 URL 参数获取用户名和密码
    @PostMapping("/register")
    public String registerUser(@RequestParam("user_name") String userName, @RequestParam("user_pwd") String userPwd) {
        boolean success = userService.register(userName, userPwd);
        return success ? "注册成功" : "注册失败";
    }

    @GetMapping("/auth")
    public ResponseEntity<?> checkAuth() {
        return ResponseEntity.ok("授权成功，访问允许");
    }
}
