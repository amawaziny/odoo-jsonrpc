/*
 * Copyright 2016 QFast Ahmed El-mawaziny
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
package org.qfast.openerp.rpc.entity;

import org.qfast.openerp.rpc.OeConst.OeModel;
import org.qfast.openerp.rpc.boundary.OeActionClientService;
import org.qfast.openerp.rpc.util.OeUtil;

import java.util.Map;

/**
 * @author Ahmed El-mawaziny
 */
public class OeActionClient extends AbstractOeEntity<OeActionClientService> {

    public static final String _TYPE = "type", _HELP = "help", _RES_MODEL = "res_model", _CONTEXT = "context";
    private static final long serialVersionUID = -1963097797139212178L;
    private Integer id;
    private String name;
    private String help;
    private String resModel;
    private Map<String, Object> context;
    private String type;

    public OeActionClient() {
    }

    public OeActionClient(OeActionClientService oe) {
        super.oe = oe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getResModel() {
        return resModel;
    }

    public void setResModel(String resModel) {
        this.resModel = resModel;
    }

    public OeModel getOeModel() {
        return OeModel.value(resModel);
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = OeUtil.convertStringToMap(context);
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + OeUtil.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && OeUtil.equals(this.id, ((OeActionClient) obj).id);
    }

    @Override
    public String toString() {
        return "OeActionClient{" + "id=" + id
                + ", name=" + name
                + ", help=" + help
                + ", resModel=" + resModel
                + ", context=" + context
                + ", type=" + type + '}';
    }
}
