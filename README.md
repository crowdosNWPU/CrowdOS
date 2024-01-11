 [![license](https://img.shields.io/github/license/crowdosNWPU/CrowdOS)](https://www.apache.org/licenses/LICENSE-2.0) [![build](https://img.shields.io/github/actions/workflow/status/crowdosNWPU/CrowdOS/maven.yml)]()  [![issues](https://img.shields.io/github/issues/crowdosNWPU/CrowdOS)]()
 [![fork](https://img.shields.io/github/forks/crowdosNWPU/CrowdOS?style=social)]() [![stars](https://img.shields.io/github/stars/crowdosNWPU/CrowdOS?style=social)]()
 
 
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
### FrameWork of the system
![FrameWork](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/FrameWork.png)  
### Kernel
The goal of CrowdOS is to improve the construction efficiency of MCS applications and reduce the usage rights of MCS applications. 
Currently, CrowdOS uses Web frameworks such as CrowdOS Kernel and SpringBoot to quickly develop crowd intelligence APPs based on crowd intelligence collection functions. 
The framework diagram of a crowd intelligence APP developed using CrowdOS Kernel is as follows:  
![img1-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img1-1.png)  
#### Kernel.constriaint and Kernel.resource
##### 1、Design Logic
The two key factors in crowd sensing applications are the task and the participants who complete the task.  
In the CrowdOS Kernel, a task is defined as a developer-defined **task** (resource package) that contains a set of **constraints** (constraint package)，
Participant is a developer-defined **participant** that contains a set of capabilities (resource package).  
Task (actually, it is a programmer-defined entity class that implements the Task interface) uses the method 
**canAssignTo()** to detect whether the task can be assigned to a participant Participant 
(actually, it is a programmer-defined entity class that implements the Participant interface).   
Each constraint (actually, it is a programmer-defined entity class that implements the Constraint interface) uses the method **satisfy()** to detect whether a certain condition (actually, 
it is a programmer-defined entity class that implements the Condition interface) is satisfied. own requirements.  
The necessary and sufficient conditions for whether a task can be assigned to a participant are: 
all constraints of the task must be satisfied by the participant's ability (in current terminology, 
the participant's ability) ) has the same meaning as the condition to satisfy the constraint). 
In the specific implementation, the method **canAssignTo()** will do two checks:
- Check whether the participant has the ability to specifically satisfy a certain type of constraint required by the task. For example, if the task requires performing a task in a certain area, 
it is necessary to insist whether the participants provide their own GPS information.
- Check whether a certain ability possessed by the participant is a condition that satisfies the constraint. 
For example, check whether the participant's GPS location information is within the range required by the task.  


The design logic diagram is as follows:  
![img2-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img2-1.png)  
##### 2、Design Patterns
The classes in the constraint package and the resource package implement a Double Dispatch mode (another commonly used implementation of double dispatch is the Visitor mode).At the same time, some reflection techniques are used to optimize the code implementation. 
The specific class diagram is as follows:  
![img3-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img3-1.png)  
##### 3、Other Content
The constraint package contains the Constraint interface of the build task and the Condition interface of the build participant, and provides some simple implementations. 
In addition, in the constraint.wrapper package, a Condition version of the underlying type is provided (same logic as the JAVA wrapper class).  
![img5-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img5-1.png)  
In addition to the key Task and Participant interfaces, the resource package also provides corresponding Abstract Classes, 
namely AbstractTask and AbstractParticipant. When programmers develop custom tasks and participants, 
they only need to inherit the corresponding abstract base class instead of starting from implementing the basic interface 
(same design idea as the design idea of the JAVA Container part). 
Some example entity classes are also provided.  
![img4-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img4-1.png)  
#### Kernel.system
##### 1、Design Logic
SystemResourceCollection in the system package manages all entities in the system. 
Currently, system entities include **TaskPool, ParticipantPool, AlgoContainer and Schedule**. System entities need to inherit the **Resource** interface and implement the **getHandler()** method. 
For the protection of system entities, all operations accessing system entities should go through the **getHandler()** method, which returns the processing handle of a specific entity. 
When other packages access system entities, they must pass the entity handle. System entity handle SystemResourceHandler<T> provides two types of access methods:
- **T getResourceView()**: access the unchangeable view of the entity;
- **T getResource()**: access the entity itself.
  The system package makes a convention for other packages (or codes): when other packages (or other codes) use getResourceView(), 
the system package ensures that the system functions normally (for example, scheduling system, task pool management, participant pool management, etc. ); 
when using getResource(), the system package does not provide this guarantee.  
  Based on the guarantees provided by the system package, other packages can safely implement their own functions through system entity handles. 
- For example, in the algorithms package, the implementation of the algorithm requires access to various information of system entities, so all operations in this package can only use getResourceView().

##### 2、Design Patterns
The system package implements a guarantee similar to the iteration pattern. The specific UML class diagram is as follows:  
![img6-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img6-1.png)  
#### Kernel.algorithms
##### 1、Design Logic
The algorithms package defines crowdsensing-related algorithms used in the system, and currently provides task allocation, 
task recommendation, and participant selection algorithm interfaces. 
The algorithms package realizes the decoupling of algorithms and system processes. 
In the process of implementing the algorithm in the algorithms package, based on the guarantee provided by the system package, the algorithm process and the system process can be decoupled to ensure the stable operation of the system process.
##### 2、Design Patterns
There are two main design patterns involved in the algorithms package, namely:
- The algorithms themselves use engineering mode. Each type of algorithm factory produces a specific type of algorithm implementation. 
Currently, each algorithm factory needs to implement task allocation, task recommendation and participant selection algorithms separately;
- When algorithms interact with the Scheduler, the algorithms are embedded in the Scheduler in the form of template patterns.

The UML class diagram of the algorithms package is as follows:  
![img7-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img7-1.png)  
##### 3、Algorithm Description
The lgorithms package provides four classic task allocation algorithms, namely T_Most, PT_Most, T_Random, and GGA_I.
The algorithm factories corresponding to the four algorithms all inherit from the algorithm adapter AlgoFactoryAdapter. Each algorithm can support single task allocation and multi-task allocation.  
The interface AlgoFactory defines the interfaces of all algorithms used in the kernel. Currently, three functions are defined:  
![img8-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img8-1.png)  
The algorithm adapter AlgoFactoryAdapter implements the interface AlgoFactory and provides the system with default task allocation, 
task recommendation and participant selection algorithms. 
If no algorithm selection is performed, the system provides the default algorithm implementation. Access to specific algorithms can be achieved by inheriting the algorithm adapter AlgoFactoryAdapter.
#### CrowdKernel system interface and Implementation
##### 1、CrowdKernel Interface
The interface CrowdKernel defines the interface for programmers to interact with kernel functions. Currently CrowdKernel defines the following functions:  
![img9-1.png](https://raw.githubusercontent.com/crowdosNWPU/CrowdOS/main/src/site/resources/images/img9-1.png)  
### ALGO
crowdos-aaas(CrowdOS Algorithms as a Service).
Coming soon.
### DataService
crowdos-dataservice.
coming soon.
### SimulationSystem
coming soon.


## Guides
[www.crowdos.cn](https://www.crowdos.cn/)The website contains some detailed information about CrowdOS.
## Licence
The CrowdOS project is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0)。
## OpenAtom Foundation
The CrowdOS project has joined the OpenAtom Foundation. Project address:https://atomgit.com/transcend/CrowdOS
