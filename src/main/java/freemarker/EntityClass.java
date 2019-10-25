package freemarker;

import freemarker.classmeata.Attribute;
import freemarker.classmeata.ClassInfo;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import util.PropertiesUtils;

import java.util.*;

/**
 * Created by WangXW.
 * Date: 2019/10/22
 */
public class EntityClass {
    private Map<String, Object> clazzMetaData;
    private List<Map<String, Object>> requestDatas = new ArrayList<>();
    private List<Map<String, Object>> responseDatas = new ArrayList<>();
    private Map<Integer, String> arrayNameMap = new HashMap<>();
    private Map<Integer, List<Map<String, Object>>> requestArrayDatas = new HashMap<>();
    private Map<Integer, List<Map<String, Object>>> responseArrayDatas = new HashMap<>();

    public EntityClass(String jsonStr) {
        Map data = JSON.parseObject(jsonStr, Map.class);
        clazzMetaData = (Map) data.get("data");
        Optional.ofNullable(clazzMetaData).ifPresent(
                metaData -> {
                    List<Map> content = (List<Map>) metaData.get("properties");
                    content.forEach(map -> {
                        String scope = (String) map.get("scope");
                        if (StringUtils.equals("Array", (String) map.get("type"))) {
                            String name = (String) map.get("name");
                            arrayNameMap.put((Integer) map.get("id"), name);
                        }
                        Integer parentId = (Integer) map.get("parentId");
                        if (Integer.valueOf(-1).equals(parentId)) {
                            if ("request".equals(scope)) {
                                requestDatas.add(map);
                            } else if ("response".equals(scope)) {
                                responseDatas.add(map);
                            }
                        } else {
                            if ("request".equals(scope)) {
                                getArrayData(map, requestArrayDatas, parentId);
                            } else if ("response".equals(scope)) {
                                getArrayData(map, responseArrayDatas, parentId);
                            }
                        }
                    });
                }
        );
    }

    private void getArrayData(Map content, Map<Integer, List<Map<String, Object>>> arrayData, Integer parentId) {
        List<Map<String, Object>> list = arrayData.get(parentId);
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        content.put("parentId", -1);
        list.add(content);
        arrayData.put(parentId, list);
    }

    public List<ClassInfo> classInfos() {
        List<ClassInfo> classInfos = new ArrayList<>();
        requestArrayDatas.forEach((id, array) -> {
            String arrayName = arrayNameMap.get(id);
            arrayName = toUpperCaseFirst(arrayName);
            classInfos.add(getClassInfo(array, "Request" + arrayName + "Model"));
        });
        responseArrayDatas.forEach((id, array) -> {
            String arrayName = arrayNameMap.get(id);
            arrayName = toUpperCaseFirst(arrayName);
            classInfos.add(getClassInfo(array, "Response" + arrayName + "Model"));
        });
        classInfos.add(getClassInfo(requestDatas, "RequestModel"));
        classInfos.add(getClassInfo(responseDatas, "ResponseModel"));
        return classInfos;
    }

    private ClassInfo getClassInfo(List<Map<String, Object>> datas, String suffix) {
        boolean validation = false;
        ClassInfo classInfo = null;
        List<Attribute> attributes = new ArrayList<>();
        List<String> imports = null;
        List<String> annotations = null;
        String url = (String) clazzMetaData.get("url");
        if (!datas.isEmpty()) {
            classInfo = new ClassInfo();
            imports = classInfo.getImports();
            classInfo.setPackagename(PropertiesUtils.get("package"));
            classInfo.setClassname(url + suffix);
            for (Map<String, Object> data : datas) {
                Attribute attribute = new Attribute();
                attribute.setName((String) data.get("name"));
                attribute.setDesc((String) data.get("description"));
                String type = (String) data.get("type");
                // 处理array类型
                if (StringUtils.equals("Array", type)) {
                    String name = (String) data.get("name");
                    String scope = (String) data.get("scope");
                    name = toUpperCaseFirst(name);
                    if ("request".equals(scope)) {
                        name = url + "Request" + name + "Model";
                    } else if ("response".equals(scope)) {
                        name = url + "Response" + name + "Model";
                    }
                    attribute.setType("List<" + name + ">");
                    imports.add("import java.util.List");
                } else {
                    attribute.setType(type);
                }
                annotations = new ArrayList<>();
                if ((Boolean) data.get("required")) {
                    validation = true;
                    // 可以放到模板中
                    String notNull = "@NotNull(message = \"" + data.get("description") + "不能为空\")";
                    annotations.add(notNull);

                }
                String rule = (String) data.get("rule");
                if (rule != null && StringUtils.trim(rule) != "") {
                    validation = true;
                    List<String> annos = Arrays.asList(StringUtils.split(rule, "&"));
                    for (String anno : annos) {
                        annotations.add(anno);
                    }
                }
                attribute.setAnnotations(annotations);
                attributes.add(attribute);
            }

        }
        if (validation) {
            imports.add("import javax.validation.constraints.*");
        }
        classInfo.setAttributes(attributes);
        return classInfo;
    }

    private String toUpperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase().concat(str.substring(1).toLowerCase());
    }

}
