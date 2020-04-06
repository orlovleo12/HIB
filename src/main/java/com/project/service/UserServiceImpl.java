package com.project.service;

import com.project.dao.UserDAO;
import com.project.model.UserDTO;
import com.project.model.UserDTONewPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDTO getUserDTOByLogin(String login) {
        return userDAO.getUserByLogin(login);
    }

    @Override
    public String saveUserDTOPersonalInformation(UserDTO userDTO) {
        String reg = "^(.+)@([a-zA-Z]+)\\.([a-zA-Z]+)$";
        if (!checkEmailFromOtherUsers(userDTO.getEmail(), userDTO.getUserId())) {
            return "error";
        } else {
            if (Pattern.matches(reg, userDTO.getEmail())) {
                userDAO.saveUserDTOPersonalInformation(userDTO);
                return "ok";
            } else {
                return "synError";
            }
        }
    }

    @Override
    public boolean checkEmailFromOtherUsers(String email, long id) {
        return userDAO.checkEmailFromOtherUsers(email, id);
    }

    @Override
    public String saveUserDTOPassword(UserDTONewPassword userDTONewPassword) {
        String reg = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
        if (Pattern.matches(reg, userDTONewPassword.getNewPassword()) && (userDTONewPassword.getNewPassword().length() >= 8 && userDTONewPassword.getNewPassword().length() <= 64)) {
            if (encoder.matches(userDTONewPassword.getOldPassword(), userDAO.getOldPassword(userDTONewPassword.getUserId()))) {
                String newPass = encoder.encode(userDTONewPassword.getNewPassword());
                userDTONewPassword.setNewPassword(newPass);
                userDAO.saveUserDTOPassword(userDTONewPassword);
                return "passOk";
            } else {
                return "wrongPassword";
            }
        } else {
            return "passError";
        }
    }
}
