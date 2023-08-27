package edu.nutech.tht.service;

import edu.nutech.tht.dto.UserTopUpDto;
import edu.nutech.tht.dto.UserTransactionDto;
import edu.nutech.tht.enums.TransactionTypeEnum;
import edu.nutech.tht.model.User;
import edu.nutech.tht.model.UserBalance;
import edu.nutech.tht.model.UserTransaction;
import edu.nutech.tht.repo.ServiceRepo;
import edu.nutech.tht.repo.UserBalanceRepo;
import edu.nutech.tht.repo.UserTransactionRepo;
import edu.nutech.tht.response.UserBalanceResponse;
import edu.nutech.tht.response.UserTransactionFilteredResponse;
import edu.nutech.tht.response.UserTransactionResponse;
import edu.nutech.tht.util.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ModuleTransactionService {

    @Autowired(required = false)
    private UserTransactionRepo userTransactionRepo;

    @Autowired(required = false)
    private UserBalanceRepo userBalanceRepo;

    @Autowired(required = false)
    private ServiceRepo serviceRepo;

    /*Begin Check User Balance*/
    public UserBalanceResponse getUserBalance() throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserBalanceResponse ubr = null;
        List<UserBalance> userBalances;

        Map<String, Long> dataMap;
        String email;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

            if (user != null && StringUtils.isNotBlank(user.getEmail())){
                dataMap = new HashMap<>();
                email = user.getEmail();
                userBalances = getUserBalance(email);

                if (!userBalances.isEmpty()){
                    UserBalance ub = userBalances.get(0);
                    long currentBalance = ub.getBalance();
                    dataMap.put("balance", currentBalance);

                    ubr = new UserBalanceResponse(0, "Get Balance Berhasil", dataMap);
                }
            }
        }else {
            ubr = new UserBalanceResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return ubr;

    }

    public List<UserBalance> getUserBalance(String email){
        List<UserBalance> userBalances = new ArrayList<>();
        if (StringUtils.isNotBlank(email)){
            userBalances = userBalanceRepo.findByUsers_Email(email);
        }
        return userBalances;
    }
    /*End*/

    /*Begin User TopUp Balance*/
    public UserBalanceResponse userTopUp(UserTopUpDto dto) throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserBalanceResponse ubr = null;
        List<UserBalance> userBalances;

        Map<String, Long> dataMap;
        String email;

        if (dto.getTopUpAmount() > 0 && StringUtils.isNumeric(String.valueOf(dto.getTopUpAmount()))){
            if (StringUtils.isNotBlank(token)){
                String payload = ModelUtil.decodeJwtToStringObject(token);
                User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

                if (user != null && StringUtils.isNotBlank(user.getEmail())){
                    dataMap = new HashMap<>();
                    email = user.getEmail();
                    userBalances = getUserBalance(email);

                    long totalBalance;
                    long totalAmount = dto.getTopUpAmount();

                    if (!userBalances.isEmpty()){
                        UserBalance ub = userBalances.get(0);
                        long oldBalance = ub.getBalance();
                        totalBalance = oldBalance + totalAmount;

                        ub.setUsers(user);
                        ub.setBalance(totalBalance);

                        saveUserBalance(ub);
                    }else {
                        UserBalance ub = new UserBalance();
                        totalBalance = totalAmount;

                        ub.setUsers(user);
                        ub.setBalance(totalBalance);

                        saveUserBalance(ub);
                    }

                    /*Save transaction*/
                    UserTransaction ut = new UserTransaction();
                    ut.setUsers(user);
                    ut.setTotalAmount(totalAmount);
                    ut.setTransactionType(TransactionTypeEnum.TOPUP.label);

                    userTransactionRepo.save(ut);
                    /**/

                    dataMap.put("balance", totalBalance);
                    ubr = new UserBalanceResponse(0, "Top Up Balance berhasil", dataMap);
                }
            }else {
                ubr = new UserBalanceResponse(108, "Token tidak valid atau kadaluwarsa", null);
            }
        }else {
            ubr = new UserBalanceResponse(102, "Parameter amount hanya boleh angka dan tidak boleh lebih kecil dari 0", null);
        }

        return ubr;
    }

    public UserBalance saveUserBalance(UserBalance userBalance){
        userBalance = userBalanceRepo.save(userBalance);
        return userBalance;
    }
    /*End*/

    /*Begin User Transaction*/
    public UserTransactionResponse userTransaction(UserTransactionDto dto) throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserTransactionResponse utr = null;
        List<UserBalance> userBalances;
        List<edu.nutech.tht.model.Service> services;

        Map<String, Object> dataMap;
        String email;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

            if (user != null && StringUtils.isNotBlank(user.getEmail())){
                dataMap = new LinkedHashMap<>();
                email = user.getEmail();
                userBalances = getUserBalance(email);

                if (!userBalances.isEmpty()){
                    UserBalance ub = userBalances.get(0);
                    long userCurrentBalance = ub.getBalance();
                    services = this.getServices(dto.getServiceCode());

                    if (!services.isEmpty()){
                        edu.nutech.tht.model.Service service = services.get(0);
                        if (userCurrentBalance >= service.getServiceTariff()){
                            UserTransaction ut = new UserTransaction();
                            ut.setUsers(user);
                            ut.setServiceCode(service.getServiceCode());
                            ut.setServiceName(service.getServiceName());
                            ut.setTotalAmount(service.getServiceTariff());
                            ut.setTransactionType(TransactionTypeEnum.PAYMENT.label);

                            UserTransaction successUt = userTransactionRepo.save(ut);

                            /*Update total balance of current user*/
                            userCurrentBalance -= service.getServiceTariff();
                            ub.setUsers(user);
                            ub.setBalance(userCurrentBalance);
                            saveUserBalance(ub);
                            /**/

                            dataMap.put("invoice_number", successUt.getInvoiceNumber());
                            dataMap.put("service_code", successUt.getServiceCode());
                            dataMap.put("service_name", successUt.getServiceName());
                            dataMap.put("transaction_type", successUt.getTransactionType());
                            dataMap.put("total_amount", successUt.getTotalAmount());
                            dataMap.put("created_on", successUt.getCreatedOn());
                            utr = new UserTransactionResponse(0, "Transaksi berhasil", dataMap);
                        }else {
                            utr = new UserTransactionResponse(102, "Jumlah saldo tidak cukup", null);
                        }
                    }else{
                        utr = new UserTransactionResponse(102, "Service atau Layanan tidak ditemukan", null);
                    }
                }
            }
        }else {
            utr = new UserTransactionResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return utr;
    }

    public UserTransactionResponse getUserTransactionHistory(int offset, int limit) throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        UserTransactionResponse utr = null;
        List<UserTransaction> userTransactions;

        Map<String, Object> dataMap;
        String email;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

            if (user != null && StringUtils.isNotBlank(user.getEmail())){
                email = user.getEmail();
                userTransactions = getUserTransaction(email);

                if (!userTransactions.isEmpty()){
                    dataMap = new LinkedHashMap<>();
                    userTransactions = userTransactions.stream()
                            .skip(offset)
                            .limit(limit)
                            .collect(Collectors.toList());

                    List<UserTransactionFilteredResponse> utfr = getUserTransactionFilteredResponses(userTransactions);

                    dataMap.put("offset", offset);
                    dataMap.put("limit", limit);
                    dataMap.put("records", utfr);

                    utr = new UserTransactionResponse(0, "Get History Berhasil", dataMap);
                }
            }
        }else {
            utr = new UserTransactionResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return utr;
    }

    private List<UserTransactionFilteredResponse> getUserTransactionFilteredResponses(List<UserTransaction> userTransactions) {
        List<UserTransactionFilteredResponse> filtered = new ArrayList<>();
        for (UserTransaction ut : userTransactions){
            UserTransactionFilteredResponse filteredResponse =
                    new UserTransactionFilteredResponse(
                            ut.getInvoiceNumber(), ut.getTransactionType(), ut.getServiceName(), ut.getTotalAmount(), ut.getCreatedOn()
                            );
            filtered.add(filteredResponse);
        }
        return filtered;
    }

    public List<UserTransaction> getUserTransaction(String email){
        List<UserTransaction> userTransactions = new ArrayList<>();
        if (StringUtils.isNotBlank(email)){
            userTransactions = userTransactionRepo.findByUsers_Email(email);
        }
        return userTransactions;
    }

    public List<edu.nutech.tht.model.Service> getServices(String serviceCode){
        List<edu.nutech.tht.model.Service> services = serviceRepo.findByServiceCode(serviceCode);

        return services;
    }
    /*End*/

}
