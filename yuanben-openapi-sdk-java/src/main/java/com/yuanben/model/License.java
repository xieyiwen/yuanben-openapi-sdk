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

package com.yuanben.model;

import java.io.Serializable;

/**
 * @description 授权协议
 */

public class License implements Serializable {
    public static String CC = "cc";
    public static String CM = "cm";
    public static String NONE = "none";

    private String type;
    private LicenseParameters content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LicenseParameters getContent() {
        return content;
    }

    public void setContent(LicenseParameters content) {
        this.content = content;
    }
}
