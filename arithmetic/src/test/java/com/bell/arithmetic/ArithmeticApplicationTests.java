package com.bell.arithmetic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.bell.arithmetic.partner.AESUtilForShardingsphere;
import com.bell.arithmetic.partner.dto.read.PDate;
import com.bell.arithmetic.partner.dto.read.PImages;
import com.bell.arithmetic.partner.dto.read.PNameScore;
import com.bell.arithmetic.partner.dto.read.Sales;
import com.bell.arithmetic.partner.dto.write.PSalesAndImage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class ArithmeticApplicationTests {

    private static final String PATH = "/Users/belltinkle/work/供应商统计/final/";
    private static final String PATH1 = "/Users/belltinkle/work/sql/xm/";
    private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");

    @Test
    void contextLoads() {
        //id
        String pIds = PATH + "3.xlsx";
        //销量
        String pSalesFileName = PATH + "psales.xlsx";
        //
        String scoreAndName = PATH + "1.xlsx";
        //
        String pImagesFileName = PATH + "2.xlsx";
        String pCDate = PATH + "date1.xlsx";
        String pImageAndSaleFileName = PATH + "pImageAndSale.xlsx";


        List<Sales> pSales = new ArrayList<>();
        List<Sales> idLists = new ArrayList<>();
        List<PImages> pImages = new ArrayList<>();
        List<PSalesAndImage> pSalesAndImages = new ArrayList<>();
        List<PNameScore> nameAndScores = new ArrayList<>();
        List<PDate> dates = new ArrayList<>();


        EasyExcel.read(pCDate, PDate.class, new PageReadListener<PDate>(dataList -> {
            for (PDate p : dataList) {
                dates.add(p);
            }
        })).sheet().doRead();
        Map<String, PDate> pDateMap = dates.stream().collect(Collectors.toMap(PDate::getPartnerId, Function.identity()));


        EasyExcel.read(pSalesFileName, Sales.class, new PageReadListener<Sales>(dataList -> {
            for (Sales sale : dataList) {
                pSales.add(sale);
            }
        })).sheet().doRead();
        Map<String, Sales> salesMap = pSales.stream().collect(Collectors.toMap(Sales::getPartnerId, Function.identity()));


        EasyExcel.read(scoreAndName, PNameScore.class, new PageReadListener<PNameScore>(dataList -> {
            for (PNameScore p : dataList) {
                nameAndScores.add(p);
            }
        })).sheet().doRead();
        Map<String, PNameScore> pNameScoreMap = nameAndScores.stream().collect(Collectors.toMap(PNameScore::getPartnerId, Function.identity()));

        EasyExcel.read(pImagesFileName, PImages.class, new PageReadListener<PImages>(dataList -> {
            for (PImages p : dataList) {
                pImages.add(p);
            }
        })).sheet().doRead();
        Map<String, PImages> pImagesMap = pImages.stream().collect(Collectors.toMap(PImages::getPartnerId, Function.identity()));

        EasyExcel.read(pIds, Sales.class, new PageReadListener<Sales>(dataList -> {
            for (Sales s : dataList) {
                String partnerId = s.getPartnerId();
                PSalesAndImage pSalesAndImage = new PSalesAndImage();
                pSalesAndImage.setPartnerId(partnerId);
                BigDecimal sales = salesMap.get(partnerId).getSales();
                BigDecimal saleBigDecimal = sales.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_HALF_DOWN);
                pSalesAndImage.setSale(saleBigDecimal);
                if (saleBigDecimal.compareTo(new BigDecimal(10)) > -1) {
                    pSalesAndImage.setSaleFlag(1);
                } else {
                    pSalesAndImage.setSaleFlag(0);
                }
                PImages pImages1 = pImagesMap.get(partnerId);
                String imgUrl = pImages1.getImg();
                String filename = imgUrl.substring(imgUrl.lastIndexOf("/"));
                pSalesAndImage.setLocalImg("/partner" + filename);

                PNameScore pNameScore = pNameScoreMap.get(partnerId);
                if(pNameScore != null){
                    BigDecimal score = new BigDecimal(Double.parseDouble(StringUtils.hasText(pNameScore.getScore()) ? pNameScore.getScore() : "0"))
                            .divide(new BigDecimal(5), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));

                    pSalesAndImage.setScore(score.toString());
                }else{
                    pSalesAndImage.setScore("0");
                }
                pSalesAndImage.setMtUrl("https://m.maitao.com/partner?partnerid=" + partnerId);
                PDate pDate = pDateMap.get(partnerId);
                if(pDate != null){
                    pSalesAndImage.setDate(sdf.format(pDate.getDate()));
                    pSalesAndImage.setPartnerName(pDate.getName());
                }else{
                    pSalesAndImage.setDate(null);
                    pSalesAndImage.setPartnerName(null);
                }
                pSalesAndImages.add(pSalesAndImage);
            }
        })).sheet().doRead();

