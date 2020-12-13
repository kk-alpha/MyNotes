# 1. WebView
用于显示网页的控件：WebView(网页视图)。
## 1.1 相关类
+ WebChromeClient：辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
+ WebViewClient：辅助WebView处理各种通知与请求事件
+ WebSettings：WebView相关配置的设置，比如setJavaScriptEnabled()设置是否允许JS脚本执行

## 1.2 Error
### 1. 在Android中使用WebView加载非https链接时，出现错误：net::ERR_CLEARTEXT_NOT_PERMITTED
+ 原因:从Android 9.0（API级别28）开始，默认情况下禁用明文支持。因此http的url均无法在webview中加载，Android9.0对未加密的流量不再信任，添加了新的限制。
+ 创建文件 res/xml/network_security_config.xml 
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <domain-config cleartextTrafficPermitted="true">
            <domain includeSubdomains="true">example.com(to be adjusted)</domain>
        </domain-config>
    </network-security-config>
    ```
    替换example.com(to be adjusted）为自己的域名，根域名即可
+ 修改AndroidManifest：
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <manifest ...>
        <uses-permission android:name="android.permission.INTERNET" />
        <application
            ...
            android:usesCleartextTraffic="true"
            android:networkSecurityConfig="@xml/network_security_config"
            ...>
            ...
        </application>
    </manifest>
    ```
+ 检查网络访问权限，修改 AndroidManifest.xml
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <manifest android:targetSandboxVersion="1">
        <uses-permission android:name="android.permission.INTERNET" />
        ...
    </manifest>
    ```
### 2.android webview加载网页错误net::ERR_UNKNOWN_URL_SCHEME
+ 网页地址或其重定向地址不是"http"或"https"开头的，而是像这样的"hwfastapp://"等，如果WebViewClient错误重写了以下方法，便会报错net::ERR_UNKNOWN_URL_SCHEME。
    ```
    @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    ```
+ 解决方法简单粗暴：不用重写该方法，如果不是必要的，可以不必重写任何方法

## 获取WebView的cookie数据
Cookie只是一个代表用户唯一标识的字符串，情景一般是： 用户输入账号密码后，点击登陆，用户要拿着这个Cookie去访问服务器提供的相关服务！ 我们可以把cookie的获取写到onPageFinsihed的方法中
```
@Override
public void onPageFinished(WebView view, String url) {             
    CookieManager cookieManager = CookieManager.getInstance();
    String CookieStr = cookieManager.getCookie(url);
    Log.e("HEHE", "Cookies = " + CookieStr);
    super.onPageFinished(view, url);
}
```
## 设置WebView的cookie数据
加入以下代码
```
CookieSyncManager.createInstance(MainActivity.this);  
CookieManager cookieManager = CookieManager.getInstance();  
cookieManager.setAcceptCookie(true);  
cookieManager.setCookie(url, cookies);  //cookies是要设置的cookie字符串 
CookieSyncManager.getInstance().sync();
```
需要写在loadUrl（）之前，而且如果设置了Cookie了，尽量别再进行其他的设置 不然可能会无效，建议设置cookie的写在webView相关设置的最后面~loadUrl（）之前


## Android APP禁止旋转和软键盘的控制
AndroidManifest.xml中修改
```
<!-- screenOrientation   portrait  限制此页面数竖屏显示 -->
<!-- screenOrientation   landscape  限制此页面横屏显示 -->
<!-- windowSoftInputMode   stateUnspecified：软键盘的状态并没有指定，系统将选择一个合适的状态或依赖于主题的设置 -->
<!-- windowSoftInputMode   stateUnchanged：当这个activity出现时，软键盘将一直保持在上一个activity里的状态，无论是隐藏还是显示 -->
<!-- windowSoftInputMode   stateHidden：用户选择activity时，软键盘总是被隐藏 -->
<!-- windowSoftInputMode   stateAlwaysHidden：当该Activity主窗口获取焦点时，软键盘也总是被隐藏的 -->
<!-- windowSoftInputMode   stateVisible：软键盘通常是可见的 -->
<!-- windowSoftInputMode   stateAlwaysVisible：用户选择activity时，软键盘总是显示的状态 -->
<!-- windowSoftInputMode   adjustUnspecified：默认设置，通常由系统自行决定是隐藏还是显示 -->
<!-- windowSoftInputMode   adjustResize：该Activity总是调整屏幕的大小以便留出软键盘的空间 -->
<!-- windowSoftInputMode   adjustPan：当前窗口的内容将自动移动以便当前焦点从不被键盘覆盖和用户能总是看到输入内容的部分 -->

<activity
    android:name=".MainActivity"
    android:screenOrientation="portrait"
    android:windowSoftInputMode="stateHidden|stateUnchanged" />
