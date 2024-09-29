package com.allenlyl.cryptoscheduler.model;

import com.allenlyl.cryptoscheduler.repository.Kline;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper
@Validated
public interface KlineMapper {
   @Select({
           "SELECT * FROM klinedata",
           "WHERE",
           "symbol = #{symbol} and open_time >= #{startTime} and close_time <= #{endTime}"
   })
   List<Kline> getDataByRange(@NotNull String symbol, @NotNull @Min(0) Long startTime, @NotNull @Min(0) Long endTime);

   @Insert({
           "INSERT INTO klinedata(open_time, open_price, high_price, low_price, close_price, volume, close_time, trade_number, symbol)" +
                   "values ",
           "<foreach  collection='klineList' item='kline' separator=','>",
           "(#{kline.openTime}, #{kline.openPrice}, #{kline.highPrice}, #{kline.lowPrice}, #{kline.closePrice}, #{kline.volume}, #{kline.closeTime}, #{kline.tradeNumber}, #{kline.symbol})",
           "</foreach>"
   })
   void insertBatch(@NotEmpty List<@Valid Kline> klineList);

   // TODO delete it
   @Select("SELECT * FROM klinedata")
   List<Kline> getAll();

   // TODO delete it
   @Update(("TRUNCATE TABLE klinedata"))
   void truncateAll();
}
