/*
 * Copyright 2019 Shanghai Qiyin Information Technology Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuanben;

import com.alibaba.fastjson.JSON;
import com.yuanben.common.HttpUtil;
import com.yuanben.model.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpException;
import org.junit.Test;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YuanbenClientTest {

    public YuanbenClientTest() throws HttpException {
        YuanbenClient.Init("http://openapi.staging.yuanben.site/v1/media", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQ0NDBiNTYxYWZhM2NmMjExMTY0M2E5YWIzM2Q3Y2JhZTcxY2JkOGU5MTRkNjk1OTBkZjFjZjY5MzQyMDZjOWJiMzE4ZmRkYTk5MzU2ODBhIn0.eyJhdWQiOiIxIiwianRpIjoiZDQ0MGI1NjFhZmEzY2YyMTExNjQzYTlhYjMzZDdjYmFlNzFjYmQ4ZTkxNGQ2OTU5MGRmMWNmNjkzNDIwNmM5YmIzMThmZGRhOTkzNTY4MGEiLCJpYXQiOjE1NDQ1MTc5MzIsIm5iZiI6MTU0NDUxNzkzMiwiZXhwIjoxODYwMTM3MTMxLCJzdWIiOiIzNjc1Iiwic2NvcGVzIjpbIm1lZGlhIl19.JnsLDSFvqIH94LgXmujjLNhtkejQK4RtGLLedB91cqSlo4HI_dd8gJzw0qZJCA_VVMT54Bd93t8nabh79-nC-L2wKFgHRKyz1U9pa_pKCZcpfkOwe4QG3YKnLsoqO5kINWuy9eX-Zi0zw0zHBZ9lrlR9ePEJPrSVHe_GsfZ7g0urthrqBV2TXTj8GRBgXlTuJzeW1QWnHONrwd1ofQ-Z9l4JlOUy9LdVXyfCH644GgqRjTo18jFit9Y2VZqP6ySY0mMA635DQgYhyZsdkRZ9mJDkMfp9hfyI31rGOWP3iGuTg36RlLr3Olq6c5J6Q3_qvTVpVj3dz6C1-NA_VA4FfA3y80pwWGnXhJYCuKPSKw6wKZ9MKM1e3tH5EIiICGMz7KXJlac6_NVL037-hrKqdso1VxWWAGzSH_tU4Ska_90I6lUKmTfcmvFoF9fln6_aj98B8klC8rXNzwJjxSFT1JM8a6mB9pvZZJrsrhYn9g9G-skstpn1Mfz0XbumxR4inMBhh-_n171PkkrpbF4rsX1voID5zBFqQyIpFFGC8EBfVviyo9rbg_r5FbVrjoVI_BK6HvHjxFIxIwjtst4fNlQo8XQpRIiKU3bh-AAMIFu6j64MUsJWKdkHdcobpuFvjRB9ynQtiHPq-ojlmbF2uai9sRwDtnBoVJB8XCnzrNw");
    }

    @Test
    public void saveArticle() throws Exception {
        List<ArticleReq> list = new ArrayList<ArticleReq>(2);
        ArticleReq article1 = new ArticleReq();
        article1.setClientID(1);
        article1.setClosed(false);
        article1.setContent("原本，重新定义原创价值");
        License article1_cc = new License();
        article1_cc.setType(License.CC);
        LicenseParameters parameters1 = new LicenseParameters();
        parameters1.setAdaptation("sa");
        parameters1.setCommercial("y");
        article1_cc.setContent(parameters1);
        article1.setLicense(article1_cc);
        article1.setTitle("原本，重新定义原创价值");
        list.add(article1);

        ArticleReq article2 = new ArticleReq();
        article2.setClientID(2);
        article2.setContent("区块链原创保护和自助交易平台");
        License article2_cc = new License();
        article2_cc.setType(License.CM);
        LicenseParameters parameters2 = new LicenseParameters();
        parameters2.setAdaptation("y");
        parameters2.setPrice(9550);
        article2_cc.setContent(parameters2);
        article2.setLicense(article2_cc);

        article2.setTitle("区块链原创保护和自助交易平台");
        list.add(article2);

        List<ArticleSaveResp> articleSaveResps = YuanbenClient.getInstance().saveArticles(list);
        System.out.println(JSON.toJSON(articleSaveResps));

    }


    @Test
    public void saveImages() throws Exception {
        File file = new File("test.jpg");
        FileImageInputStream reader = new FileImageInputStream(file);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int n;
        while ((n = reader.read(buf)) != -1) {
            output.write(buf, 0, n);
        }

        byte[] data = output.toByteArray();

        String base64String = Base64.encodeBase64String(data);


        List<ImageReq> list = new ArrayList<ImageReq>(2);
        ImageReq image1 = new ImageReq();
        image1.setClientID(1);
        image1.setDescription("第一张图，使用base64编码");
        License image1_cc = new License();
        image1_cc.setType(License.CC);
        LicenseParameters parameters1 = new LicenseParameters();
        parameters1.setAdaptation("sa");
        parameters1.setCommercial("y");
        image1_cc.setContent(parameters1);
        image1.setLicense(image1_cc);

        ArrayList<String> tags = new ArrayList<String>();
        tags.add("测试");

        image1.setTags(tags);

        image1.setTitle("落日");

        image1.setImage(base64String);
        list.add(image1);

        ImageSaveResp resp = YuanbenClient.getInstance().saveImages(list);

        assert resp.getStatus().getCode() == 200 : resp.getStatus().getMessage();
        System.out.println(JSON.toJSON(resp));
    }

    @Test
    public void hydxTest() {
        try {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.hydxTest();
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
}
