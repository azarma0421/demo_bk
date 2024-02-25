package com.example.demo.scheduling;

import com.example.demo.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalTime;

@Component
public class DailyApiCaller {

    @Autowired
    private ApiService apiService;

    @PostConstruct
    public void init() {
        // 在應用程式啟動時觸發一次
        System.out.println("排成啟動....");
//        callApi();
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void callApi() {
        apiService.callApi();

        System.out.println("API called at: " + LocalTime.now());
    }
}

