package com.mokuroku.backend.dutch.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DutchDTO {

    @Getter
    @Setter
    public static class DutchItem {
        private String menu; //메뉴
        private Integer price; //가격
        private Integer quantity; //양
        private Integer totalPrice; //총금액
        private List<String> eater;
    }


    @Getter
    @Setter
    public static class DutchRequest {
        private List<String> participant;
        private List<DutchItem> dutchList;

    }
}