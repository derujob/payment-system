package edu.nutech.tht.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

@Slf4j
public class ModelUtil {

    public static String GLOBAL_TOKENS = "";

    public static String getMapperObjectToJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static <T> T getMapperJsonStringToObject(String content, Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, valueType);
    }

    public static <T> T convertMapperJsonStringToObject(String content, Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(content, valueType);
    }

    public static String decodeJwtToStringObject(String token){
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = null;
        if (StringUtils.isNotBlank(token)){
            String[] chunks = token.split("\\.");
            payload = new String(decoder.decode(chunks[1]));
        }

        return payload;
    }

    public static boolean isEmailValid(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        boolean pattern = Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();

        return pattern;
    }

    public static boolean isNumber(String number){
        String regexPattern = "^\\d{10}$";

        boolean pattern = Pattern.compile(regexPattern)
                .matcher(number)
                .matches();

        return pattern;
    }
}
