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

  

## 调起地图

#### 高德

+ ```java
      /**                 调起高德地图
       *                  https://lbs.amap.com/api/amap-mobile/guide/android/route
       * @param dev       起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
       * @param t         t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）（骑行仅在V7.8.8以上版本支持）
       */
      private void openGaoDeMap(Context context, String sName, String edName, LatLng sLatLng, LatLng edLatLng, int dev, int t){
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.addCategory(Intent.CATEGORY_DEFAULT);
  //        amapuri://route/plan/?sid=&slat=39.92848272&slon=116.39560823&sname=A&did=&dlat=39.98848272&dlon=116.47560823&dname=B&dev=0&t=0
          Uri uri = Uri.parse("amapuri://route/plan/?sid="
                          + "&slat=" + sLatLng.latitude
                          + "&slon=" + sLatLng.longitude
                          + "&sname=" + sName
                          + "&did="
                          + "&dlat="+edLatLng.latitude
                          + "&dlon="+edLatLng.longitude
                          + "&dname="+edName
                          + "&dev="+dev
                          + "&t="+t
          );
  
          intent.setData(uri);
          intent.setPackage("com.autonavi.minimap");
          context.startActivity(intent);
      }
  ```

#### 腾讯

+ ```java
      /**
       *          调起腾讯地图
       *          https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute
       */
      private void openTencentMap(Context context, String sName, String edName, LatLng sLatLng, LatLng edLatLng){
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.addCategory(Intent.CATEGORY_DEFAULT);
          Uri uri = Uri.parse("qqmap://map/routeplan?"
                  + "type=drive"
                  + "&from=" + sName
                  + "&fromcoord=" + sLatLng.latitude
                  + "," + sLatLng.longitude
  //               + "&fromcoord=39.994745,116.247282"
                  + "&to=" + edName
                  + "&tocoord=" + edLatLng.latitude
                  + "," + edLatLng.longitude
  //               + "&tocoord=39.867192,116.493187"
  //                + "&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"
          );
          intent.setData(uri);
          intent.setPackage("com.tencent.map");
          context.startActivity(intent);
      }
  ```

+ 