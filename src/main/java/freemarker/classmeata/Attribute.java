package freemarker.classmeata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by WangXW.
 * Date: 2019/10/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    private String name;
    private String type;
    private List<String> annotations;
    private String annoDesc;
    private String desc;
}
