package edu.nutech.tht.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nutech.tht.dto.UserLoginDto;
import edu.nutech.tht.dto.UserRegisDto;
import edu.nutech.tht.model.Data;
import edu.nutech.tht.model.User;
import edu.nutech.tht.model.UserProfile;
import edu.nutech.tht.repo.UserProfileRepo;
import edu.nutech.tht.repo.UserRepo;
import edu.nutech.tht.response.UserLoginResponse;
import edu.nutech.tht.response.UserProfileResponse;
import edu.nutech.tht.response.UserRegisResponse;
import edu.nutech.tht.util.ModelUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ModuleMembershipService {

    @Autowired(required = false)
    private UserRepo userRepo;

    @Autowired(required = false)
    private UserProfileRepo userProfileRepo;

    public UserRegisResponse getUserRegisResponse(UserRegisDto dto){
        boolean validEmail = ModelUtil.isEmailValid(dto.getEmail());
        UserRegisResponse urr;

        if (dto.getPassword().length() >= 8 && validEmail){
            User user = saveUser(dto);
            if (user != null){
                urr = new UserRegisResponse(0, "Registrasi berhasil silahkan login", null);
            }else{
                urr = new UserRegisResponse(102, "Parameter email tidak sesuai format", null);
            }
        }else {
            urr = new UserRegisResponse(102, "Parameter email tidak sesuai format", null);
        }

        return urr;
    }

    public User saveUser(UserRegisDto dto){
        User user = new User(dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getPassword());
        if (user != null){
            List<User> users = userRepo.findUserByEmail(user.getEmail());
            if (users.isEmpty()){
                userRepo.save(user);
                userProfileRepo.save(new UserProfile(user.getEmail(), null));
            }else {
                user = null;
            }
        }
        return user;
    }

    /*Begin User Login*/
    public UserLoginResponse getUserLogin(UserLoginDto dto){
        boolean validEmail = ModelUtil.isEmailValid(dto.getEmail());

        String token;
        UserLoginResponse ulr = null;

        if (dto != null && dto.getPassword().length() >= 8 && validEmail){
            List<User> users = getUser(dto.getEmail(), dto.getPassword());

            if (!users.isEmpty()){
                token = encodeToJwtBearerToken(users);
                if (StringUtils.isNotBlank(token)){
                    Data data = new Data(token);
                    ulr = new UserLoginResponse(0, "Login Sukses", data);
                    ModelUtil.GLOBAL_TOKENS = token;
                }
            }else {
                ulr = new UserLoginResponse(103, "Email atau password salah", null);
            }
        }else {
            ulr = new UserLoginResponse(102, "Parameter email atau password tidak sesuai format", null);
        }

        return ulr;
    }

    public String encodeToJwtBearerToken(List<User> users){
        Map<String, Object> claims; //create a hashmap
        ObjectMapper mapper;

        String token = null;

        if (!users.isEmpty()){
            for (User user : users){
                mapper = new ObjectMapper();
                claims = new HashMap<>();

                Map<String, Object> userMap = mapper.convertValue(user, Map.class);
                claims.putAll(userMap);

                //create the token
                token = Jwts.builder()
                        .setHeaderParam("typ","JWT")
                        .setClaims(claims)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000) * 12))
                        .signWith(SignatureAlgorithm.HS256, "secret")
                        .compact();
            }
        }

        return token;
    }

    public List<User> getUser(String email, String password){
        List<User> users = null;
        if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(password)){
            users = userRepo.findUserByEmailAndPassword(email, password);
        }

        return users;
    }
    /*End*/

    /*Begin user profile*/
    public UserProfileResponse getUserProfile() throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserProfileResponse data = null;
        Map<String, String> dataMap;
        List<UserProfile> userProfiles;
        UserProfile userProfile;
        String email;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);
            
            if (user != null && StringUtils.isNotBlank(user.getEmail())){
                dataMap = new LinkedHashMap<>();
                email = user.getEmail();
                userProfiles = getUserProfile(email);
                String userPictureLink = null;

                if (!userProfiles.isEmpty()){
                    userProfile = userProfiles.get(0);
                    userPictureLink = userProfile.getPictureLink();
                }

                dataMap.put("email", user.getEmail());
                dataMap.put("first_name", user.getFirstName());
                dataMap.put("last_name", user.getLastName());
                dataMap.put("profile_image", userPictureLink);

                data = new UserProfileResponse(0, "Sukses", dataMap);
            }
        }else{
            data = new UserProfileResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return data;
    }

    public List<UserProfile> getUserProfile(String email){
        List<UserProfile> userProfiles = null;
        if (StringUtils.isNotBlank(email)){
            userProfiles = userProfileRepo.findByEmail(email);
        }

        return userProfiles;
    }
    /*End*/

    /*Begin update user profile*/
    public UserProfileResponse updateUserProfile(String newFirstName, String newLastName) throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserProfileResponse data = null;
        Map<String, String> dataMap;
        List<UserProfile> userProfiles;
        UserProfile userProfile;
        String email;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

            if (user != null && StringUtils.isNotBlank(user.getEmail())){
                dataMap = new HashMap<>();
                email = user.getEmail();
                userProfiles = getUserProfile(email);
                String oldPassword = userRepo.findUserByEmail(email).get(0).getPassword();
                String userPictureLink = null;

                if (!userProfiles.isEmpty()){
                    userProfile = userProfiles.get(0);
                    userPictureLink = userProfile.getPictureLink();
                }

                if (StringUtils.isNotBlank(newFirstName) && StringUtils.isNotBlank(newLastName)){
                    user.setFirstName(newFirstName);
                    user.setLastName(newLastName);
                }

                dataMap.put("email", user.getEmail());
                dataMap.put("first_name", user.getFirstName());
                dataMap.put("last_name", user.getLastName());
                dataMap.put("profile_image", userPictureLink);
                user.setPassword(oldPassword);

                userRepo.save(user);
                data = new UserProfileResponse(0, "Update Profile berhasil", dataMap);
            }
        }else{
            data = new UserProfileResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return data;
    }

    public UserProfileResponse updateUserImage(MultipartFile file) throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserProfileResponse data = null;
        Map<String, String> dataMap;
        List<UserProfile> userProfiles;
        UserProfile userProfile;
        String email;

        if (file != null && file.getOriginalFilename().endsWith(".jpeg") || file.getOriginalFilename().endsWith(".png")){
            if (StringUtils.isNotBlank(token)){
                /*Validate token*/
                User user = validateTokenUser(token);
                if (user != null && StringUtils.isNotBlank(user.getEmail())){
                    dataMap = new LinkedHashMap<>();
                    email = user.getEmail();
                    userProfiles = getUserProfile(email);

                    if (!userProfiles.isEmpty()){
                        userProfile = userProfiles.get(0);
                        String userPictureLink = file.getOriginalFilename();
                        userProfile.setPictureLink(userPictureLink);

                        dataMap.put("email", user.getEmail());
                        dataMap.put("first_name", user.getFirstName());
                        dataMap.put("last_name", user.getLastName());
                        dataMap.put("profile_image", userPictureLink);

                        userProfileRepo.save(userProfile);
                        data = new UserProfileResponse(0, "Update Profile Image berhasil", dataMap);
                    }
                }
            }else{
                data = new UserProfileResponse(108, "Token tidak valid atau kadaluwarsa", null);
            }
        }else{
            data = new UserProfileResponse(102, "Format Image tidak sesuai", null);
        }

        return data;
    }

    public User validateTokenUser(String token) throws IOException {
        String payload = ModelUtil.decodeJwtToStringObject(token);
        return ModelUtil.getMapperJsonStringToObject(payload, User.class);
    }
    /*End*/
}
