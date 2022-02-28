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
- 调取后端接口进行登录信息验证
- 登陆成功后到主页的跳转

#### Register页面

##### 已完成

- 布局Layout
- 到登录页面的跳转Activity
- 调取后端接口实现信息注册
- 注册成功后到主页的跳转

#### Main页面

##### 已完成

- 含有三个item的底部导航栏Layout
- 点击不同item切换页面的逻辑OverviewActivity

#### Profile页面

##### 已完成

- 基本页面布局
- 获取用户数据
- 修改个性签名
- 退出登录（直接回到Launch页面）

#### Home页面

##### 已完成

- 基本页面布局
- 获取推荐故事并加载
- 获取用户当日背书计划
- 获取用户当日复习计划
- 到语音助手页面的跳转
- 到进度页面的跳转
- 到收藏夹的跳转
- 到搜索页面的跳转

#### UDrop页面

##### 已完成
- 基本页面布局
- 识别用户语音输入
- 合成小助手语音

##### 待完成
- 获取后端接口完成背书逻辑

#### Collection页面

##### 已完成
- 基本页面布局
- 获取收藏的故事
- 点击单个跳转到故事详情
- 取消收藏

#### Detail页面

##### 已完成
- 基本页面布局
- 获取详情
- 添加收藏&&取消收藏

#### Search页面

##### 已完成
- 基本页面布局
- 获取推荐故事并加载
- 监听搜索内容实时展示结果
- 点击单个结果到详情页
- 加入学习计划

### 接口
url: http://121.199.77.139:5001
#### 1.1 add_new_user (Done)
/user/register

- POST
- form: \[name: String, password: String]
- return:
  - user_id: Integer
  - added: Integer: 0, new user added; 1, not added (exist)
#### 1.2 check_existed_user (Done)
/user/name

- GET
- param: \[name: String]
- return:
  - "Exist", "Not exist", "Failed"
#### 1.3 get_user_info (Done)
/user/basic_info

- GET
- param: \[user_id: Int]
- return: (user_name: String, user_motto: String, learned_days: Int)
#### 1.4 change_user_info (Done)
/user/basic_info

- POST
- param: \[user_id: Int, name: String, user_motto: String]
- return: 
  - "Failed", "Success"
#### 1.5 login (Done)
/user/login

- POST
- form: \[name: String, password: String]
- return:
  - success: Integer 1 or 0
  - userId: Integer
#### 2.1 get_schedule (Mock)
/study/schedule

- GET
- param: (user_id: Int)
- return: (new_list: JSONArray, review_list: JSONArray)
  - new_list: 今日所有需要新学的课文，每个课文信息中包含是否已背诵
  - review_list: 今日所有需要复习的课文，每个课文信息中包含是否已背诵
#### 2.2 set_new_schedule (Todo)
- param: (user_id: Int, new_schedule: JSONArray) 这里的JSONArray就是你前面返回的故事列表格式，不用管它是个什么词
- return: (resultCode: Int)
  - 0: failure
  - 1: success
#### 3.1 get_text_detail (Todo)
- param: (title: String)
- return: (title: String, writer: String, writer_info: String, content: String)
#### 3.2 search_text (Todo)
- param: (key: String)
- return: (result_list: JSONArray) 诗名或作者与关键词匹配
#### 4.1 get_collection (Todo)
- param: (user_id: Int)
- return: (collection_list: JSONArray)
#### 4.2 cancel_collection (Todo)
- param: (user_id: Int, title: String)
- return:
  - "Failed", "Success"
#### 4.3 add_collection (Todo)
- param: (user_id: Int, title: String)
- return:
  - "Failed", "Success"
