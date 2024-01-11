 [![license](https://img.shields.io/github/license/crowdosNWPU/CrowdOS)](https://www.apache.org/licenses/LICENSE-2.0) [![build](https://img.shields.io/github/actions/workflow/status/crowdosNWPU/CrowdOS/maven.yml)]()  [![issues](https://img.shields.io/github/issues/crowdosNWPU/CrowdOS)]()
 


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
CrowdOS目标在于提升群智应用的构建效率，降低群智应用的使用门槛。目前CrowdOS使用CrowdOS Kernel和SpringBoot等Web框架快速开发基于群智感知功能的群智APP。一个使用CrowdOS Kernel开发的群智APP的框架图如下：
[img1-1.png](https://github.com/crowdosNWPU/CrowdOS/blob/main/src/site/resources/images/img1-1.png)
#### Kernel.constriaint与Kernel.resource
##### 1、设计逻辑
群智感知应用中关键的两个因素是任务与完成任务的参与者。在CrowdOS Kernel中任务被定义为包含一组**约束**（constraint包）条件的开发者自定义**任务**（resource包），参与者为包含一组能力的开发者自定义**参与者**（resource包）。
任务Task（实际情况为实现Task接口的程序员自定义实体类）通过方法canAssignTo()，检测该任务能否分配给某个参与者Participant（实际情况为实现Participant接口的程序员自定义实体类）。
每一个约束（实际情况为实现Constraint接口的程序员自定义实体类）通过方法satisfy()，检测某条件（实际情况为实现Condition接口的程序员自义定的实体类）是否满足自身要求。
任务能否分配给某个参与者的充分必要条件为：任务的所有约束（Constraint）条件都要被该参与者所具备的能力（Ability）满足（在目前的术语中，参与者的能力（Ability）与满足约束的条件（Condition）是同一个意思）。在具体实现中，方法canAssignTo()会做两个检查：
1.检查对于任务要求的某约束类型，该参与者是否具体满足该类型约束的能力。例如，任务要求在某一区域执行任务，此时需要坚持参与者是否提供了自己的GPS信息。
2.检查参与者所具有的某个能力，是否是能满足该约束的条件。例如，检查参与者的GPS位置信息是否在任务要求的范围内。
设计逻辑图如下所示:
![Alt text](img2-1.png)
##### 2、设计模式
constraint包与resource包中的类实现了一种Double Dispatch模式（双分派的另一个常用的实现是Visitor模式）。同时使用了一些反射技术优化了代码实现。具体类图如下：
![Alt text](img3-1.png)
##### 3、其他内容
constraint包中包含了构建任务的Constraint接口和构建参与者的Condition接口，并且提供了一部分实现简单实现。此外，在constraint.wrapper包中，提供了基础类型的Condition版本（此处的逻辑与JAVA包装类相同）。
![Alt text](img5-1.png)
resource包中除过关键的Task和Participant接口外，还提供了对应的Abstract Class分别为AbstractTask和AbstractParticipant。当程序员开发自定义任务和参与者时，只要继承对应的抽象基类，而不必从实现基础接口开始（此处的设计逻辑与JAVA Container部分设计思路相同）。此外还提供了一些示例性的实体类。
![Alt text](img4-1.png)
#### Kernel.system
##### 1、设计逻辑
system包中的SystemResourceCollection管理了系统中的所有实体。目前，系统实体包括TaskPool、ParticipantPool、AlgoContainer和Schedule。系统实体需要继承Resource接口，实现getHandler()方法。出于对系统实体的保护，所有访问系统实体的操作都应该通过getHandler()方法，该方法返回某一个具体实体的处理句柄。其他包在访问系统实体时，必须通过实体句柄。系统实体句柄SystemResourceHandler<T>提供了两类访问方式：
1.T getResourceView()：访问该实体的不可更改视图；
2.T getResource()：访问该实体本身。
system包对其他包（或代码）做出了一个约定：当其他包（或其他代码）使用getResourceView()时，system包保证系统功能正常（例如，调度系统、任务池管理、参与者池管理等）；当使用getResource()时，system包不提供该保证。
基于system包提供的保证，其他包可以放心的通过系统实体句柄实现自己的功能。例如，在algorithms包中，算法的实现需要访问系统实体的各项信息，因此该包中所有操作仅可使用getResourceView()。
##### 2、设计模式
system包实现了一种类似于迭代模式的保证。具体的UML类图如下：
![Alt text](image6-1.png)
#### Kernel.algorithms
##### 1、设计逻辑
algorithms包定义了系统中所使用的群智感知相关算法，目前提供了任务分配、任务推荐和参与者选择算法接口。algorithms包实现了算法与系统流程的解耦。在实现algorithms包中算法的过程中，基于system包提供的保证，可以解耦算法流程与系统流程，确保系统流程的稳定运行。
##### 2、设计模式
algorithms包涉及的设计模式主要有两种，分别为：
1.algorithms自身使用了工程模式。每一类算法工厂产出一类特定的算法实现。目前每个算法工厂需要分别实现任务分配、任务推荐和参与者选择算法；
2.algorithms与Scheduler交互时，algorithms以模板模式的形式嵌入Scheduler。
algorithms包的UML类图如下：
![Alt text](image7-1.png)
##### 3、算法说明
algorithms包中提供了四种经典的任务分配算法，分别为T_Most、PT_Most、T_Random、GGA_I。四种算法对应的算法工厂均继承自算法适配器AlgoFactoryAdapter，每种算法都可支持但任务分配和多任务分配。
接口AlgoFactory定义了内核中使用的所有算法的接口，目前定义了三个功能：
![Alt text](image.png)
算法适配器AlgoFactoryAdapter实现接口AlgoFactory，为系统提供了默认任务分配、任务推荐和参与者选择算法，若不进行算法选择，则系统提供其中的默认算法实现，具体算法的接入可通过继承算法适配器AlgoFactoryAdapter实现。
#### CrowdKernel系统接口与实现
##### 1、1.CrowdKernel接口
接口CrowdKernel定义了程序员与内核功能交互的接口。目前CrowdKernel定义了如下功能：
![Alt text](image-1.png)
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
