package com.allenlyl.cryptoscheduler.model;

import com.allenlyl.cryptoscheduler.repository.Kline;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface KlineMapper {
   @Select({
           "<script>",
           "SELECT * FROM klinedata",
           "WHERE",
           "symbol = #{symbol} and open_time >= #{startTime} and close_time <= #{endTime}"
   })
   List<Kline> getDataByRange(String symbol, Long startTime, Long endTime);

   @Insert({
           "<script>",
           "INSERT INTO klinedata(open_time, open_price, high_price, low_price, close_price, volume, close_time, trade_number, symbol)" +
           "values ",
           "<foreach  collection='klineList' item='kline' separator=','>",
           "(#{kline.openTime}, #{kline.openPrice}, #{kline.highPrice}, #{kline.lowPrice}, #{kline.closePrice}, #{kline.volume}, #{kline.closeTime}, #{kline.tradeNumber}, #{kline.symbol})",
           "</foreach>",
           "</script>"
   })
   int insertBatch(@NotEmpty List<@Valid Kline> klineList);

   @Select("SELECT * FROM klinedata")
   List<Kline> getAll();

   @Select("SELECT * FROM klinedata where open_time >= #{startTime} and close_time <= #{endTime}")
   List<Kline> getData(String symbol, Long startTime, Long endTime);

   @Update(("TRUNCATE TABLE klinedata"))
   void truncateAll();
}
