[TOC]



## AlertDialog

+ 背景设置不生效

  + ```java
    Window window = alertDialog.getWindow();
    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  // 有白色背景，加这句代码
    ```

  + https://blog.csdn.net/denglusha737/article/details/63255577

## 控件高度问题

+ http://t.cn/A6GSu251

+ **方法一：**

  ```java
  int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//-----①
  int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//-----②
  textView.measure(widthMeasureSpec, heightMeasureSpec); //----------------------------------------③
  int height = des_content.getMeasuredHeight();//获取高度
  ```

  注：View.MeasureSpec.makeMeasureSpec是制定测量规则，makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);表示大小不指定，由系统自身决定测量大小。上述①、②、③句等同于：textView.measure(0, 0);

+ **方法二、**

  ```java
  ViewTreeObserver observer = textView.getViewTreeObserver();
  observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
  	textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);//避免重复监听
          height = textView.getMeasuredHeight();//获文本高度
  	//获取高度后要进行的操作就在这里执行，在外面可能onGlobalLayout还没有执行而获取不到height
           des_layout.setOnClickListener(new MyClickListner());//设置监听（其中用到了height值）
      }});
  ```

  **注：对于TextView，假设有如下两行文本：**
  **`String text1 = "hello what's your name, how are you. I'm  fine.";`**
  **`String text2 = "hello\nhello";`**
  **其中text1宽度大于手机屏幕宽度（手机上多行显示），text2中手机上显示为两行，使用方法一测量文本控件高度，text1的结果只能得到一行的高度（非视觉高度，尽管显示为多行），text2的结果得到两行的高度。方法二得到的高度为手机上显示的高度（视觉高度）；**

## Button

+ **减小button大小**

  + https://blog.csdn.net/zfireear/article/details/51007094

  + 1.我们可以**直接设置button属性**：minHeight和minWidth

    ```xml-dtd
    android:minHeight="0dp"
    android:minWidth="0dp"
    ```

  + 2.**使用安卓定义的更小按钮的样式**

    ```xml-dtd
    style="?android:attr/buttonStyleSmall"
    ```

  

