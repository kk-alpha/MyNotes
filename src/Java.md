## 运行时区域

​	程序计数器、Java虚拟机栈（VM Stack）、本地方法栈（Native Method Stack）、Java堆、方法区

### 1. 程序计数器

+ 1.1 线程私有

### 2. Java虚拟机栈

+ 2.1 线程私有，生命周期与线程相同
+ 2.2描述的是**Java方法执行**的**线程内存模型**
  + 方法被执行时，Java虚拟机会同步创建一个栈帧（Stack Frame）用于存储局部变量表、操作数栈、动态连接、方法出口等信息
  + 方法被调用直至执行完毕的过程，对应着一个栈帧在虚拟机栈中从入栈到出栈的过程

### 3. 本地方法栈

### 4. Java堆

+ 被所有线程共享的一块内存区域，虚拟机启动时创建
+ 此区域的唯一目的就是存放对象实例，几乎所有的对象实例都在这里分配内存
+ Java堆是垃圾收集器管理的内存区域，也被称为“GC堆”

### 5.方法区

+ 各个线程共享的内存区域
+ 用于存储已被虚拟机加载的类型信息、常量、静态变量、即时便一起编译后的代码缓存等数据





### Handler

+ 传递消息Message

+ 子线程通知主线程更新UI

+ 非主线程没有loop对象，所以要调用Looper.prepare()方法，而且如果主线程给子线程发送消息，还要调用一个Looper.loop（）的方法(此方法保证消息队列中的消息被不停的拿出，并被处理)

  + ```java
    class MyThread extends Thread{
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        //处理消息
                    }
                };
                Looper.loop();
            }
    }
    ```

+ activity如被finish，但是handler刚好还在处理消息，如果需要用的资源已被释放，则会出现空指针异常。

  所以在ondestory中去remove掉我们要处理的事件，还是有必要的。不想处理就直接try catch或者判空。

  