```

# 2. Android网络编程 
## 2.1 HTTP 协议
### 2.1.1 HTTP 协议主要特点

+ 支持C/S（客户/服务器）模式。
+ 简单快速：客户向服务器请求服务时，只需传送请求方法和路径。请求方法常用的有GET、HEAD、POST，每种方法规定了客户与服务器联系的类型不同。由于HTTP协议简单，使得HTTP服务器的程序规模小，因而通信速度很快。
+ 灵活：HTTP允许传输任意类型的数据对象。正在传输的类型由Content-Type加以标记。
+ 无连接：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后，即断开连接。采用这种方式可以节省传输时间。
+ 无状态：HTTP协议是无状态协议，无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要前面的信息，则它必须重传，这样可能导致每次连接传送的数据量增大。另一方面，在服务器不需要先前信息时它的应答就较快。

### 2.1.2 HTTP URL格式

+ ```
    http://host[":"port][abs_path]
    ```

+ http表示要通过HTTP协议来定位网络资源；host表示合法的Internet主机域名或者IP地址；port指定一个端口号，为空则使用默认端口80；abs_path指定请求资源的URI（Web上任意的可用资源）。

## 2.2 HTTP 协议请求报文

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\Vuw5hn.png)

通常来说一个HTTP请求报文由请求行、请求报头、空行、和请求数据4个部分组成。

### 2.2.1 请求行

+ ```
    Method Request-URI HTTP-Version CRLF
    ```

+ 其中 Method表示请求方法；Request-URI是一个统一资源标识符；HTTP-Version表示请求的HTTP协议版本；CRLF表示回车和换行（除了作为结尾的CRLF外，不允许出现单独的CR或LF字符）。

+ HTTP请求方法有8种，分别是GET、POST、DELETE、PUT、HEAD、TRACE、CONNECT 、OPTIONS. 移动开发中最常用的就是GET和POST。

    + GET：请求获取Request-URI所标识的资源
    + POST：在Request-URI所标识的资源后附加新的数据
    + HEAD：请求获取由Request-URI所标识的资源的响应消息报头
    + PUT：     请求服务器存储一个资源，并用Request-URI作为其标识
    + DELETE ：请求服务器删除Request-URI所标识的资源
    + TRACE  ： 请求服务器回送收到的请求信息，主要用于测试或诊断
    + CONNECT： HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。
    + OPTIONS ：请求查询服务器的性能，或者查询与资源相关的选项和需求

### 2.2.2 请求报头

+ 在请求行之后会有0个或者多个请求报头，每个请求报头都包含一个名字和一个值，它们之间用“：”分割。请求头部会以一个空行，发送回车符和换行符，通知服务器以下不会有请求头。

### 2.2.3 请求数据

+ 请求数据不在GET方法中使用，而是在POST方法中使用。POST方法适用于需要客户填写表单的场合，与请求数据相关的最常用的请求头是Content-Type和Content-Length。

## 2.3 HTTP 协议响应报文

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\Vuw4ts.png)

HTTP的响应报文由状态行、消息报头、空行、响应正文组成。响应报头后面会讲到，响应正文是服务器返回的资源的内容

### 2.3.1  状态行

+ 状态行格式：

+ ``` 
  HTTP-Version Status-Code Reason-Phrase CRLF
  ```

+ 其中，HTTP-Version表示服务器HTTP协议的版本；Status-Code表示服务器发回的响应状态代码；Reason-Phrase表示状态代码的文本描述。

+ 状态代码有三位数字组成，第一个数字定义了响应的类别，且有五种可能取值：

  + 100~199：指示信息，表示请求已接收，继续处理
  + 200~299：请求成功，表示请求已被成功接收、理解、接受
  + 300~399：重定向，要完成请求必须进行更进一步的操作
  + 400~499：客户端错误，请求有语法错误或请求无法实现
  + 500~599：服务器端错误，服务器未能实现合法的请求

# 3.四大组件

## 3.1 Activity

### 3.1.1 Activity 生命周期

![activity](/Users/kai/GitDocuments/src/Androiddddd/pictures/activity.png)

+ **attention**
  
+  #### onStart() & onStop()、onResume()  & onPause() 除了回调时刻，在实际使用中无任何区别
  
     + onStart()& onStop() ：从Activity是否完全可见的角度 进行回调;
   + onResume() & onPause()： 从 Activity`是否位于前台（UI最顶层）的角度进行回调；
  
+  #### 当前Activity为A，此时用户打开ActivityB后，那么A的onPause（）和B的onResume()哪个方法先执行？
  
     + 先A的onPause（），然后B的onResume（）
     + Activity的启动过程：由ActivityManagerService（AMS）对栈内的Activity状态进行同步管理 & 规定：**新Activity启动前，栈顶的Activity必须先onPause（），才能启动新的Activity（执行onResume()）**
     + 为了让新的Activity尽快切换到前台，在 onPause()尽量不要做耗时 / 重量级操作

### 3.1.2 常见场景的生命周期调用方式

### <img src="/Users/kai/GitDocuments/src/Androiddddd/pictures/activity-2.png" alt="activity-2" style="zoom:150%;" />

+ **暂停Activity**

  + 当系统调用Activity中的onPause(),从技术上讲，意味着Activity仍然处于部分可见的状态。但更多时候意味着用户正在离开这个Activity，并马上会进入Stopped state。通常应该在onPause()回调方法中做以下事情

    + 停止动画或者是其他正在运行的操作，那些都会导致CPU的浪费.
    + 提交在用户离开时期待保存的内容(例如邮件草稿).
    + 释放系统资源，例如broadcast receivers, sensors (比如GPS), 或者是其他任何会影响到电量的资源。

  +  如果程序使用Camera,onPause()会是一个比较好的地方去做那些释放资源的操作

    + ``` java
      @Override
      public void onPause() {
          super.onPause();  // Always call the superclass method first
          // Release the Camera because we don't need it when paused
      // and other activities might need to use it.
          if (mCamera != null) {
              mCamera.release()
              mCamera = null;
          }
      }
      ```

  + 不应该使用onPause()来保存用户改变的数据 (例如填入表格中的个人信息)  到永久存储(File或者DB)上。仅仅当确认用户期待那些改变能够被自动保存的时候(例如正在撰写邮件草稿)，才把那些数据存到永久存储  。但是，我们应该避免在onPause()时执行CPU-intensive  的工作，例如写数据到DB，因为它会导致切换到下一个activity变得缓慢(应该把那些heavy-load的工作放到onStop()去做)。

