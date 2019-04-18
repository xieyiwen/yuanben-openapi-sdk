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
import com.yuanben.common.Strings;
import com.yuanben.exception.InvalidParametersException;
import com.yuanben.model.*;
import org.apache.http.HttpException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 原本SDK客户端
 */

public class YuanbenClient {

    private static YuanbenClient client;
    private String url;
    private String accessToken;
    private HttpUtil httpUtil;

    public YuanbenClient(String url, String accessToken) throws HttpException {
        this.url = url;
        this.accessToken = accessToken;
        this.httpUtil = new HttpUtil();
    }

    public static void Init(String url, String accessToken) throws HttpException {
        client = new YuanbenClient(url, accessToken);
    }

    public static YuanbenClient getInstance() {
        return client;
    }

    public List<ArticleSaveResp> saveArticles(List<ArticleReq> articles) throws Exception {
        if (articles == null || articles.size() < 1) {
            return null;
        }
        for (ArticleReq article : articles) {
            if (Strings.isBlank(article.getContent())) {
                throw new InvalidParametersException("[article]content is empty :" + article.getClientID());
            }
            if (Strings.isBlank(article.getTitle())) {
                throw new InvalidParametersException("[article]title is empty :" + article.getClientID());
            }
            if (article.getLicense() == null || Strings.isBlank(article.getLicense().getType())) {
                throw new InvalidParametersException("[article]license is empty :" + article.getClientID());
            }
            if (License.CC.equals(article.getLicense().getType())) {
                if (Strings.isBlank(article.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[article]license's adaptation is empty :" + article.getClientID());
                }
                if (!"y".equals(article.getLicense().getContent().getAdaptation()) && !"n".equals(article.getLicense().getContent().getAdaptation())
                        && !"sa".equals(article.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[article]adaption is must in ['y','n','sa'] for CC :" + article.getClientID());
                }
                if (Strings.isBlank(article.getLicense().getContent().getCommercial())) {
                    throw new InvalidParametersException("[article]commercial is a must for CC :" + article.getClientID());
                }
            } else if (License.CM.equals(article.getLicense().getType())) {
                if (Strings.isBlank(article.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[article]license's adaptation is empty :" + article.getClientID());
                }
                if (!"y".equals(article.getLicense().getContent().getAdaptation()) && !"n".equals(article.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[article]adaption is must in ['y','n'] for CM :" + article.getClientID());
                }
                if (article.getLicense().getContent().getPrice() < 0) {
                    throw new InvalidParametersException("[article]price is a must for CM :" + article.getClientID());
                }
            } else if (License.NONE.equals(article.getLicense().getType())) {

            } else {
                throw new InvalidParametersException("[article]incorrect license :" + article.getClientID());
            }
        }

        ArticleSaveReq req = new ArticleSaveReq();
        req.setArticles(articles);

        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("authorization", "Bearer " + this.accessToken);
        headers.put("Content-Type", "application/json");
        String jsonResp = httpUtil.sendPost(this.url + "/articles", JSON.toJSONString(req), headers);
        return JSON.parseArray(jsonResp, ArticleSaveResp.class);
    }

    public ImageSaveResp saveImages(List<ImageReq> images) throws Exception {
        if (images == null || images.size() < 1) {
            return null;
        }
        for (ImageReq image : images) {
            if (Strings.isBlank(image.getTitle())) {
                throw new InvalidParametersException("[image]title is empty :" + image.getClientID());
            }
            if (Strings.isBlank(image.getImage())) {
                throw new InvalidParametersException("[image]image is empty :" + image.getClientID());
            }
            if (image.getTags() == null || image.getTags().size() < 1) {
                throw new InvalidParametersException("[image]tags is empty :" + image.getClientID());
            }
            if (image.getLicense() == null || Strings.isBlank(image.getLicense().getType())) {
                throw new InvalidParametersException("[image]license is empty :" + image.getClientID());
            }
            if (License.CC.equals(image.getLicense().getType())) {
                if (Strings.isBlank(image.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[image]license's adaptation is empty :" + image.getClientID());
                }
                if (!"y".equals(image.getLicense().getContent().getAdaptation()) && !"n".equals(image.getLicense().getContent().getAdaptation())
                        && !"sa".equals(image.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[image]adaption is must in ['y','n','sa'] for CC :" + image.getClientID());
                }
                if (Strings.isBlank(image.getLicense().getContent().getCommercial())) {
                    throw new InvalidParametersException("[image]commercial is a must for CC :" + image.getClientID());
                }
            } else if (License.CM.equals(image.getLicense().getType())) {
                if (Strings.isBlank(image.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[image]license's adaptation is empty :" + image.getClientID());
                }
                if (!"y".equals(image.getLicense().getContent().getAdaptation()) && !"n".equals(image.getLicense().getContent().getAdaptation())) {
                    throw new InvalidParametersException("[image]adaption is must in ['y','n'] for CM :" + image.getClientID());
                }
                if (image.getLicense().getContent().getPrice() < 0) {
                    throw new InvalidParametersException("[image]price is a must for CM :" + image.getClientID());
                }
            } else if (License.NONE.equals(image.getLicense().getType())) {

            } else {
                throw new InvalidParametersException("[image]incorrect license :" + image.getClientID());
            }
        }

        ImageSaveReq req = new ImageSaveReq();
        req.setImages(images);

        Map<String, String> auth = new HashMap<String, String>(1);
        auth.put("authorization", "Bearer " + this.accessToken);
        auth.put("Content-Type", "application/json");
        String jsonResp = httpUtil.sendPost(this.url + "/images", JSON.toJSONString(req), auth);
        return JSON.parseObject(jsonResp, ImageSaveResp.class);
    }
}
