package com.bell.arithmetic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.bell.arithmetic.partner.dto.read.PImages;
import com.bell.arithmetic.partner.dto.read.PImages2;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class ArithmeticApplicationTests {

    private static final String PATH = "D:/gongy/";

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
            String savePath = "D:/gongy/partner";
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
        return "/partner/" + filename;
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
    void test2() {

        String pSalesFileName = PATH + "1.xls";
        String pSalesFileName1 = PATH + "2.xls";
        List<Sales> pSales = new ArrayList<>();
        EasyExcel.read(pSalesFileName, Sales.class, new PageReadListener<Sales>(dataList -> {
            for (Sales sale : dataList) {
                pSales.add(sale);
            }
        })).sheet().doRead();
        List<String> collect = pSales.stream().map(Sales::getPartnerId).collect(Collectors.toList());


        List<Sales> pSales1 = new ArrayList<>();
        for (String s : collect) {
            Sales sales = new Sales();
            if(s.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?")){

                BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(s)).setScale(0,BigDecimal.ROUND_HALF_DOWN);

                sales.setPartnerId(s);
                sales.setSales1("CNY" + bigDecimal.toString());

            }else {
//                if("啥".equals(s)){
//                    sales.setPartnerId("");
//                    sales.setSales1("");
//                }else{
                    sales.setPartnerId(s);
                    sales.setSales1(s);
//                }
            }
            pSales1.add(sales);
        }

        EasyExcel.write(pSalesFileName1, PSalesAndImage.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return pSales1;
                });



    }
    @Test
    void test3() {

        String readImg = PATH + "readImg.xls";
        String writeImg = PATH + "writeImg2.xls";
        List<PImages2>  images2List  = new ArrayList<>();
        List<PImages2>  writeList  = new ArrayList<>();

        EasyExcel.read(readImg, PImages2.class, new PageReadListener<PImages2>(dataList -> {
            for (PImages2 p : dataList) {
                images2List.add(p);
            }
        })).sheet().doRead();

        List<String> img1 = images2List.stream().map(PImages2::getImg1).collect(Collectors.toList());

        for (PImages2 pImages2 : images2List) {

            PImages2 p = new PImages2();
            p.setImg2(pImages2.getImg2());
            if(img1.contains(pImages2.getImg2())){
                p.setImg1("");
                p.setFlag("1");
            }else{
                p.setImg1("");
                p.setFlag("0");
            }
            writeList.add(p);
        }

        EasyExcel.write(writeImg, PSalesAndImage.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return writeList;
                });

    }


    @Test
    void test4() {
        String path = PATH + "partner1";
        String imgAndId = PATH + "imgAndId.xls";
        String writeImages = PATH + "writeImages.xls";
        List<PImages> imagesList = new ArrayList<>();
        List<PImages> writeImagesList = new ArrayList<>();


        EasyExcel.read(imgAndId, PImages.class, new PageReadListener<PImages>(dataList -> {
            for (PImages p : dataList) {
                imagesList.add(p);
            }
        })).sheet().doRead();
        Map<String, PImages> imagesMap = imagesList.stream().collect(Collectors.toMap(PImages::getPartnerName, Function.identity()));


        File folder = new File(path);

        File[] fileArr = folder.listFiles();

        for (File file : fileArr) {
            String name = file.getName();
            PImages pImages = imagesMap.get("/partner/" + name);
            String substring = name.substring(name.lastIndexOf(".") + 1);
            File newFile = new File(PATH + "partner1/" + pImages.getPartnerID() + "." + substring);
            PImages pImages1 = new PImages();
            pImages1.setPartnerID(pImages.getPartnerID());
            pImages1.setPartnerName("/partner/"+ pImages.getPartnerID() + "." + substring);
            writeImagesList.add(pImages1);
            file.renameTo(newFile);
        }

        EasyExcel.write(writeImages, PSalesAndImage.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return writeImagesList;
                });


    }

    @Test
    void test5() {


        String imgAndId = PATH + "imgAndId.xls";
        String writeImages = PATH + "writeImages.xls";
        List<PImages> imagesList = new ArrayList<>();
        List<PImages> writeList = new ArrayList<>();

        EasyExcel.read(imgAndId, PImages.class, new PageReadListener<PImages>(dataList -> {
            for (PImages p : dataList) {
                imagesList.add(p);
            }
        })).sheet().doRead();
        Map<String, PImages> imagesMap = imagesList.stream().collect(Collectors.toMap(PImages::getPartnerID, Function.identity()));

        for (PImages pImages : imagesList) {
            PImages newp = new PImages();
            newp.setPartnerID(pImages.getPartnerID());
            newp.setPartnerName(pImages.getPartnerName());
            String partnerName = pImages.getPartnerName();
            String substring = partnerName.substring(partnerName.lastIndexOf(".") + 1);
            newp.setImg("/partner/" + pImages.getPartnerID() + "." + substring);
            writeList.add(newp);

        }
        EasyExcel.write(writeImages, PSalesAndImage.class)
                .sheet("sheet1")
                .doWrite(() -> {
                    // 分页查询数据
                    return writeList;
                });

    }




}

