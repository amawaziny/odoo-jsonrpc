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

package com.odoo.rpc.json.util.adaptor;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.odoo.rpc.util.OeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Gson deserializer implementation for Map
 *
 * @author Ahmed El-mawaziny
 * @since 1.0
 */
public class MapDeserializer implements JsonDeserializer<Map> {

    @Override
    public Map deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json != null && !json.isJsonNull() && json.isJsonPrimitive()) {
            if (json.getAsJsonPrimitive().isBoolean()) {
                return null;
            } else if (json.getAsJsonPrimitive().isString()) {
                String asString = OeUtil.clearMap(json.getAsString());
                return new Gson().fromJson(asString, Map.class);
            }
        }
        return new Gson().fromJson(json, Map.class);
    }
}
