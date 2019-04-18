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
        YuanbenClient.Init("http://openapi.staging.yuanben.site/v1/media", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0.......NTYxjOmQ4ZTkxNGuai9sRwDtnBoVJB8XCnzrNw");
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
}
