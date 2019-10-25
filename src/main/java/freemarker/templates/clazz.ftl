package ${classInfo.packagename};

import lombok.Builder;
import lombok.Data;

<#list classInfo.imports as import>
${import};
</#list>

/**
 * RAP模板自动生成请勿直接修改
 */
@Data
@Builder
public class ${classInfo.classname} {

<#if classInfo.attributes??>
<#list classInfo.attributes as attribute>
<#if attribute.desc??>
    /**
     * ${attribute.desc}
     */
</#if>
<#if attribute.annotations??>
<#list attribute.annotations as annotation>
    ${annotation}
</#list></#if>
    private ${attribute.type} ${attribute.name};
 </#list>
</#if>

}
