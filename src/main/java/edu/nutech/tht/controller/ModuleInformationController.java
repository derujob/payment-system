package edu.nutech.tht.controller;

import edu.nutech.tht.response.BannerResponse;
import edu.nutech.tht.response.ServiceResponse;
import edu.nutech.tht.service.ModuleInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("")
public class ModuleInformationController {

    @Autowired(required = false)
    private ModuleInformationService moduleInformationService;

    @GetMapping(value = "/banner", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBanner() {
        try {
            BannerResponse result = moduleInformationService.getBannerResponse();

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

    @GetMapping(value = "/product-services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServices() {
        try {
            ServiceResponse result = moduleInformationService.getServiceResponse();

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
}
