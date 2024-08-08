package com.nft.cn.vo.resp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PageRespVO<T> {

    private Long total;
    private Long current;

    private Long size;
    private Long pages;

    private List<T> list;

    public PageRespVO() {
    }

    public PageRespVO(Long total, Long current, Long size, Long pages){
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = pages;
    }

    public PageRespVO(IPage iPage){
        total = iPage.getTotal();
        current = iPage.getCurrent();
        size = iPage.getSize();
        pages = iPage.getPages();
    }

    public PageRespVO(IPage iPage, List<T> list){
        total = iPage.getTotal();
        current = iPage.getCurrent();
        size = iPage.getSize();
        pages = iPage.getPages();
        this.list = list;
    }

    public void pageInit(IPage iPage) {
        total = iPage.getTotal();
        current = iPage.getCurrent();
        size = iPage.getSize();
        pages = iPage.getPages();
    }

    public void pageInit(IPage iPage, List<T> list) {
        total = iPage.getTotal();
        current = iPage.getCurrent();
        size = iPage.getSize();
        pages = iPage.getPages();
        this.list = list;
    }
}
