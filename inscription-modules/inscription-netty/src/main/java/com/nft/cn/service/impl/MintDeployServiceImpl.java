package com.nft.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.cn.dao.MintDeployMapper;
import com.nft.cn.entity.MintDeploy;
import com.nft.cn.service.MintDeployService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MintDeployServiceImpl extends ServiceImpl<MintDeployMapper, MintDeploy> implements MintDeployService {

    @Override
    public MintDeploy getMintDeploy(String accord) {
        return lambdaQuery().eq(MintDeploy::getAccord, accord).one();
    }


    public static void main(String[] args) {
        String number = "1234";
        String formattedNumber = formatNumberWithCommas(number);
        System.out.println(formattedNumber);
    }

    public static String formatNumberWithCommas(String number) {
        String reversedNumber = new StringBuilder(number).reverse().toString();
        String s = String.join(",",
                splitStringIntoChunks(reversedNumber, 3)).replaceAll(",$", "");
        return new StringBuilder(s).reverse().toString();
    }

    public static List<String> splitStringIntoChunks(String str, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < str.length(); i += chunkSize) {
            chunks.add(str.substring(i, Math.min(str.length(), i + chunkSize)));
        }
        return chunks;
    }

}
