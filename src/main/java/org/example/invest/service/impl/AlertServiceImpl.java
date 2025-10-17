package org.example.invest.service.impl;

import org.example.invest.client.TelegramClient;
import org.example.invest.dto.EtfResponse;
import org.example.invest.dto.IndicesResponse;
import org.example.invest.dto.ProcessDto;
import org.example.invest.dto.bse.etf.BseEtfData;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.dto.nse.etf.EtfData;
import org.example.invest.dto.nse.etf.NseEtfResponse;
import org.example.invest.dto.nse.index.NseAllIndicesResponse;
import org.example.invest.dto.nse.index.NseIndexData;
import org.example.invest.dto.request.RequestBean;
import org.example.invest.mapper.ProcessDtoMapper;
import org.example.invest.service.AlertService;
import org.example.invest.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlertServiceImpl implements AlertService {
    @Autowired
    private ProcessDtoMapper processDtoMapper;

    @Autowired
    private GeneralUtil generalUtil;

    @Autowired
    private TelegramClient telegramClient;

    @Value("${shubh.etf.bot.token:}")
    private String shubhEtfBotToken;

    @Value("${jd.shubh.etf.bot.chat.id:}")
    private Long jdShubhEtfBotChatId;

    private static Set<String> dailyNseEtfSymbols;
    private static Set<String> dailyBseEtfScripCodes;

    private static Set<String> dailyNseIndices;
    private static Set<String> dailyBseIndices;

    @Scheduled(cron = "0 8 * * * *")
    @PostConstruct
    public void refreshDaily() {
        telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId
                , new StringBuilder("ETF alerts for DATE: ").append(new Date()).toString());
        dailyNseEtfSymbols = new HashSet<>();
        dailyBseEtfScripCodes = new HashSet<>();
        dailyNseIndices = new HashSet<>();
        dailyBseIndices = new HashSet<>();
    }

    @Override
    public EtfResponse sendAlerts() {
        ProcessDto processDto = processDtoMapper.getProcessDto(new RequestBean());
        EtfResponse etfResponse = new EtfResponse(processDto.getNseDto().getInvestableEtf()
                , processDto.getBseDto().getInvestableEtf()
                , getAiOpinion(processDto.getNseDto().getInvestableEtf(), processDto.getBseDto().getInvestableEtf()));
        alertEtf(etfResponse);

        IndicesResponse indicesResponse = new IndicesResponse(processDto.getNseDto().getInvestableIndices()
                , processDto.getBseDto().getInvestableIndices()
                , getAiOpinion(processDto.getNseDto().getInvestableIndices(), processDto.getBseDto().getInvestableIndices()));
        alertIndices(indicesResponse);
        return etfResponse;
    }

    private Object getAiOpinion(NseAllIndicesResponse nseIndicesResponse, List<BseRealTimeData> bseIndices) {
        return null;
    }

    private Object getAiOpinion(NseEtfResponse nseEtf, BseEtfResponse bseEtf) {
        return null;
    }

    @Override
    public void alertEtf(EtfResponse etfResponse) {
        if (etfResponse == null) {
            return;
        }
        alertNseEtfs(etfResponse.getNse());
        alertBseEtfs(etfResponse.getBse());
    }

    private void alertNseEtfs(NseEtfResponse nse) {
        if (nse == null || CollectionUtils.isEmpty(nse.getData())) {
            return;
        }
        List<EtfData> nseEtfs = nse.getData();
        for (EtfData nseEtf : nseEtfs) {
            if (!dailyNseEtfSymbols.contains(nseEtf.getSymbol())) {
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId
                        , new StringBuilder("Alert for ETF-Symbol(NSE): ").append(nseEtf.getSymbol()).toString());

                String aiPrompt = generalUtil.generateAiPrompt(nseEtf);
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, aiPrompt);
                //telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, ObjectUtils.toString(nse));*/
                dailyNseEtfSymbols.add(nseEtf.getSymbol());
            }
        }
    }

    private void alertBseEtfs(BseEtfResponse bse) {
        if (bse == null || CollectionUtils.isEmpty(bse.getData())) {
            return;
        }
        List<BseEtfData> bseEtfs = bse.getData();
        for (BseEtfData bseEtf : bseEtfs) {
            if (!dailyBseEtfScripCodes.contains(bseEtf.getScripCode())) {
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId
                        , new StringBuilder("Alert for ETF-Symbol(BSE): ").append(bseEtf.getScripCode()).toString());
                String aiPrompt = generalUtil.generateAiPrompt(bseEtf);
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, aiPrompt);
                //telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, ObjectUtils.toString(bse));*/
                dailyBseEtfScripCodes.add(bseEtf.getScripCode());
            }
        }
    }


    @Override
    public void alertIndices(IndicesResponse indicesResponse) {
        if (indicesResponse == null) {
            return;
        }
        alertNseIndices(indicesResponse.getNse());
        alertBseIndices(indicesResponse.getBse());
    }

    private void alertNseIndices(NseAllIndicesResponse nse) {
        if (nse == null || CollectionUtils.isEmpty(nse.getData())) {
            return;
        }
        for (NseIndexData nseIndex : nse.getData()) {
            if (!dailyNseIndices.contains(nseIndex.getIndex())) {
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId
                        , new StringBuilder("Alert for Index(NSE): ").append(nseIndex.getIndex()).toString());
                String aiPrompt = generalUtil.generateAiPrompt(nseIndex);
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, aiPrompt);
                //telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, ObjectUtils.toString(bseIndex));*/
                dailyNseIndices.add(nseIndex.getIndex());
            }
        }
    }

    private void alertBseIndices(List<BseRealTimeData> bse) {
        if (CollectionUtils.isEmpty(bse)) {
            return;
        }
        for (BseRealTimeData bseIndex : bse) {
            if (!dailyBseIndices.contains(bseIndex.getIndexName())) {
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId
                        , new StringBuilder("Alert for Index(BSE): ").append(bseIndex.getIndexName()).toString());
                String aiPrompt = generalUtil.generateAiPrompt(bseIndex);
                telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, aiPrompt);
                //telegramClient.sendMsg(shubhEtfBotToken, jdShubhEtfBotChatId, ObjectUtils.toString(bseIndex));*/
                dailyBseIndices.add(bseIndex.getIndexName());
            }
        }
    }
}