package org.example.invest.controller;

import org.example.invest.dto.EtfResponse;
import org.example.invest.dto.IndicesResponse;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.mapper.EtfResponseMapper;
import org.example.invest.mapper.IndicesResponseMapper;
import org.example.invest.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/all")
@CrossOrigin(origins = "*")
public class SingleController {

    @Autowired
    private EtfResponseMapper etfResponseMapper;

    @Autowired
    private IndicesResponseMapper indicesResponseMapper;

    @Autowired
    private AlertService alertService;

    @GetMapping("/investableEtfs")
    public EtfResponse getInvestableEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return etfResponseMapper.getInvestableEtfs(new RequestBean(cookie));
    }

    @GetMapping("/daily/notify/investable")
    public EtfResponse dailyNotifyInvestableEtfs(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return scheduleInvestableNotification();
    }

    @Scheduled(cron = "0 19,30,45 9 * * 1-5")//every 15 minutes from Monday to Friday between 9:19 AM and 9:45 AM = 3 times a day
    @Scheduled(cron = "0 */18 10-14 * * 1-5")//every 18 minutes from Monday to Friday between 10 AM and 2:42 PM = 17 times a day
    @Scheduled(cron = "0 0-30/20 15 * * 1-5")//every 20 minutes from Monday to Friday between 3 PM and 3:30 PM = 2 times a day
    public EtfResponse scheduleInvestableNotification() {
        return alertService.sendAlerts();
    }

    @GetMapping("/investableIndices")
    public IndicesResponse getInvestableIndices(@RequestHeader(value = "Cookie", required = false) String cookie) {
        return indicesResponseMapper.getInvestableIndices(new RequestBean(cookie));
    }
}