+ **恢复Activity**

  + 当用户从Paused状态恢复activity时，系统会调用onResume()方法。

    请注意，系统每次调用这个方法时，activity都处于前台，包括第一次创建的时候。所以，应该实现onResume()来初始化那些在onPause方法里面释放掉的组件，并执行那些activity每次进入Resumed state都需要的初始化动作 (例如开始动画与初始化那些只有在获取用户焦点时才需要的组件)

    + 下面的onResume()的例子是与上面的onPause()例子相对应的。

    + ```  java
      @Override
      public void onResume() {
          super.onResume();  // Always call the superclass method first
      
          // Get the Camera instance as the activity achieves full user focus
          if (mCamera == null) {
              initializeCamera(); // Local method to handle camera init
          }
      }
      ```

+ **停止与重启Activity**

  + 在下面一些关键的场景中会涉及到停止与重启
    + 用户打开最近使用app的菜单并从我们的app切换到另外一个app，这个时候我们的app是被停止的。如果用户通过手机主界面的启动程序图标或者最近使用程序的窗口回到我们的app，那么我们的activity会重启。
    + 用户在我们的app里面执行启动一个新activity的操作，当前activity会在第二个activity被创建后stop。如果用户点击back按钮，第一个activtiy会被重启。
    + 用户在使用我们的app时接收到一个来电通话。
  + Activity类提供了onStop()与onRestart()方法来允许在activity停止与重启时进行调用。不同于暂停状态的部分阻塞UI，停止状态是UI不再可见并且用户的焦点转移到另一个activity中.
  + 当用户离开我们的activity时，系统会调用onStop()来停止activity.  这个时候如果用户返回，系统会调用onRestart(), 之后会迅速调用onStart()与onResume().  请注意：无论什么原因导致activity停止，系统总是会在onStop()之前调用onPause()方法。

+ **停止Activity**

  + 当activity调用onStop()方法,  activity不再可见，并且应该释放那些不再需要的所有资源。一旦activity停止了，系统会在需要内存空间时摧毁它的实例(和栈结构有关，通常back操作会导致前一个activity被销毁)。**极端情况下，系统会直接杀死我们的app进程，并不执行activity的onDestroy()回调方法, 因此我们需要使用onStop()来释放资源，从而避免内存泄漏。**(这点需要注意)

  + 尽管onPause()方法是在onStop()之前调用，我们应该**使用onStop()来执行那些CPU intensive的shut-down操作**，例如往数据库写信息。

  + 下面是一个在onStop()的方法里面保存笔记草稿到persistent storage的示例:

    ```java
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
    
        // Save the note's current draft, because the activity is stopping
        // and we want to be sure the current note progress isn't lost.
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_NOTE, getCurrentNoteText());
        values.put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle());
    
        getContentResolver().update(
                mUri,    // The URI for the note to update.
                values,  // The map of column names and new values to apply to them.
                null,    // No SELECT criteria are used.
                null     // No WHERE columns are used.
                );
    }
    ```

  + activity已经停止后，Activity对象会保存在内存中，并在activity resume时被重新调用。我们不需要在恢复到Resumed state状态前重新初始化那些被保存在内存中的组件。系统同样保存了每一个在布局中的视图的当前状态，如果用户在EditText组件中输入了text，它会被保存，因此不需要保存与恢复它。

  + 即使系统会在activity stop时停止这个activity，它仍然会保存View对象的状态(比如EditText中的文字) 到一个Bundle中，并且在用户返回这个activity时恢复它们

+ **启动与重启Activity**

  + 当activity从Stopped状态回到前台时，它会调用onRestart().系统再调用onStart()方法，onStart()方法会在每次activity可见时都会被调用。onRestart()方法则是只在activity从stopped状态恢复时才会被调用，因此我们可以使用它来执行一些特殊的恢复(restoration)工作，请注意之前是被stopped而不是destrory。

  + 因为onStop()方法应该做清除所有activity资源的操作，**我们需要在重启activtiy时重新实例化那些被清除的资源，同样,  我们也需要在activity第一次创建时实例化那些资源。**介于上面的原因，应该使用onStart()作为onStop()所对应方法。因为系统会在创建activity与从停止状态重启activity时都会调用onStart()。也就是说，**我们在onStop里面做了哪些清除的操作，就该在onStart里面重新把那些清除掉的资源重新创建出来。**

  + 例如：因为用户很可能在回到这个activity之前已经过了很长一段时间，所以onStart()方法是一个比较好的地方来验证某些必须的系统特性是否可用。

    ```java
    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
    
        // The activity is either being restarted or started for the first time
        // so this is where we should make sure that GPS is enabled
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    
        if (!gpsEnabled) {
            // Create a dialog here that requests the user to enable GPS, and use an intent
            // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
            // to take the user to the Settings screen to enable GPS when they click "OK"
        }
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
    
        // Activity being restarted from stopped state
    }
    ```

  + 因为我们会在**onStop方法里面做释放资源**的操作，那么**onDestory方法**则是我们最后去**清除那些可能导致内存泄漏的地方**。因此需要确保那些线程都被destroyed并且所有的操作都被停止。

