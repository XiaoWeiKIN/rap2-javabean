package freemarker.classmeata;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangXW.
 * Date: 2019/10/24
 */
@Data
@NoArgsConstructor
public class ClassInfo {
    private String packagename;
    private String classname;
    private List<String> imports = new ArrayList<>();
    private List<Attribute> attributes;
}
