## 说明

- 使用 WSDL2.0 版本

- 抽离 Fault 相关的WSDL组件，使用 import 进行导入

- 为 http://jw.nju.edu.cn/scoreService 通过HTTP绑定SOAP消息
  
- 定义如下操作和错误：
```
    - add
    
        - 输入：jw:课程成绩列表 类型
    
        - 输出：wsdl:状态 类型
    
        - 错误：
    
            - 参数错误
    
            - 目标已存在
    
    - delete
    
        - 输入：jw:课程成绩列表 类型
    
        - 输出：wsdl:状态 类型
    
        - 错误：
    
            - 参数错误
    
            - 课程、学生、课程类型不存在

    - update

        - 输入：jw:课程成绩列表 类型

        - 输出：wsdl:状态 类型

        - 错误：
        
            - 参数错误
    
            - 课程、学生、课程类型不存在
    
    - get

        - 输入：jw:课程成绩列表 类型

        - 输出：wsdl:状态 类型

        - 错误：

            - 参数错误

            - 学生信息不存在
```

文件目录

```
    - xsd：
    
        - Course.xsd
        
        - Department.xsd
        
        - PersonInfo.xsd
        
        - ScoreFault.xsd
        
        - ScoreList.xsd
        
        - Student.xsd
        
        - StudentList.xsd
        
    - wsdl
    
        - ScoreService.wsdl
        
        - ScoreFault.wsdl
```