+ **重新创建Activity**

  + 例：你的Activity会在每次旋转屏幕时被destroyed与recreated。当屏幕改变方向时，系统会Destory与Recreate前台的activity，因为屏幕配置被改变，你的Activity可能需要加载另一些替代的资源(例如layout). 

  + 默认情况下, **系统使用 Bundle 实例来保存每一个View(视图)对象中的信息(例如输入EditText  中的文本内容)。**因此，如果Activity被destroyed与recreated,  则layout的状态信息会自动恢复到之前的状态。然而，activity也许存在更多你想要恢复的状态信息，例如记录用户Progress的成员变量(member variables)。

  + **为了使Android系统能够恢复Activity中的View的状态，每个View都必须有一个唯一ID，由android:id定义。** 

  + 为了可以保存额外更多的数据到saved instance  state。在Activity的生命周期里面存在一个额外的回调函数，你必须重写这个函数。这个方法是**onSaveInstanceState()** ，当用户离开Activity时，系统会调用它。当系统调用这个函数时，系统会在Activity被异常Destory时传递 Bundle  对象，这样我们就可以增加额外的信息到Bundle中并保存到系统中。若系统在Activity被Destory之后想重新创建这个Activity实例时，之前的Bundle对象会(系统)被传递到你我们activity的**onRestoreInstanceState()**方法与 onCreate() 方法中。 

  + 创建Activity之后翻转屏幕  Activity生命周期方法调用过程：

    ![activity3](/Users/kai/GitDocuments/src/Androiddddd/pictures/activity3.png)

    

### 3.1.3  Activity与Fragment生命周期对比

![activity-fragment](/Users/kai/GitDocuments/src/Androiddddd/pictures/activity-fragment.png)

### 3.1.4 Activity Task的管理

我们的APP一般都是由多个Activity构成的，而在Android中给我们提供了一个**Task(任务)**的概念， 就是将多个相关的Activity收集起来，然后进行Activity的跳转与返回！当然，这个Task只是一个 frameworker层的概念，而在Android中实现了Task的数据结构就是**Back Stack（回退堆栈）**！

当切换到新的Activity，那么该Activity会被压入栈中，成为栈顶！ 而当用户点击Back键，栈顶的Activity出栈，紧随其后的Activity来到栈顶！

**只需要通过修改AndroidManifest.xml中 < activity >的相关属性值或者在代码中通过传递特殊标识的Intent给startActivity( )就可以轻松的实现 对Actvitiy的管理了。**

+ ManagingTasks

  + **<activity>中可以使用的属性**

    + **taskAffinity**

    + **launchMode**

    + **allowTaskReparenting**

    + **clearTaskOnLaunch**

    + **alwaysRetainTaskState**

    + **finishOnTaskLaunch**

  + **能用的主要的Intent标志有：**

    + **FLAG_ACTIVITY_NEW_TASK**

    + **FLAG_ACTIVITY_CLEAR_TOP**

    + **FLAG_ACTIVITY_SINGLE_TOP**

+ taskAffinity和allowTaskReparenting

  + 默认情况下，一个应用程序中的**所有activity都有一个Affinity**，这让它们属于同一个Task。 你可以理解为是否处于同一个Task的标志，然而，每个Activity可以通过 < activity>中的taskAffinity属性设置单独的Affinity。 不同应用程序中的Activity可以共享同一个Affinity，同一个应用程序中的不同Activity 也可以设置成不同的Affinity。

+ launchMode

  Activity四种加载模式

  + standard（默认）
  + singleTop
  + singleTask
  + singleInstance

![activity-launchMode](/Users/kai/GitDocuments/src/Androiddddd/pictures/activity-launchMode.png)

## 3.2 Service

### 3.2.1 线程相关

+ 概念

  + **程序**

    为了完成特定任务，用某种语言编写的一组指令集和（一组静态代码）

  + **进程**

    **运行中的程序**，系统调度与资源分配的一个**独立单位**，操作系统会 为每个进程分配一段内存空间！程序的依次动态执行，经历代码的加载，执行， 执行完毕的完整过程！

  + **线程**

    比进程更小的执行单元，每个进程可能有多条线程，**线程**需要放在一个 **进程**中才能执行，**线程由程序**负责管理，而**进程则由系统**进行调度！

  + 多线程

    **并行**执行多个条指令，将**CPU时间片**按照调度算法分配给各个 线程，实际上是**分时**执行的，只是这个切换的时间很短，用户感觉到"同时"而已！

+ **线程生命周期**

  ![service-thread](/Users/kai/GitDocuments/src/Androiddddd/pictures/service-thread.png)

+ **创建线程的三种方式**

  + 继承Thread类
  + 实现Runnable接口
  + 实现Callable接口

### 3.2.2 Service 生命周期

![service2](/Users/kai/GitDocuments/src/Androiddddd/pictures/service2.png)

