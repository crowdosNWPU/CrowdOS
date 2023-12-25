 [![license](https://img.shields.io/github/license/loyx/CrowdOS)](https://www.apache.org/licenses/LICENSE-2.0) [![build](https://img.shields.io/github/actions/workflow/status/loyx/CrowdOS/maven.yml)]()  [![issues](https://img.shields.io/github/issues/loyx/CrowdOS)]()
 


![Logo](https://raw.githubusercontent.com/li-haoyang1/CrowdOS/master/src/site/resources/images/logo.jpg)
CrowdOS是一个无处不在的操作系统，用于[Crowdsoucring](https://en.wikipedia.org/wiki/Crowdsourcing)和[移动众包](https://en.wikipedia.org/wiki/Crowdsensing)，它可以同时处理多种类型的它可以同时处理多种类型的众包问题。

## 安装和入门

CrowdOS可在Maven中心获得。

如果你使用Maven或Gradle，请在你的构建脚本中添加一个具有以下坐标的依赖关系。
```xml
<dependencies>
    <dependency>
        <groupId>cn.crowdos</groupId>
        <artifactId>crowdos-kernel</artifactId>
        <version>1.0.3</version>
    </dependency>
</dependencies>
```
你可以以任何方式使用CrowdOS提供的功能，但如果你想开发一个springboot应用程序。你只需要完成以下几个简单的步骤。
1. 你需要一个像这样的 _CrowdKernelComponent.java_，它是由Spring框架使用的。
```java
// CrowdKernelComponent.java
import cn.crowdos.kernel.CrowdKernel;
import cn.crowdos.kernel.Kernel;
import org.springframework.stereotype.Component;

@Component
public class CrowdKernelComponent {
    public CrowdKernel getKernel(){
        CrowdKernel kernel = Kernel.getKernel();
        if (!kernel.isInitialed())kernel.initial();
        return kernel;
    }
}
```
2. 你需要按照你的要求创建参与者类，你应该实现_Participant_接口。或者直接继承自 _AbstractParticipant_ 类。
```java
import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.wrapper.*;
import cn.crowdos.kernel.resource.*;
import com.fasterxml.jackson.annotation.JsonFormat;


public class User extends AbstractParticipant {

    @ability
    private IntegerCondition userId;

    @ability
    @JsonFormat(pattern = "yyyy.MM.dd")
    private DateCondition activeTime;
    
    //...
}
```
3. 你需要按照你的意愿创建任务类，你应该实现 _Task_ 接口或直接继承 _AbstractTask_ 类或我们提供的任何其他任务类。
```java
import cn.crowdos.kernel.constraint.Constraint;
import cn.crowdos.kernel.resource.SimpleTask;

import java.util.List;


public class Task extends SimpleTask {
    private int taskId;

    public Task(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        super(constraints, taskDistributionType);
    }
    
    // ...
}
```
4. 为了使用CrowdOS内核提供的功能，你应该首先注册参与者并提交任务。
```
// use 
crowdKernelComponent.getKernel().registerParticipant(user);
// and
crowdKernelComponent.getKernel().submitTask(task);
```
5. 做你想做的一切。

这是一个由CrowdOS驱动的Springboot应用程序的简单演示（[CrowdOS-demo](https://github.com/loyx/CrowdOS-demo)）。和[WeSense]()是一个实际运行的应用程序，它是基于CrowdOS的，你可以从安卓和IOS的应用程序商店中下载它。

## 获得帮助
如果你在使用CrowdOS时遇到任何问题，以下内容可能对你有帮助。
- 访问我们的网站[www.crowdos.cn](https://www.crowdos.cn/)。
- 查看[参考文档](https://crowdos.cn/crowdos_doc/public/apidocs/)。
- 或者给我们发电子邮件。_crowdos_nwpu@163.com_


## 报告问题
CrowdOS使用GitHub的集成问题跟踪系统来记录bug和功能请求。
如果你想提出一个问题，请遵循以下建议。
- 在你记录一个bug之前，请搜索问题追踪器，看看是否有人已经报告了这个问题。
- 如果该问题还不存在，请创建一个新的问题。
- 请在问题报告中提供尽可能多的信息。我们希望知道CrowdOS的版本。
  操作系统，以及你所使用的JVM版本。
- 如果你需要粘贴代码或包括堆栈跟踪，请使用Markdown。````在你的文本前后都要转义。
- 如果可能的话，尝试创建一个测试案例或项目来复制这个问题，并将其附加到问题中。
## 模块
**注意：CrowdOS仍处于开发阶段，目前只有内核部分已经完成。
### Kernel
crowdos-kernel。主库，提供支持CrowdOS内核其他部分的功能。
#### kernel.algorithms
**algorithms**包含了**Scheduler**使用的三种算法。这部分将被移到crowdos-aaas功能中。
#### kernel.Constraint
包含了一些在构建任务时使用的约束。
#### kernel.resource
包含参与者和任务。
#### kernel.system
提供一些访问系统资源的方式。
### ALGO
crowdos-aaas(CrowdOS Algorithms as a Service)。即将推出。
### DataService
crowdos-dataservice. 即将推出。
### SimulationSystem
即将推出。

## 引导
[www.crowdos.cn](https://www.crowdos.cn/)网站包含了CrowdOS的一些细节信息。
## 许可证
CrowdOS项目是根据[Apache License](https://www.apache.org/licenses/LICENSE-2.0)的2.0版本发布的。
