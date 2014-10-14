/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.engine.utils.ZkPaths;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Service
public class CellService {
    @Autowired
    private ZkClient zkClient;

    @Export
    public List<String> listGroups() {
        List<String> groups = zkClient.getChildren(ZkPaths.rootPath());
        List<String> notEmptyGroups = Lists.newArrayList();
        for (String group : groups) {
            if (zkClient.countChildren(ZkPaths.groupPath(group)) > 0) {
                notEmptyGroups.add(group);
            }
        }
        return notEmptyGroups;
    }

    @Export(paramNames = {"group"})
    public List<String> listCellsByGroup(String group) {
        return zkClient.getChildren(ZkPaths.groupPath(group));
    }

    public LinkedHashMap<String, List<String>> listAll() {
        List<String> groups = zkClient.getChildren(ZkPaths.rootPath());
        LinkedHashMap<String, List<String>> result = Maps.newLinkedHashMap();
        for (String group : groups) {
            List<String> cells = zkClient.getChildren(ZkPaths.groupPath(group));
            if (cells != null && !cells.isEmpty()) { // 过滤空 group
                result.put(group, cells);
            }
        }
        return result;
    }

    @Export
    public Map<String, Integer> countAll() {
        LinkedHashMap<String, List<String>> all = listAll();
        Map<String, Integer> result = Maps.newHashMap();
        result.put("groups", all.size());
        int cells = 0;
        for (List<String> cellList : all.values()) {
            cells += cellList.size();
        }
        result.put("cells", cells);
        return result;
    }

    @Export
    public void clean() {
        List<String> groups = zkClient.getChildren(ZkPaths.rootPath());
        for (String group : groups) {
            if (zkClient.countChildren(ZkPaths.groupPath(group)) == 0) {
                zkClient.delete(ZkPaths.groupPath(group));
            }
        }
    }
}