+ **相关方法**
  + onCreate()：当Service第一次被创建后立即回调该方法，该方法在整个生命周期 中只会调用一次！
  + onDestroy()：当Service被关闭时会回调该方法，该方法只会回调一次！
  + onStartCommand(intent,flag,startId)：早期版本是onStart(intent,startId), 当客户端调用startService(Intent)方法时会回调，可多次调用StartService方法， 但不会再创建新的Service对象，而是继续复用前面产生的Service对象，但会继续回调 onStartCommand()方法！
  + IBinder onOnbind(intent)：该方法是Service都必须实现的方法，该方法会返回一个 IBinder对象，app通过该对象与Service组件进行通信！
  + onUnbind(intent)：当该Service上绑定的所有客户端都断开时会回调该方法！

+ StartService()启动Service

  + **①**首次启动会创建一个Service实例,依次调用onCreate()和onStartCommand()方法,此时Service 进入运行状态,如果再次调用StartService启动Service,将不会再创建新的Service对象, 系统会直接复用前面创建的Service对象,调用它的onStartCommand()方法！

  + **②**但这样的Service与它的调用者无必然的联系,就是说当调用者结束了自己的生命周期, 但是只要不调用stopService,那么Service还是会继续运行的! 

  + **③**无论启动了多少次Service,只需调用一次StopService即可停掉Service

+ BindService()启动Service

+ StartService启动Service后bindService绑定

  + 如果Service已经由某个客户端通过StartService()启动,接下来由其他客户端 再调用bindService(）绑定到该Service后调用unbindService()解除绑定最后在 调用bindService()绑定到Service的话,此时所触发的生命周期方法如下:
     **onCreate( )->onStartCommand( )->onBind( )->onUnbind( )->onRebind( )**
     **PS:**前提是:onUnbind()方法返回true!!! 这里或许部分读者有疑惑了,调用了unbindService后Service不是应该调用 onDestory()方法么!其实这是因为这个Service是由我们的StartService来启动的 ,所以你调用onUnbind()方法取消绑定,Service也是不会终止的!
     **得出的结论:** 假如我们使用bindService来绑定一个启动的Service,注意是已经启动的Service!!! 系统只是将Service的内部IBinder对象传递给Activity,并不会将Service的生命周期 与Activity绑定,因此调用unBindService( )方法取消绑定时,Service也不会被销毁

### 3.2.3 IntentService

如果我们直接把 耗时线程放到Service中的onStart()方法中，虽然可以这样做，但是很容易 会引起ANR异常(Application Not Responding)

1.Service不是一个单独的进程,它和它的应用程序在同一个进程中
 2.Service不是一个线程,这样就意味着我们应该避免在Service中进行耗时操作

IntentService是继承与Service并处理异步请求的一个类,在IntentService中有 一个工作线程来处理耗时操作,请求的Intent记录会加入队列

**工作流程：**

> 客户端通过startService(Intent)来启动IntentService; 我们并不需要手动地区控制IntentService,当任务执行完后,IntentService会自动停止; 可以启动IntentService多次,每个耗时操作会以工作队列的方式在IntentService的 onHandleIntent回调方法中执行,并且每次只会执行一个工作线程,执行完一，再到二这样!

​	

## 3.3 BroadcastReceiver

### 3.3.1  接收系统广播

+ 注册广播

  ![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\broadcast1.png)

+ 动态注册

### 3.3.x  出现的版本问题

+ 发送标准广播时收不到广播

  + 8.0后广播在AndroidManifest.xml中注册后发送intent是接收不到广播了，看了一下原因，好像是8.0为了管理系统和节约电量特别针对广播和服务发送intent的方式启动做出的改变，也就是说广播和服务不能随意收intent了，要对广播和服务更精确的指向，所以在创建intent的时候，我们需要指定我们的广播和服务的包名加类名，为的就是精确

  + ```java
    public class MainActivity extends AppCompatActivity {
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //只需在建立intent后调用setComponent((new ComponentName(“包名”,“接收器类名”) )即可。AndroidManifest.xml中的静态注册也可以不指定action
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.youyu4", "com.example.youyu4.MyBroadcastReceiver"));//参数为包名和类名，注意类名中要包括包名
                    sendBroadcast(intent);
                }
            });
        }
    }
    
    ```


## 3.4 ContentProvider



### 3.4.1 作用

+ 进程间 进行**数据交互 & 共享**，即跨进程通信

+ ![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\contentprovider.png)

### 3.4.2 具体使用

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\content2.png)

+ **统一资源标识符（URI）**

  + 定义：`Uniform Resource Identifier`，即统一资源标识符

  + 作用：唯一标识 `ContentProvider` & 其中的数据

  + 外界进程通过 `URI` 找到对应的`ContentProvider`  & 其中的数据，再进行数据操作

  + `URI`分为 系统预置 & 自定义，分别对应系统内置的数据（如通讯录、日程表等等）和自定义数据库

  + 自定义URI

    + ![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\自定义URI.png)

    + ```java
      // 设置URI
      Uri uri = Uri.parse("content://com.carson.provider/User/1") 
      // 上述URI指向的资源是：名为 `com.carson.provider`的`ContentProvider` 中表名 为`User` 中的 `id`为1的数据
      
      // 特别注意：URI模式存在匹配通配符* & ＃
      
      // *：匹配任意长度的任何有效字符的字符串
      // 以下的URI 表示 匹配provider的任何内容
      content://com.example.app.provider/* 
      // ＃：匹配任意长度的数字字符的字符串
      // 以下的URI 表示 匹配provider中的table表的所有行
      content://com.example.app.provider/table/# 
      ```

