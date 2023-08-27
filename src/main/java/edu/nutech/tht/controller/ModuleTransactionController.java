package edu.nutech.tht.controller;

import edu.nutech.tht.dto.UserTopUpDto;
import edu.nutech.tht.dto.UserTransactionDto;
import edu.nutech.tht.response.UserBalanceResponse;
import edu.nutech.tht.response.UserTransactionResponse;
import edu.nutech.tht.service.ModuleTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("")
public class ModuleTransactionController {

    @Autowired(required = false)
    private ModuleTransactionService mtService;

    @GetMapping(value = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserBalance() {
        try {
            UserBalanceResponse result = mtService.getUserBalance();

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

    @PostMapping("/topup")
    public ResponseEntity<?> userTopUp(@RequestBody UserTopUpDto topUpAmount) {
        try {
            UserBalanceResponse result = mtService.userTopUp(topUpAmount);

            if (result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else if (result.getStatus() == 102){
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("error: ", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transaction")
    @ResponseBody
    public ResponseEntity<?> userTransaction(@RequestBody UserTransactionDto dto) {
        try {
            UserTransactionResponse result = mtService.userTransaction(dto);

            if (result.getStatus() == 0) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else if (result.getStatus() == 102){
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("error: ", e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/transaction/history/{offset}/{limit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getUserTransactionHistory(@PathVariable Integer offset, @PathVariable Integer limit) {
        try {
            UserTransactionResponse result = mtService.getUserTransactionHistory(offset, limit);

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
