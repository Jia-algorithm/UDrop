## UDrop App——智能背书语音助手

### 开发注意事项

#### 分支（branch）

- 任何新增issue必须先创建分支，issue完成后pull request到dev分支，review无误后方可merge。
- 从dev新增分支后，在pull request之前先将更新的dev分支merge到当前分支，避免生产环境冲突。

#### 命名

- 分支命名规范：序号_任务（ _分任务）
- 文件命名规范：
  - 场景名+Activity/Fragment/Adapter/Model（首字母大写）
  - drawable文件：类型_场景（ _item/selector/...）
  - layout文件：（类型_）场景（ _item/head/...）

#### 颜色
 - 统一放到values的colors中，在其他地方直接通过@color/...调用

### 开发进度

#### Launch页面

##### 已完成

- 布局Layout
- 到登录注册页面的跳转Activity

#### Login页面

##### 已完成

- 布局Layout
- 到注册页面的跳转Activity

##### 待完成

- 调取后端接口进行登录信息验证
- 登陆成功后到主页的跳转

#### Register页面

##### 已完成

- 布局Layout
- 到登录页面的跳转Activity

##### 待完成

- 调取后端接口实现信息注册
- 注册成功后到主页的跳转

#### Main页面

##### 已完成

- 含有三个item的底部导航栏Layout
- 点击不同item切换页面的逻辑OverviewActivity

#### Profile页面

##### 已完成

- 布局Layout
- recyclerview的简单adapter
- model和databinding
- 页面加载ProfileFragment

##### 待完成

- 获取用户数据
- 添加到设置页面的跳转
- 添加到用户信息修改页面的跳转
- 添加到用户已背课文浏览页面的跳转
- 添加到用户计划背诵课文页面的跳转
- 添加到课文详情页面的跳转

#### Home页面

##### 已完成

- 布局Layout
- recyclerview的简单aadapter
- 页面加载HomeFragment

##### 待完成
- 获取用户当日背书计划
- 获取用户当日复习计划
- 添加到背书页面的跳转
- 添加到复习页面的跳转
- 添加到收藏夹的跳转
- 添加到错题本的跳转
- 添加到游戏中心的跳转
