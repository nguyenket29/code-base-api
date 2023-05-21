package com.java.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageDataResponse<E> {
    private String totalElements;
    private List<E> data;

    public static <E> PageDataResponse<E> of(String totalElements, List<E> data) {
        PageDataResponse<E> rs = new PageDataResponse<>();
        rs.setTotalElements(totalElements);
        rs.setData(data);
        return rs;
    }

    public static <E> PageDataResponse<E> of(Page<E> page) {
        PageDataResponse<E> rs = new PageDataResponse<>();
        rs.setTotalElements(String.valueOf(page.getTotalElements()));
        rs.setData(page.getContent());
        return rs;
    }

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }
}
