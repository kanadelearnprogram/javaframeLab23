## 错误原因分析

这个错误是由于Spring MVC无法正确解析控制器方法参数名称导致的。

### 错误信息解释
```
Name for argument of type [java.lang.String] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.
```


这意味着Spring MVC无法通过反射获取方法参数的名称，需要编译器使用'-parameters'标志来保留参数名称信息。

### 具体问题
在LoginController的login方法中：
```java
@PostMapping("/login")
public String login(HttpServletRequest request,String password, String username){
```


Spring MVC需要知道参数名称来绑定请求参数，但由于编译时没有保留参数名称信息，它无法自动识别password和username参数应该对应哪些请求参数。

### 解决方案（无需修改代码）
这个问题可以通过以下方式解决，而不需要修改代码：

1. **在编译时添加-parameters标志**：
    - 在Maven中配置编译插件添加该标志
    - 或者在IDE的编译器设置中启用该选项

2. **使用@RequestParam注解明确指定参数名称**：
   ```java
   @PostMapping("/login")
   public String login(HttpServletRequest request,
                      @RequestParam("password") String password, 
                      @RequestParam("username") String username){
   ```

## 错误原因分析

这个错误是由于MyBatis配置文件中引用了不存在的Mapper XML文件导致的。

### 错误信息解释
```
java.io.IOException: Could not find resource mapper/TClassMapper.xml
```


这意味着MyBatis在初始化时尝试加载`mapper/TClassMapper.xml`文件，但找不到该文件。

### 具体问题
在[sql-mapconfig.xml](file://E:\codelab\javaframeLab23\src\main\resources\sql-mapconfig.xml)配置文件中，有以下配置：
```xml
<mappers>
    <mapper resource="mapper/TClassMapper.xml" />
    <mapper resource="mapper/TCourseMapper.xml" />
    <mapper resource="mapper/TFileCategoryMapper.xml" />
    <mapper resource="mapper/TFileMapper.xml" />
    <mapper resource="mapper/TScoreMapper.xml" />
    <mapper resource="mapper/TSpaceMapper.xml" />
    <mapper resource="mapper/TUserMapper.xml" />
    <mapper resource="mapper/UserMapper.xml" />
</mappers>
```


但实际项目中缺少了这些以"T"开头的Mapper XML文件（如TClassMapper.xml、TCourseMapper.xml等）。

### 解决方案（无需修改代码）
这个问题可以通过以下方式解决，而不需要修改代码：

1. **移除sql-mapconfig.xml中不存在的mapper引用**
   - 从配置文件中删除对不存在的XML文件的引用

2. **创建缺失的Mapper XML文件**
   - 为每个引用的Mapper创建对应的XML文件

3. **使用注解替代XML配置**
   - 在Mapper接口上使用@Select等注解，避免使用XML文件

由于您要求不需要修改，我会推荐第一种方法，即清理sql-mapconfig.xml中的无效引用。