package com.bell.arithmetic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.bell.arithmetic.partner.dto.read.PImages;
import com.bell.arithmetic.partner.dto.read.Sales;
import com.bell.arithmetic.partner.dto.write.PSalesAndImage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class ArithmeticApplicationTests {

    private static final String PATH = "/Users/belltinkle/work/供应商统计/";

    @Test
    void contextLoads() {

        String pSalesFileName = PATH + "psales.xlsx";
        String pImagesFileName = PATH + "供应商简.xlsx";
        String pImageAndSaleFileName = PATH + "pImageAndSale.xlsx";


        List<Sales> pSales = new ArrayList<>();
        List<PImages> pImages = new ArrayList<>();
        List<PSalesAndImage> pSalesAndImages = new ArrayList<>();

        EasyExcel.read(pSalesFileName, Sales.class, new PageReadListener<Sales>(dataList -> {
            for (Sales sale : dataList) {
                pSales.add(sale);
            }
        })).sheet().doRead();
        Map<String, Sales> salesMap = pSales.stream().collect(Collectors.toMap(Sales::getPartnerId, Function.identity()));


        EasyExcel.read(pImagesFileName, PImages.class, new PageReadListener<PImages>(dataList -> {
            for (PImages pImage : dataList) {
                PSalesAndImage pSalesAndImage = new PSalesAndImage();
                pSalesAndImage.setPartnerId(pImage.getPartnerID());
                pSalesAndImage.setImg(pImage.getImg());
                if (salesMap.get(pImage.getPartnerID()) != null) {
                    BigDecimal sales = salesMap.get(pImage.getPartnerID()).getSales();
                    BigDecimal saleBigDecimal = sales.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_HALF_DOWN);
                    pSalesAndImage.setSale(saleBigDecimal);
                    if (saleBigDecimal.compareTo(new BigDecimal(10)) > -1) {
                        pSalesAndImage.setSaleFlag(1);
                    } else {
                        pSalesAndImage.setSaleFlag(0);
                    }
                }
                pSalesAndImage.setLocalImg(download(pImage.getImg()));
                pSalesAndImage.setPartnerName(pImage.getPartnerName());
                pSalesAndImages.add(pSalesAndImage);
            }
        })).sheet().doRead();


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
}

