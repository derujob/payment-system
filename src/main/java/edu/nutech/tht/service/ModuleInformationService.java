package edu.nutech.tht.service;

import edu.nutech.tht.model.Banner;
import edu.nutech.tht.model.User;
import edu.nutech.tht.repo.BannerRepo;
import edu.nutech.tht.repo.ServiceRepo;
import edu.nutech.tht.response.BannerResponse;
import edu.nutech.tht.response.ServiceResponse;
import edu.nutech.tht.util.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ModuleInformationService {

    @Autowired(required = false)
    private BannerRepo bannerRepo;

    @Autowired(required = false)
    private ServiceRepo serviceRepo;

    public BannerResponse getBannerResponse() throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        BannerResponse data = null;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

            if (user != null){
                List<Banner> banners = getBanners();
                data = new BannerResponse(0, "Sukses", banners);
            }
        }else{
            data = new BannerResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return data;
    }

    public ServiceResponse getServiceResponse() throws IOException {
        String token = ModelUtil.GLOBAL_TOKENS;
        ServiceResponse data = null;

        if (StringUtils.isNotBlank(token)){
            String payload = ModelUtil.decodeJwtToStringObject(token);
            User user = ModelUtil.getMapperJsonStringToObject(payload, User.class);

            if (user != null){
                List<edu.nutech.tht.model.Service> serviceList = getServices();
                data = new ServiceResponse(0, "Sukses", serviceList);
            }
        }else{
            data = new ServiceResponse(108, "Token tidak valid atau kadaluwarsa", null);
        }

        return data;
    }

    public List<Banner> getBanners(){
        List<Banner> banners = bannerRepo.findAll();
        return banners;
    }

    public List<edu.nutech.tht.model.Service> getServices(){
        List<edu.nutech.tht.model.Service> services = serviceRepo.findAll();
        return services;
    }
}