//        EasyExcel.read(pImagesFileName, PImages.class, new PageReadListener<PImages>(dataList -> {
//            for (PImages pImage : dataList) {
//                if(fIds.contains(pImage.getPartnerID())){
//                    continue;
//                }
//                PSalesAndImage pSalesAndImage = new PSalesAndImage();
//                pSalesAndImage.setPartnerId(pImage.getPartnerID());
//                pSalesAndImage.setImg(pImage.getImg());
//                if (salesMap.get(pImage.getPartnerID()) != null) {
//                    BigDecimal sales = salesMap.get(pImage.getPartnerID()).getSales();
//                    BigDecimal saleBigDecimal = sales.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_HALF_DOWN);
//                    pSalesAndImage.setSale(saleBigDecimal);
//                    if (saleBigDecimal.compareTo(new BigDecimal(10)) > -1) {
//                        pSalesAndImage.setSaleFlag(1);
//                    } else {
//                        pSalesAndImage.setSaleFlag(0);
//                    }
//                }
//                pSalesAndImage.setLocalImg(download(pImage.getImg()));
//                pSalesAndImage.setPartnerName(pImage.getPartnerName());
//                pSalesAndImages.add(pSalesAndImage);
//            }
//        })).sheet().doRead();

        EasyExcel.write(pImageAndSaleFileName, PSalesAndImage.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return pSalesAndImages;
                });
    }

    @Test
    void test1() {
        String download = download("https://img.maitao.com/488d6ea5-ed0b-4c06-8dda-faabc8d532f1.png");
        log.info(download);
    }

    public String download(String imgUrl) {

        if (!StringUtils.hasText(imgUrl)) {
            return "";
        }
        String filename = "";
        try {
            filename = imgUrl.substring(imgUrl.lastIndexOf("/"));
            // TODO: 2022/1/14  图片保存位置
            String savePath = "/Users/belltinkle/work/供应商统计/test";
            URL url = new URL(imgUrl);
            URLConnection con = url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
            con.setConnectTimeout(10 * 1000);
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            long length = (sf.length()) / 128;
            OutputStream os = new FileOutputStream(sf.getPath() + "/" + filename);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "./test" + filename;
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {

        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    @Test
    void contextLoads2() {
        String pCDate = PATH1 + "1.xlsx";
        String pImageAndSaleFileName = PATH1 + "2.xlsx";
        List<PDate> dates = new ArrayList<>();
        List<PDate> dates2 = new ArrayList<>();



        EasyExcel.read(pCDate, PDate.class, new PageReadListener<PDate>(dataList -> {
            for (PDate p : dataList) {
                dates.add(p);
            }
        })).sheet().doRead();

        for (PDate date : dates) {
            String partnerId = date.getPartnerId();
            String decrypt = AESUtilForShardingsphere.decrypt(partnerId);
            date.setName(decrypt);
            dates2.add(date);
        }
        EasyExcel.write(pImageAndSaleFileName, PSalesAndImage.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return dates2;
                });



    }
}

