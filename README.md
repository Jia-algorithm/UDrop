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

- 基本页面布局

##### 待完成

- 获取用户数据
- 添加到设置页面的跳转
- 添加到用户信息修改页面的跳转
- 添加到用户已背课文浏览页面的跳转
- 添加到用户计划背诵课文页面的跳转
- 添加到课文详情页面的跳转

#### Home页面

##### 已完成

- 基本页面布局

##### 待完成
- 获取用户当日背书计划
- 获取用户当日复习计划
- 添加到背书页面的跳转
- 添加到复习页面的跳转
- 添加到收藏夹的跳转
- 添加到错题本的跳转
- 添加到游戏中心的跳转

#### UDrop页面

##### 已完成
- 基本页面布局

##### 待完成
- 识别用户语音输入
- 调取百度语音API实现语音交互
- 背书功能的实现
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
#### 2.1 get_schedule (Done)
/study/schedule

- GET
- param: (user_id: Int)
- return: (new_list: JSONArray, review_list: JSONArray)
  - new_list: 今日所有需要新学的课文，每个课文信息中包含是否已背诵
  - review_list: 今日所有需要复习的课文，每个课文信息中包含是否已背诵
#### 2.2 set_new_schedule (Done)

/study/new_schedule

- POST

- param: (user_id: Int, new_schedule: JSONArray) 这里的JSONArray就是你前面返回的古诗列表格式，不用管它是个什么词
- return: (resultCode: Int)
  - 0: failure
  - 1: success
#### 2.3 set_review_schedule (Done)

/study/review_schedule

- POST

- param: (user_id: Int, review_schedule: JSONArray)
- return: (resultCode: Int)
  - 0: failure
  - 1: success
#### 3.1 get_text_detail (Done)

/passage/detail

- GET

- param: (title: String)
- return: (title: String, author: String, author_info: String, content: String)
#### 3.2 search_text (Done)

/poems/search

- GET
- param: (key: String)
- return: (result_list: JSONArray) 诗名或作者与关键词匹配

#### 3.3 random_poems (Done)

/poems/random

- GET
- param: (number: Int) 随机返回的数量
- return: (result_list: JSONArray)

#### 4.1 get_collection (Done)

/user/collection

- GET

- param: (user_id: Int)
- return: (collection_list: JSONArray)
#### 4.2 remove_collection (Done)

/user/collection

- DELETE

- param: (user_id: Int, title: String)
- return:
  - "Failed", "Removed", "No Change"
#### 4.3 add_collection (Done)

/user/collection

- POST

- param: (user_id: Int, title: String)
- return:
  - "Failed", "Added", "No Change"

#### 4.4 check_single_collection(Done)

/user/check_collection

- GET
- param: (user_id: Int, title: String)
- return:
  - "Failed", "Yes", "No"

#### 5.1 reply (Doing)

/response

- POST
- param: (user_id: Int, text: String) 用户id和用户语音转成的文本
- return:
  - response: String，需要转成语音的回复
  - is_finished: Bool，true - 会话结束，false - 尚未结束继续进行

