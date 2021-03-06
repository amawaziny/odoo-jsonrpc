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

package com.odoo.rpc.boundary;

import com.odoo.rpc.entity.OeModule;
import com.odoo.rpc.exception.OeRpcException;
import com.odoo.rpc.json.OeExecutor;
import com.odoo.rpc.json.util.OeJUtil;

import java.util.List;
import java.util.Map;

import static com.odoo.rpc.util.OeConst.OeModel.MODULES;

/**
 * OeModuleService for find Odoo module (ir.module.module) by findById, finAll or
 * custom search criteria
 *
 * @author Ahmed El-mawaziny
 * @see OeModule
 * @since 1.0
 */
public class OeModuleService extends AbstractOeService<OeModule> {

    public static final String name = MODULES.getName();
    private static final long serialVersionUID = 3639886772896021816L;

    public OeModuleService(OeExecutor executor) {
        super(executor, OeModule.class);
    }

    public <C extends OeModule> OeModuleService(OeExecutor executor, Class<C> model) {
        super(executor, model);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Used to install Odoo modules
     *
     * @param ids id(s) of Odoo modules to install
     * @return true if module successfully installed
     * @throws OeRpcException if Odoo response with error
     */
    public boolean install(Object... ids) throws OeRpcException {
        executor.execute(name, Fun.INSTALL.name, OeJUtil.parseAsJsonArray(ids));
        return true;
    }

    /**
     * used to uninstall Odoo modules
     *
     * @param ids id(s) of Odoo modules to uninstall
     * @return true if module successfully uninstalled
     * @throws OeRpcException if Odoo response with error
     */
    public boolean uninstall(Object... ids) throws OeRpcException {
        executor.execute(name, Fun.UNINSTALL.name, ids, true);
        return true;
    }

    @Override
    public List<OeModule> find(List<Object> sc, Integer offset, Integer limit, String order, Map<String, Object> context,
                               String... columns) throws OeRpcException {
        return super.find(this, sc, offset, limit, order, context, columns);
    }

    /**
     * enum contains some functions for Odoo module
     */
    public enum Fun {

        INSTALL("button_immediate_install"),
        UNINSTALL("button_immediate_uninstall");

        private final String name;

        Fun(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
