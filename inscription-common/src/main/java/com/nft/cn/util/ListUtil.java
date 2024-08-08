package com.nft.cn.util;

import com.nft.cn.vo.resp.PageRespVO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil<T> {

    public static <T> PageRespVO<T> pageList(List<T> list, Integer pageSize, Integer pageNum) {
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<T>();
        }
        long pages = (list.size() + pageSize - 1) / pageSize;
        PageRespVO<T> respPageRespVO = new PageRespVO<T>((long) list.size(), pageNum.longValue(), pageSize.longValue(), pages);
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex < list.size()) {
            respPageRespVO.setList(list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size())));
        } else {
            respPageRespVO.setList(new ArrayList<T>());
        }
        return respPageRespVO;
    }


}
