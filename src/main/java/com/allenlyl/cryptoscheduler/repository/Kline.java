package com.allenlyl.cryptoscheduler.repository;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Kline {
   private @NotNull @Min(0) Long openTime;
   private @NotNull @Min(0) Double openPrice;
   private @NotNull @Min(0) Double highPrice;
   private @NotNull @Min(0) Double lowPrice;
   private @NotNull @Min(0) Double closePrice;
   private @NotNull @Min(0) Double volume;
   private @NotNull @Min(0) Long closeTime;
   private @NotNull @Min(0) Integer tradeNumber;
   private @NotNull String symbol;

   public Kline(String[] klineData, String symbol) {
      this.openTime = Long.parseLong(klineData[0]);
      this.closeTime = Long.parseLong(klineData[6]);
      this.symbol = symbol;
      this.openPrice = Double.parseDouble(klineData[1]);
      this.highPrice = Double.parseDouble(klineData[2]);
      this.lowPrice = Double.parseDouble(klineData[3]);
      this.closePrice = Double.parseDouble(klineData[4]);
      this.volume = Double.parseDouble(klineData[5]);
      this.tradeNumber= Integer.parseInt(klineData[8]);
   }
}