+ **MIME数据类型**

  作用：指定某个扩展名的文件用某种应用程序来打开
   如指定`.html`文件采用`text`应用程序打开、指定`.pdf`文件采用`flash`应用程序打开

  + **ContentProvider根据 URI返回MIME类型**

    + ```java
      ContentProvider.geType(uri) ；
      ```

  + **MIME类型组成**

    + 由2部分组成 = 类型 + 子类型

    + ```
      text / html
      // 类型 = text、子类型 = html
      
      text/css
      text/xml
      application/pdf
      ```

+ **ContentProvider类**

  主要以**表格的形式**组织数据，也支持文件数据

  每个表格中包含多张表，每张表包含行 & 列，分别对应记录 & 字段

  + 主要方法

    + 进程间共享数据的本质是：添加、删除、获取 & 修改（更新）数据

    + 所以ContentProvider的核心方法也主要是上述4个作用

    + ```java
      <-- 4个核心方法 -->
        public Uri insert(Uri uri, ContentValues values) 
        // 外部进程向 ContentProvider 中添加数据
      
        public int delete(Uri uri, String selection, String[] selectionArgs) 
        // 外部进程 删除 ContentProvider 中的数据
      
        public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
        // 外部进程更新 ContentProvider 中的数据
      
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,  String sortOrder)　 
        // 外部应用 获取 ContentProvider 中的数据
      
      // 注：
        // 1. 上述4个方法由外部进程回调，并运行在ContentProvider进程的Binder线程池中（不是主线程）
       // 2. 存在多线程并发访问，需要实现线程同步
         // a. 若ContentProvider的数据存储方式是使用SQLite & 一个，则不需要，因为SQLite内部实现好了线程同步，若是多个SQLite则需要，因为SQL对象之间无法进行线程同步
        // b. 若ContentProvider的数据存储方式是内存，则需要自己实现线程同步
      
      <-- 2个其他方法 -->
      public boolean onCreate() 
      // ContentProvider创建后 或 打开系统后其它进程第一次访问该ContentProvider时 由系统进行调用
      // 注：运行在ContentProvider进程的主线程，故不能做耗时操作
      
      public String getType(Uri uri)
      // 得到数据类型，即返回当前 Url 所代表数据的MIME类型
      ```

    + ContentProvider类并不会直接与外部进程交互，而是通过ContentResolver 类

+ **ContentResolver类**

  + 作用：统一管理不同ContentProvider间的操作

    + 通过URI可以操作不同的ContentProvider中的数据
    + 外部进程通过ContentResolver类从而和ContentProvider类进行交互

  + ContentResolver类提供了与ContentProvider类相同名字和作用的四个方法：

    ```java
    // 外部进程向 ContentProvider 中添加数据
    public Uri insert(Uri uri, ContentValues values)　 
    
    // 外部进程 删除 ContentProvider 中的数据
    public int delete(Uri uri, String selection, String[] selectionArgs)
    
    // 外部进程更新 ContentProvider 中的数据
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)　 
    
    // 外部应用 获取 ContentProvider 中的数据
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    ```

  + 实例说明

    ```java
    // 使用ContentResolver前，需要先获取ContentResolver
    // 可通过在所有继承Context的类中 通过调用getContentResolver()来获得ContentResolver
    ContentResolver resolver =  getContentResolver(); 
    
    // 设置ContentProvider的URI
    Uri uri = Uri.parse("content://cn.scu.myprovider/user"); 
     
    // 根据URI 操作 ContentProvider中的数据
    // 此处是获取ContentProvider中 user表的所有记录 
    Cursor cursor = resolver.query(uri, null, null, null, "userid desc"); 
    ```

+ ContentUris类（下面三个都是ContentProvider的辅助工具类）

  + 作用：操作URI	

  + 核心方法两个：withAppendedId（）和parseId（）

    ``` java
    // withAppendedId（）作用：向URI追加一个id
    Uri uri = Uri.parse("content://cn.scu.myprovider/user") 
    Uri resultUri = ContentUris.withAppendedId(uri, 7);  
    // 最终生成后的Uri为：content://cn.scu.myprovider/user/7
    
    // parseId（）作用：从URL中获取ID
    Uri uri = Uri.parse("content://cn.scu.myprovider/user/7") 
    long personid = ContentUris.parseId(uri); 
    //获取的结果为:7
    ```

