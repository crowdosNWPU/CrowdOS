 [![license](https://img.shields.io/github/license/crowdosNWPU/CrowdOS)](https://www.apache.org/licenses/LICENSE-2.0) [![build](https://img.shields.io/github/actions/workflow/status/crowdosNWPU/CrowdOS/maven.yml)]() [![lang](https://img.shields.io/github/languages/top/crowdosNWPU/CrowdOS)]() [![issues](https://img.shields.io/github/issues/crowdosNWPU/CrowdOS)]() [![fork](https://img.shields.io/github/forks/crowdosNWPU/CrowdOS?style=social)]() [![stars](https://img.shields.io/github/stars/crowdosNWPU/CrowdOS?style=social)]()
 
 
 ![Logo](https://raw.githubusercontent.com/li-haoyang1/CrowdOS/master/src/site/resources/images/logo.jpg)

CrowdOS is a ubiquitous operating system for [Crowdsoucring](https://en.wikipedia.org/wiki/Crowdsourcing) and
[Mobile Crowdsensing](https://en.wikipedia.org/wiki/Crowdsensing), which can deal with multiple types of 
crowdsourcing problems simultaneously.

## Installation and Getting Started

CrowdOS are available on Maven Central.

If you use Maven or Gradle, add a dependency with following coordinates to your build script:
```xml
<dependencies>
    <dependency>
        <groupId>cn.crowdos</groupId>
        <artifactId>crowdos-kernel</artifactId>
        <version>1.0.3</version>
    </dependency>
</dependencies>
```
You can use the features provided by CrowdOS in any way you want, but if you want to develop a springboot application, 
you only need the following few simple steps:
1. You need a _CrowdKernelComponent.java_ like this one, which is used by the Spring Framework.
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
2. You need to create the Participant class as you want, you should either implement the _Participant_ interface, 
or inherit directly from the_AbstractParticipant_ class.
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
3. You need to create the task class as you want, and you should either implement the _Task_ interface 
or inherit directly from the _AbstractTask_ class or any other task class we provide.
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
4. To use the features provided by the CrowdOS kernel, you should first register participants and submit tasks.
```
// use 
crowdKernelComponent.getKernel().registerParticipant(user);
// and
crowdKernelComponent.getKernel().submitTask(task);
```
5. Do everything you want.

This is a simple demo of a Springboot application powered by CrowdOS ([CrowdOS-demo](https://github.com/loyx/CrowdOS-demo)).
and [WeSense]() is an actual running application, it is based on CrowdOS and you can download it from the app store 
on Android and IOS.

## Getting Help
if you have any trouble with CrowdOS, the following may be of help to you.
- Visit our website [www.crowdos.cn](https://www.crowdos.cn/).
- Check the [reference documentation](https://crowdos.cn/crowdos_doc/public/apidocs/).
- Or email us: _crowdos_nwpu@163.com_

## Reporting Issue
CrowdOS uses GitHub's integrated issue tracking system to record bugs and feature requests. 
If you want to raise an issue, please follow the recommendations below:
- Before you log a bug, please search the issue tracker to see if someone has already reported the problem.
- If the issue doesn't already exist, create a new issue.
- Please provide as much information as possible with the issue report. We like to know the CrowdOS version, 
operating system, and JVM version you’re using.
- If you need to paste code or include a stack trace, use Markdown. ``` escapes before and after your text.
- If possible, try to create a test case or project that replicates the problem and attach it to the issue.
## Modules
**Note: CrowdOS is still in the development stage, only the kernel part is completed at present.**
### kernel
crowdos-kernel. The main library providing features that support the other parts of CrowdOS kernel.
#### kernel.algorithms
**algorithms** contains three algorithms used by **Scheduler**. this part will be moved to crowdos-aaas feature.
#### kernel.Constraint
Contains some constraints to used when construct a task.
#### kernel.resource
Contains participant and task.
#### kernel.system
Provide some way to access system resources.
### ALGO
crowdos-aaas(CrowdOS Algorithms as a Service). Coming soon.
### DataService
crowdos-dataservice. Coming soon.
### SimulationSystem
Coming soon.

## Guides
The [www.crowdos.cn](https://www.crowdos.cn/) site contains some detail information of CrowdOS.
## License
The CrowdOS Project is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
