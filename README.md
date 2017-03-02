# AppStore
这个项目是仿应用市场的Android客户端，主要模块包括应用展示模块，应用详情模块，应用下载模块。

####应用图片展示
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/01.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/02.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/03.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/04.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/05.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/06.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/07.png)
![image](https://github.com/fengrixin/AppStore/raw/master/Screenshot/08.png)

####应用展示模块
主体框架是用ViewPager和Fragment管理各个子模块，使用第三方自定义控件PagerTab实现指示器功能。其中，各个子模块中有使用到ListView(优化后)列表展示应用，实现ListVIew的HeaderView的自动轮播功能；自定义ImageView，解决不同分辨率的适配问题；使用到第三方自定义控件FlowLayout，RatioLayout，ProgressArc和ProgressHorizontal。

####应用详情模块
使用到第三方库xUtils中的BitmapUtils和ViewUtils。

####应用下载模块
使用HttpHelper访问网络，多线程断点续传下载，使用线程池对线程进行管理。同时对服务器返回的数据进行缓存
