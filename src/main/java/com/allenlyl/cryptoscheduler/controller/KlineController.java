package com.allenlyl.cryptoscheduler.controller;

import com.allenlyl.cryptoscheduler.annotation.LogExecutionTime;
import com.allenlyl.cryptoscheduler.repository.Kline;
import com.allenlyl.cryptoscheduler.service.KlineService;
import com.allenlyl.cryptoscheduler.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kline")
public class KlineController {
   @Autowired
   private KlineService klineService;

   @Autowired
   private ValidationService validationService;

   @PostMapping("/loadData")
   @LogExecutionTime
   public void loadData(
           @RequestParam String symbol,
           @RequestParam Long startTime,
           @RequestParam Long endTime) {
      validationService.isValidSymbol(symbol)
              .isValidTime(startTime, endTime);
      klineService.loadData(symbol, startTime, endTime);
   }

   @GetMapping("/getData")
   @LogExecutionTime
   @ResponseBody
   public List<Kline> getData(
           @RequestParam String symbol,
           @RequestParam int interval,
           @RequestParam Long startTime,
           @RequestParam Long endTime
   ) {
      validationService.isValidSymbol(symbol)
              .isValidTime(startTime, endTime);
      return klineService.getData(symbol, interval, startTime, endTime);
   }

   @GetMapping("/getAll")
   @LogExecutionTime
   public List<Kline> getAll() {
      return klineService.getAll();
   }
   // BTCUSDT
   // ETHUSDT
   // BTCETH = 3
   // ETHBTC != 1/3 ARBTRAGE
   @PostMapping("/deleteAll")
   @LogExecutionTime
   public String deleteAll() {
      klineService.deleteAll();
      return "successful";
   }
}
