package com.allenlyl.cryptoscheduler.service;

import com.allenlyl.cryptoscheduler.model.KlineMapper;
import com.allenlyl.cryptoscheduler.repository.Kline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

@Service
public class KlineService {
   @Autowired
   private KlineMapper klineMapper;

   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private ValidationService validationService;

   @Value("${api-url-template}")
   private String apiUrl;

   @Value("${limit}")
   private int limit;

   // The query interval is 1m
   public void loadData(String symbol, Long startTime, Long endTime) {
      long duration = 500 * 60 * 1000;

      LongStream.range(startTime, endTime + 1)
              .filter(time -> (time - startTime) % duration == 0)
              .parallel()
              .forEach(
                   start -> {
                      long end = (start + duration) < endTime ? start + duration : endTime;
                      String url = String.format(apiUrl, symbol, start, end);
                      ResponseEntity<String[][]> response = restTemplate.getForEntity(url, String[][].class);
                      String[][] data = response.getBody();
                      // todo try catch error for data
                      List<Kline> klineList = convertData(data, symbol);
                      klineMapper.insertBatch(klineList);
                   }
              );
   }

   private List<Kline> convertData(String[][] data, String symbol) {
      return Arrays.stream(data)
              .parallel()
              .map(d -> new Kline(d, symbol))
              .toList();
   }

   public List<Kline> getData(String symbol, int interval, Long startTime, Long endTime) {
      List<Kline> klineList = klineMapper.getDataByRange(symbol, startTime, endTime);
      combined(klineList, interval);
      return klineList;
   }

   private void combined(List<Kline> klineList, int interval){
      int start = 0;
      while (start < klineList.size()) {
         double volume = 0;
         double highPrice = Double.MIN_VALUE;
         double lowPrice = Double.MAX_VALUE;
         int tradeNumber = 0;
         Kline kline = new Kline();
         int end = Math.min(start + interval, klineList.size());

         kline.setOpenPrice(klineList.get(start).getOpenPrice());
         kline.setCloseTime(klineList.get(end).getCloseTime());
         kline.setOpenPrice(klineList.get(start).getOpenPrice());
         kline.setClosePrice(klineList.get(end).getClosePrice());

         for(int i = start; i <= end; i++){
            Kline tempKline = klineList.get(i);
            volume += tempKline.getVolume();
            highPrice = Math.max(highPrice, tempKline.getHighPrice());
            lowPrice = Math.min(lowPrice, tempKline.getLowPrice());
            tradeNumber += tempKline.getTradeNumber();
         }

         kline.setVolume(volume);
         kline.setHighPrice(highPrice);
         kline.setLowPrice(lowPrice);
         kline.setTradeNumber(tradeNumber);
         klineList.set(start, kline);

         for(int i = start + 1; i <= end; i++) {
            klineList.remove(start + 1);
         }
         start += 1;
      }
   }

   // TODO delete it
   public List<Kline> getAll() {
      return klineMapper.getAll();
   }

   // TODO delete it
   public void deleteAll() {
      klineMapper.truncateAll();
   }
}
