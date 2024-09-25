package com.allenlyl.cryptoscheduler.service;

import com.allenlyl.cryptoscheduler.exception.InvalidDataException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ValidationService {

   //TODO CHECK IN THE RESTAPI -> BINANCE API
   public ValidationService isValidSymbol(@NotBlank String symbol) {
      if (symbol == null) {
         throw new InvalidDataException("Invalid symbol");
      }
      return this;
   }

   public ValidationService isValidTime(@NotNull @Min(0) Long startTime, Long endTime) {
      if (!(startTime == null && endTime == null) && !(startTime != null && endTime != null && startTime < endTime)) {
         throw new InvalidDataException("Invalid time");
      }
      return this;
   }
}
