package com.filepan.service;

import com.filepan.constant.Constant;
import com.filepan.entity.Priv;
import com.filepan.entity.User;
import com.filepan.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    //根据用户id去查询权限
    public List<Priv> selctPriv(int id){
        List<Priv> privs = userMapper.selectPriv(id);
        return privs;
    }

    public User login(String user_name, String user_pwd) {
        user_pwd = hashPassword(user_pwd);
        User user = userMapper.login(user_name, user_pwd);
        return user;
    }

    public boolean register(String user_name, String user_pwd) {
        user_pwd = hashPassword(user_pwd);
//        System.out.println("user_pwd:"+user_pwd);
        int res = userMapper.insert(user_name, user_pwd, Constant.USER);
        return res > 0;
    }

    // 使用 SHA-256 哈希加密密码
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法不可用", e);
        }
    }
}
