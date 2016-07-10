/*
 * Copyright 2016 QFast
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

package com.odoo.rpc.json.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odoo.rpc.boundary.AbstractOeService;
import com.odoo.rpc.entity.AbstractOeEntity;
import com.odoo.rpc.entity.OeView;
import com.odoo.rpc.json.util.adaptor.OeViewDeserializer;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

/**
 * @author Ahmed El-mawaziny
 */
public class OeBinder {

    @SuppressWarnings("unchecked")
    public static <T extends AbstractOeEntity, E extends AbstractOeService> T bind(String result, Class<T> tClaz, E oe)
            throws JsonSyntaxException {
        Gson gson = OeGson.getGsonBuilder()
                .registerTypeAdapter(OeView.class, new OeViewDeserializer())
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .create();
        result = result.replace("u'", "'");
        T instance = gson.fromJson(result, tClaz);
        instance.setOe(oe);
        return instance;
    }
}