+ UriMatcher类（）

  + 作用:

    + ContentProvider中注册URI
    + 根据URI匹配ContentProvider中对应的数据表

  + 使用：

    ```java
    // 步骤1：初始化UriMatcher对象
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH); 
        //常量UriMatcher.NO_MATCH  = 不匹配任何路径的返回码
        // 即初始化时不匹配任何东西
    
    // 步骤2：在ContentProvider 中注册URI（addURI（））
        int URI_CODE_a = 1；
        int URI_CODE_b = 2；
        matcher.addURI("cn.scu.myprovider", "user1", URI_CODE_a); 
        matcher.addURI("cn.scu.myprovider", "user2", URI_CODE_b); 
        // 若URI资源路径 = content://cn.scu.myprovider/user1 ，则返回注册码URI_CODE_a
        // 若URI资源路径 = content://cn.scu.myprovider/user2 ，则返回注册码URI_CODE_b
    
    // 步骤3：根据URI 匹配 URI_CODE，从而匹配ContentProvider中相应的资源（match（））
    
    @Override   
        public String getType(Uri uri) {   
          Uri uri = Uri.parse(" content://cn.scu.myprovider/user1");   
    
          switch(matcher.match(uri)){   
         // 根据URI匹配的返回码是URI_CODE_a
         // 即matcher.match(uri) == URI_CODE_a
          case URI_CODE_a:   
            return tableNameUser1;   
            // 如果根据URI匹配的返回码是URI_CODE_a，则返回ContentProvider中的名为tableNameUser1的表
          case URI_CODE_b:   
            return tableNameUser2;
            // 如果根据URI匹配的返回码是URI_CODE_b，则返回ContentProvider中的名为tableNameUser2的表
        }   
    }
    ```

    ​	

+ ContentObserver类

  + 作用：观察URI引起ContentProvider中的数据变化和通知外界

  + 当ContentProvider中数据发生变化时（增删改），就会触发改ContentObserver类

  + 使用：

    ```java
    // 步骤1：注册内容观察者ContentObserver
        getContentResolver().registerContentObserver（uri）；
        // 通过ContentResolver类进行注册，并指定需要观察的URI
    
    // 步骤2：当该URI的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
        public class UserContentProvider extends ContentProvider { 
          public Uri insert(Uri uri, ContentValues values) { 
          db.insert("user", "userid", values); 
          getContext().getContentResolver().notifyChange(uri, null); 
          // 通知访问者
       } 
    }
    
    // 步骤3：解除观察者
     getContentResolver().unregisterContentObserver（uri）；
        // 同样需要通过ContentResolver类进行解除
    ```

# 4. Intent

Activity、Service、BroadcastReceiver之间的通信载体 =Intent

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\intent.png)

## 4.1 使用当前组件要完成的动作

### 4.1.1 显式意图

+ 特点：明确指定需启动的组件名

+ 显示Intent 不需要解析 Intent 则可直接启动目标组件

+ 具体使用：明确指定组件名的方式：调用Intent的构造方法、Intent.setComponent（）、Intent.setClass（）

  ```java
  // 使FirstActivity启动SecondActivity（通过按钮）
  mybutton.setOnClickListener(new OnClickListener() {
  
      @Override
      public void onClick(View v) {
      // 1. 实例化显式Intent & 通过构造函数接收2个参数
      // 参数1 = Context：启动活动的上下文，一般为当前Activity
      // 参数2 = Class：是指定要启动的目标活动
        Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
      
      // 2. 通过Activity类的startActivity（）执行该意图操作（接收一个Intent对象）
      // 将构建好的Intent对象传入该方法就可启动目标Activity
        startActivity (intent);
      }
  });
  ```

### 4.2.2 隐式意图

+ 特点：无明确指定需启动的组件名，但 **指定了需启动组件需满足的条件**

+ 隐式Intent 需要解析Intent 才能启动目标组件

+ 具体使用：通过 AndroidManifest.xml文件下的<Activity>标签下的<intent -filter> 声明 需 匹配的条件

  一个<Activity>标签下可以有多组<intent -filter>，只需匹配其中1组即可

+ 声明条件含：动作（Action）、类型（Category）、数据（Data）

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\intent2.png)

+ 实例

  ```java
  // 使FirstActivity启动SecondActivity（通过按钮）
        mybutton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
          // 1. 实例化1个隐式Intent对象，并指定action参数
      Intent intent = new Intent("android.intent.action.ALL_APPS");
         // 2. 调用Intent中的addCategory（）来添加一个category
        // 注：每个Intent中只能指定1个action，但却能指定多个category
    intent.addCategory("com.example.intent_test.MY_CATEGORY");
                  startActivity (intent);
      }
  });
  
  // 为使SecondActivity能继续响应该Intent
  // 我们需在AndroidManifest.xml文件下的<Activity>标签下配置<intent -filter>的内容
  
  <intent-filter >
        <action android:name="android.intent.action.ALL_APPS"/>
            <category android:name="android.intent.category.DEFAULT">        
            </category>
            <category android:name="com.example.intent_test.MY_CATEGORY"/>
  </intent-filter>
  ```

## 4.2 不同组件间传递数据

### 4.2.1 使用方法

+ putExtra（）、Bundle方式

### 4.2.2 可传递的数据类型

+ a.  8种基本数据类型（boolean byte char short int long float double）、String
+ b. Intent、Bundle
+ c. Serializable对象、Parcelable及其对应数组、CharSequence 类型
+ d. ArrayList，泛型参数类型为：<Integer>、<? Extends Parcelable>、<Charsequ ence>、<String>

### 4.2.3 使用

在当前Activity中将要传递到数据暂存在Intent中，在新启动的Activity中取出数据

