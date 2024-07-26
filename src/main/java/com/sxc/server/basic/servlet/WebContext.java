package com.sxc.server.basic.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {
    private List<Entity> entities = null;
    private List<Mapping> mappings = null;
    //key->servlet-name，value->servlet-class
    private Map<String,String> entityMap = new HashMap<>();
    //key->url-pattern，value->servlet-name
    private Map<String,String> mappingMap = new HashMap<>();

    public WebContext(List<Entity> entities, List<Mapping> mappings) {
        this.entities = entities;
        this.mappings = mappings;

        //将List的entity和mapping转换成Map
        for(Entity e : this.entities) {
            String name = e.getName();
            String clz = e.getClz();
            entityMap.put(name, clz);
        }
        for(Mapping m : this.mappings) {
            for(String p : m.getPatterns()) {
                mappingMap.put(p, m.getName());
            }
        }
    }

    public String getClz(String pattern) {
        String name = mappingMap.get(pattern);
        if (name == null) {
            System.out.println("No servlet-name found for pattern: " + pattern);
            return null;
        }
        String class_name = entityMap.get(name);
        if (class_name == null) {
            System.out.println("No servlet-class found for servlet-name: " + name);
            return null;
        }
        System.out.println(class_name);
        return class_name;
    }
}
