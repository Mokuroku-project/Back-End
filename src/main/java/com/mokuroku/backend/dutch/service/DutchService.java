package com.mokuroku.backend.dutch.service;

import com.mokuroku.backend.dutch.dto.DutchDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DutchService {

    public Map<String, Integer> calculate(DutchDTO.DutchRequest request) {
        Map<String, Integer> dutchResult = new HashMap<>();


        for (DutchDTO.DutchItem item : request.getDutchList()) {
            int totalPrice = 0;

            if (item.getTotalPrice() != null) {
                totalPrice = item.getTotalPrice();
            } else if (item.getPrice() != null && item.getQuantity() != null) {
                totalPrice = item.getPrice() * item.getQuantity();
                item.setTotalPrice(totalPrice);
            } else {
                continue;
            }

            List<String> eaters = item.getEater();
            if (eaters == null || eaters.isEmpty()) continue;

            int dutchPrice = totalPrice / eaters.size();

            for (String person : eaters) {
                dutchResult.put(person, dutchResult.getOrDefault(person, 0) + dutchPrice);
            }
        }

        return dutchResult;
    }


}
