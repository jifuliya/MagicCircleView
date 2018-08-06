# MagicCircleView

*[中文](README.md)|[English](README_En.md)* 

![](https://img.shields.io/badge/language-java-brightgreen.svg) ![](https://img.shields.io/badge/widget-magic-blue.svg) 

这是一个有趣的，如魔法一般的ImageCircleView，除了自动对图片进行适配并以圆形显示之外，当点击图片的时候，图片会自动以一个可以设置的速度旋转，
当然，也可以为它的旋转设置一个加速度，而当手指离开的时候，会产生一个动画效果并触发对应的响应事件，目前仅提供让图片以粒子效果散开消失，后续将添加更多有趣
的动画效果。

## 控件效果
![name](https://github.com/jifuliya/MagicCircleView/blob/master/app/src/main/res/drawable/MagicCircleGif.gif)

## 控件属性

| Attribution | Function|tips|
| --------   | -----:   | :-----:|
| magic_drawable | 设置图片 | 
| default_magic_drawable | 设置默认图片 | 
| magic_anim | 设置动画效果| 目前只支持一种动画
| magic_speed | 设置图片旋转速度| 初始速度建议在900以上，否则图片旋转会很慢
| magic_speed_interval | 设置图片旋转加速度| 建议在1~3之间
| magic_degree | 设置图片每次刷新旋转的角度| 
| magic_is_vague | 图片是否使用模糊效果| 

### Example

```Xml
    <jifuliya.lyl.magiccircleview.MagicCircleView
        android:id="@+id/magic"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:default_magic_drawable="@drawable/bg_default"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:magic_anim="debris"
        app:magic_degree="20"
        app:magic_drawable="@drawable/bg_default"
        app:magic_is_vague="false"
        app:magic_speed="900"
        app:magic_speed_interval="2" />
```

## 事件响应ClickUpListener
通过`magicCircleView.setClickUpListener`来设置事件响应，不需要使用`setOnClickListener`，否则会造成响应冲突
```java
     magicCircleView.setClickUpListener(new MagicCircleView.ClickUpListener() {
            @Override
            public void clickUp() {
                Log.i(TAG, "click-up");
                ChangeActivity.startChangeActivity(MainActivity.this);
            }
        });
```

## 重置
当用户在完成了一些转场的事件响应后，需要重置MagicCircleView的状态，控件提供了`magicCircleView.reset()`方法来进行重置，建议在`onPause()`中来完成
重置，保证View的正确状态。
```java
       @Override
    protected void onPause() {
        super.onPause();
        magicCircleView.reset();
    }
```

## 日志
通过查看`TAG = MagicCircleView`，可以在logcat查看一些重要的日志输出。

## License
```
Copyright 2018 jifuliya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


