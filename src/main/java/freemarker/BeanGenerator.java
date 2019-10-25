package freemarker;

import http.RapHttpClient;
import org.apache.commons.lang3.StringUtils;
import util.PropertiesUtils;

import java.util.Arrays;

/**
 * Created by WangXW.
 * Date: 2019/10/24
 */
public class BeanGenerator {

    public void Generator() {
        RapHttpClient rapHttpClient = new RapHttpClient();
        String ids = PropertiesUtils.get("id");
        Arrays.asList(StringUtils.split(ids,",")).forEach(
                id -> {
                    try {
                        String interfaceData = rapHttpClient.getInterfaceData(StringUtils.trim(id));
                        EntityClass entityClass = new EntityClass(interfaceData);
                        EntriyFreeMarker freeMarker = new EntriyFreeMarker();
                        entityClass.classInfos().forEach(classInfo -> {
                            freeMarker.process(classInfo);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );


    }
}
