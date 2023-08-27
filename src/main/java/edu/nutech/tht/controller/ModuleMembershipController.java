package edu.nutech.tht.controller;

import edu.nutech.tht.dto.UserLoginDto;
import edu.nutech.tht.dto.UserRegisDto;
import edu.nutech.tht.dto.UserUpdateProfileDto;
import edu.nutech.tht.response.UserLoginResponse;
import edu.nutech.tht.response.UserProfileResponse;
import edu.nutech.tht.response.UserRegisResponse;
import edu.nutech.tht.service.ModuleMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("")
public class ModuleMembershipController {

    @Autowired(required = false)
    private ModuleMembershipService moduleMembershipService;

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody UserRegisDto user) {
        try {
            UserRegisResponse result = moduleMembershipService.getUserRegisResponse(user);

            if (result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("error: ", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> userLogin(@RequestBody UserLoginDto dto) {
        try {
            UserLoginResponse result = moduleMembershipService.getUserLogin(dto);

            if (result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else if (result.getStatus() == 102){
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("error: ", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userProfile() {
        try {
            UserProfileResponse result = moduleMembershipService.getUserProfile();

            if (result != null && result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            log.error("error: ", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/profile/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateProfileDto dto) {
        try {
            UserProfileResponse result = moduleMembershipService.updateUserProfile(dto.getFirstName(), dto.getLastName());

            if (result != null && result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            UserProfileResponse result = moduleMembershipService.updateUserImage(file);

            if (result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else if (result.getStatus() == 102){
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