+ putExtra

  ```java
  // 目的：将FristActivity中的一个字符串传递到SecondActivity中，并在SecondActivity中将Intent对象中的数据（FristActivity传递过来的数据）取出
  
      // 1. 数据传递
        // a. 创建Intent对象（显示Intent）
        Intent intent = new Intent(FirstActivity.this,SecondActivity.class);     
       
        // b. 通过putExtra（）方法传递一个字符串到SecondActivity；
        // putExtra（）方法接收两个参数：第一个是键，第二个是值（代表真正要传递的数据）
        intent.putExtra("data","I come from FirstActivity");
        
        // c. 启动Activity
        startActivity(intent);
       
      // 2. 数据取出（在被启动的Activity中）
        // a. 获取用于启动SecondActivit的Intent
        Intent intent = getIntent();
        // b. 调用getStringExtra（），传入相应的键名，就可得到传来的数据
        // 注意数据类型 与 传入时保持一致
        String data = intent.getStringExtra("data");
   
  ```

+ Bundle

  ```java
      // 1. 数据传递
        // a. 创建Intent对象（显示Intent）
        Intent intent = new Intent(FirstActivity.this,SecondActivity.class);     
       
        // b. 创建bundle对象
        Bundle bundle = new Bundle();
  
        // c. 放入数据到Bundle
        bundle.putString("name", "carson");
        bundle.putInt("age", 28);
        
        // d. 将Bundle放入到Intent中
        intent.putExtras(bundle);
  
        // e. 启动Activity
        startActivity(intent);
  
      // 2. 数据取出（在被启动的Activity中）
        // a. 获取用于启动SecondActivit的Intent
        Intent intent = getIntent();
  
        // b. 通过Intent获取bundle
        Bundle bundle = intent.getExtras();
  
        // c. 通过bundle获取数据传入相应的键名，就可得到传来的数据
        // 注意数据类型 与 传入时保持一致
        String nameString = bundle.getString("name");
        int age = bundle.getInt("age");
  ```

### 4.2.3 区别

+ Bundle更多适用于连续传递数据：Activity A→B→C；putExtra（）更多使用于单次传递、传递简单数据类型的应用场景

+ Bundle可传递对象

  + putExtra无法传递对象，Bundle可通过putSerializable传递对象，但传递的对象要实现Serializable接口

    ```java
    // 如传递User类的对象
    
    public class User implements Serializable {
        ...
    }
    
    // 传递时
    User user = new User();
    Intent intent = new Intent(MyActivity.this,OthereActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("user", user);
    intent.putExtras(bundle);
    ```

# 5. Fragment

## 5.1 定义

Activity界面中的一部分，可理解为模块化的Activity

+ Fragment不能独立存在，必须嵌入到Activity中
+ Fragment具有自己的生命周期，接收它自己的事件，并可以在Activity运行时被添加或删除
+ Fragment的生命周期直接受所在的Activity的影响。如：当Activity暂停时，它拥有的所有Fragment们都暂停

## 5.2 生命周期

![fragment2](/Users/kai/GitDocuments/src/Androiddddd/pictures/fragment2.jpg)

> ①Activity加载Fragment的时候,依次调用下面的方法: **onAttach** ->  **onCreate** -> **onCreateView** -> **onActivityCreated** -> **onStart** ->**onResume**
>
> ②当我们弄出一个悬浮的对话框风格的Activity,或者其他,就是让Fragment所在的Activity可见,但不获得焦点 **onPause**
>
> ③当对话框关闭,Activity又获得了焦点: **onResume**
>
> ④当我们替换Fragment,并调用addToBackStack()将他添加到Back栈中 **onPause -> onStop -> onDestoryView** ！！**注意**,此时的Fragment还没有被销毁!!!
>
> ⑤当我们按下键盘的回退键，Fragment会再次显示出来: **onCreateView -> onActivityCreated -> onStart -> onResume**
>
> ⑥如果我们替换后,在事务commit之前**没有调用addToBackStack()**方法将 Fragment添加到back栈中的话;又或者退出了Activity的话,那么Fragment将会被完全结束, Fragment会进入销毁状态 **onPause** -> **onStop** -> **onDestoryView** -> **onDestory** -> **onDetach**

## 5.3 创建 Fragment

### 5.3.1 静态加载Fragment

![fragment3](/Users/kai/GitDocuments/src/Androiddddd/pictures/fragment3.png)

### 5.3.2 动态加载Fragment

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\fragment4.png)

```java
//MainActivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoFragment fragment = new DemoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment,fragment)
                .commit();
    }
}


//DemoFragment
public class DemoFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_demo, container, false);
        return view;

    }
```

## 5.4 Fragment管理与Fragment事务

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\fragment5.png)

## 5.5 Fragment与Activity交互

![](E:\Kai\OneDrive\文档\作业\Kotlin\pictures\fragment-activity.png)

```java
//MainActivity
public class MainActivity extends AppCompatActivity {

    String mResult;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DemoFragment fragment = new DemoFragment();

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用接口回调的方法获取数据
                fragment.getData(new DemoFragment.CallBack() { 
                    @Override
                    public void getResult(String result) {
                        mResult = result;
                    }
                });
                Toast.makeText(MainActivity.this,mResult,Toast.LENGTH_SHORT).show();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment,fragment)
                .commit();
    }
}

//DemoFragment
public class DemoFragment extends Fragment {
    View view;
    EditText editText ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_demo, container, false);
        editText = view.findViewById(R.id.et);
        return view;
    }
    //接口回调
    public void getData(CallBack callBack){
        String msg =editText.getText().toString();
        callBack.getResult(msg);
    }
	//接口
    public interface CallBack{
        /*定义一个获取信息的方法*/
        void getResult(String result);
    }
}
```

