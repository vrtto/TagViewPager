# TagViewPager

世面上现在有很多图片分享社交类app都喜欢加上打标签这个功能，比如比较知名“nice”、“理理相册”等等，他们打标签的方式可能各不相同但是本质原理是一样的，我感觉理理相册做的很好，于是参照它的交互模式自己实现了一下，基本做到了高仿，喜欢的话随手点个star 多谢。

##项目简述
1. 通过ToolBar的menu菜单点击添加标签；
2. 作为ViewPager外嵌套一层帧布局使用，解决了和ViewPager的滑动冲突；
3. 随意在界面拖动标签，加入严格的边界控制；
4. 具备标签点击进行编辑修改和长按删除标签的功能；

## 添加标签

效果图：

<img src="https://github.com/vrtto/TagViewPager/blob/master/gifs/photo1.jpg?raw=true" width=268 height=457 />

<img src="https://github.com/vrtto/TagViewPager/blob/master/gifs/photo2.jpg?raw=true" width=268 height=457 />

## 编辑标签

效果图：

<img src="https://github.com/vrtto/TagViewPager/blob/master/gifs/photo3.jpg?raw=true" width=268 height=457 />

## 删除标签

效果图：

<img src="https://github.com/vrtto/TagViewPager/blob/master/gifs/photo4.jpg?raw=true" width=268 height=457 />

## Thanks

[Android-PictureTagView](https://github.com/saiwu-bigkoo/Android-PictureTagView)
