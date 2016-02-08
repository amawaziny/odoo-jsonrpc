/*
 * Copyright 2014 QFast Ahmed El-mawaziny.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qfast.openerp.rpc.boundary;

import org.json.JSONObject;
import org.qfast.openerp.rpc.OeConst.JsonMenu;
import org.qfast.openerp.rpc.entity.OeGroup;
import org.qfast.openerp.rpc.entity.OeMenu;
import org.qfast.openerp.rpc.exception.OeRpcException;
import org.qfast.openerp.rpc.json.OeExecutor;
import org.qfast.openerp.rpc.util.OeBinder;
import org.qfast.openerp.rpc.util.OeCriteriaBuilder;
import org.qfast.openerp.rpc.util.OeUtil;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.qfast.openerp.rpc.OeConst.OeModel.MENUS;
import static org.qfast.openerp.rpc.boundary.OeMenuService.Fun.LOAD_MENUS;
import static org.qfast.openerp.rpc.entity.OeMenu._GROUPS_ID;
import static org.qfast.openerp.rpc.entity.OeMenu._GROUPS_ID_ID;
import static org.qfast.openerp.rpc.entity.OeMenu._PARENT_ID;
import static org.qfast.openerp.rpc.entity.OeMenu._PARENT_ID_ID;

/**
 * @author Ahmed El-mawaziny
 */
public class OeMenuService extends AbstractOeService<OeMenu> {

    public static final String name = MENUS.getName();

    public enum Fun {

        LOAD_MENUS("load_menus"),
        LOAD_MENUS_ROOT("load_menus_root");
        private final String name;

        private Fun(String name) {
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

    public OeMenuService(OeExecutor executer) {
        super(executer, OeMenu.class);
    }

    @Override
    protected String getName() {
        return name;
    }

    public JsonObject loadMenus() throws OeRpcException {
        if (executor.getVersion().getVersionNumber() < 8) {
            JSONObject params = new JSONObject();
            params.put("session_id", executor.getSessionId());
            params.put("context", executor.getJSONContext());
            return (JsonObject) executor.execute(JsonMenu.LOAD.getPath(), params);
        } else {
            return (JsonObject) executor.execute(name, LOAD_MENUS.getName());
        }
    }

    /**
     * load OpenERP Menus by calling loadMenus function in ir.ui.menu in OpenERP
     * v8. {@link #loadMenus() }
     *
     * @return
     * @throws OeRpcException
     */
    public OeMenu loadOeMenus() throws OeRpcException {
        JsonObject result = loadMenus();
        if (result != null) {
            return OeBinder.bind(result.toString(), OeMenu.class, this);
        } else {
            return null;
        }
    }

    @Override
    public List<OeMenu> find(List<Object> sc, Integer offset,
            Integer limit, String order, Map<String, Object> context,
            String... columns) throws OeRpcException {
        return super.find(this, sc, offset, limit, order, context, columns);
    }

    public List<OeMenu> findByUserIdAllInOne(Integer userId) throws OeRpcException {
        if (OeUtil.equals(userId, executor.getUserId())) {
            List<OeMenu> oeMenus = new ArrayList<OeMenu>(10);
            addChildren(oeMenus, loadOeMenus().getChildren());
            return oeMenus;
        } else {
            OeGroupService oeGroupService = new OeGroupService(executor);
            Set<OeGroup> oeGroups = oeGroupService.findByUserId(userId);
            oeGroupService = null;
            Set<OeMenu> menus = new TreeSet<OeMenu>();
            Integer[] oeGroupsId = new Integer[oeGroups.size()];
            int i = 0;
            for (OeGroup oeGroup : oeGroups) {
                oeGroupsId[i] = oeGroup.getId();
                i++;
            }
            menus.addAll(findByGroupId(oeGroupsId));
            menus.addAll(findByNoGroup());
            return new ArrayList<OeMenu>(menus);
        }
    }

    private void addChildren(List<OeMenu> menus, OeMenu[] children) {
        menus.addAll(Arrays.asList(children));
        for (OeMenu oeMenu : children) {
            addChildren(menus, oeMenu.getChildren());
        }
    }

    public OeMenu findByUserId(Integer userId) throws OeRpcException {
        if (!OeUtil.equals(userId, executor.getUserId())) {
            OeGroupService oeGroupService = new OeGroupService(executor);
            Set<OeGroup> oeGroups = oeGroupService.findByUserId(userId);
            oeGroupService = null;
            Integer[] oeGroupsId = new Integer[oeGroups.size()];
            int i = 0;
            for (OeGroup oeGroup : oeGroups) {
                oeGroupsId[i] = oeGroup.getId();
                i++;
            }
            OeMenu root = new OeMenu(this);
            root.setName("root");
            root.setParentId(new Object[]{-1, ""});
            setMenuChildren(root, oeGroupsId);
            return root;
        } else {
            return loadOeMenus();
        }
    }

    private void setMenuChildren(OeMenu parentMenu, Integer[] oeGroupsId) throws OeRpcException {
        Set<OeMenu> menus = new HashSet<OeMenu>(10);
        menus.addAll(findByGroupIdParentId(parentMenu.getId(), oeGroupsId));
        menus.addAll(findByNoGroupParentId(parentMenu.getId()));
        parentMenu.setChildren(menus.toArray(new OeMenu[menus.size()]));
        for (OeMenu oeMenu : menus) {
            setMenuChildren(oeMenu, oeGroupsId);
        }
    }

    public Set<OeMenu> findByNoGroup() throws OeRpcException {
        OeCriteriaBuilder cb = new OeCriteriaBuilder();
        cb.column(_GROUPS_ID).eq(null);
        return (new HashSet<OeMenu>(super.find(cb)));
    }

    public Set<OeMenu> findByNoGroupParentId(Integer parentId) throws OeRpcException {
        OeCriteriaBuilder cb = new OeCriteriaBuilder();
        cb.column(_GROUPS_ID).eq(null)
                .andColumn((parentId != null) ? _PARENT_ID_ID : _PARENT_ID)
                .eq(parentId);
        return (new HashSet<OeMenu>(super.find(cb)));
    }

    public Set<OeMenu> findByGroupId(Integer... groupId) throws OeRpcException {
        OeCriteriaBuilder cb = new OeCriteriaBuilder();
        cb.column(_GROUPS_ID_ID).in(groupId);
        return (new HashSet<OeMenu>(super.find(cb)));
    }

    public Set<OeMenu> findByGroupIdParentId(Integer parentId,
            Integer... groupId) throws OeRpcException {
        OeCriteriaBuilder cb = new OeCriteriaBuilder();
        cb.column(_GROUPS_ID_ID).in(groupId)
                .andColumn((parentId != null ? _PARENT_ID_ID : _PARENT_ID))
                .eq(parentId);
        return (new HashSet<OeMenu>(super.find(cb)));
    }
    private static final long serialVersionUID = -6623029584982281200L;
}
