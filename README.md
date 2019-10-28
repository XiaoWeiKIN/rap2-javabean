# rap2-javabean

## 1.使用方式

注意：引入该插件后,将集成lombok,接口参数校验,需要先安装lombok插件.

在resource目录下配置rap.properties

```
###### cooike
koa.sid=EljUmooFFMzg1y9tnSWa45gbXydNAPZ-
koa.sid.sig=9TBKH7eQiHZluN38jCjsbjHEww0
###### 要生成文件的相对包路径
package=com.bean
###### rap接口的地址
url=http://rap2api.taobao.org/interface/get?id=
###### 生成的接口id,多个以逗号隔开
id=1342622
```
接口url填写你要生成的类名,会自动生成带Request/ResponseModel的javaBean

如果在勾选不能为空则自动生成接口校验注解,可以在rule一栏书写你想要扩展的校验注解

支持嵌套Array类型的集合Bean生成。

```
public class BeanGeneratorTest {

    public static void main(String[] args) {
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.generator();
    }
}

![Image text](https://github.com/IndiraFinish/rap2-javabean/blob/master/src/main/resources/image/B813B703-8589-456d-8C99-539A9D2A29BD.png)

## 原理

freemarker+httpclient 可自己定制开发

