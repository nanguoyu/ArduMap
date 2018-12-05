# ArduMap

### 这是什么?
一款站在巨人肩膀上的导航棒的安卓客户端，是我学习Android开发、熟悉AndroidStusio而实现的练手项目。
项目全名为“基于Arduino的方向定位与路线导航系统” 。
项目硬件是一根有四向指示灯、震动的导航棒，通过蓝牙与手机客户端连接后，导航棒根据高德SDK提供导航信息、设备硬件传感器方向信息为用户提供体感导航。
项目软件是主要实现基本地图软件功能，以及与硬件的蓝牙连接、数据通信。
用户使用时，会看到应前往方向的指示灯闪烁、震动器震动、语音播报转向提示。

### 它能做什么?
地图查看、路径规划、体感导航（通过蓝牙与基于arduino的交互硬件设备连接）

### 为什么不直接用手机呢？

我为了解决室内导航、复杂街区场景下的导航问题，为用户提供体感导航。在这些场景下，用户不容易根据地标等外界信息判断自己位置和方向，这时候一个直观的导航棒就很需要了。

### 数据来源?
高德

### 蓝牙来源?
Android-BluetoothSPPLibrary https://github.com/akexorcist/Android-BluetoothSPPLibrary

### 导航界面实现来源？
https://github.com/andforce/Maps

###我做了什么？
加入蓝牙连接功能，编写对应的arduino硬件程序

### 截图
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/0.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/1.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/2.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/3.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/4.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/5.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/6.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/7.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/8.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/10.png)
![截图](https://github.com/nanguoyu/ArduMap/blob/master/screenshot/11.png)
