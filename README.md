# [Side project] Spring boot + Vue + MySQL 簡易考試網站專案，並部屬於Heroku上【 Abby.com 線上考試系統】

###### tags: `Java` `Spring Boot` `Vue` `MySQL`


## [前情提要] 
正式成為全端工程師也2年了，2022年再度受到Covid-19的襲擊，公司WFH了好一陣子、家裡又有很多高風險族群的同住家人。宅在家的時間變多了，萌生了檢驗自己運用現有的技術，寫一個簡單線上面試技術考題系統的想法，來檢驗自己2年的成果，Let's go!!

## [系統說明]
* 一個用於做技術面試的線上考試網站 
* 角色權限如下表：分為考生、考官、管理員 3 種


| 權限/角色 | 考生     | 考官     | 管理員 |
| --------- | -------- | -------- | ------ |
| 首頁      | ☑        | ☑        | ☑      |
| 待考科目  | ☑        | ☑        | ☑      |
| 考題管理  | -------- | ☑(部分)  | ☑      |
| 考試管理  | -------- | ☑(部分) | ☑      |
| 考試紀錄  | ☑        | ☑(部分)  | ☑      |

### TODO 部分細部功能未完整開發：
* 細部權限設定未完整區隔
* 頭像、批次題目上傳功能
* 個人資料設定頁面
* 前端畫面Modal上一步/下一步bug
* 考題刪除、criteria搜尋功能
* ...etc

## [專案架構]
* 前端: Vue + Ant Design Vue (因為懶...畫面架構引用了 https://github.com/andrewgreat/antd-pro-vite-vue3)
* 後端: Spring Boot + JPA + Maven + Swaggar2
* DB: MySQL
* 架設: Heroku (免費仔的choice，介面比起3大平台親民很多，但事後發現蠻多雷QQ)

## [系統展示說明]

> 【系統網址】：https://abby-com-tech-test.herokuapp.com/
> 【系統Swaggar】：https://abby-test-heroku.herokuapp.com/swagger-ui/index.html
> 【GitHub程式碼】：https://github.com/mko123654/AbbyComTechTest

#### Swaggar畫面
![](https://i.imgur.com/2548STl.png)


#### 登入畫面
![](https://i.imgur.com/Cm4HCST.jpg)

#### 待考科目&考試紀錄
![](https://i.imgur.com/BsiyDuM.png)
![](https://i.imgur.com/UmHzF7x.png)
![](https://i.imgur.com/6DuiY3F.png)
![](https://i.imgur.com/uij6gEa.png)

#### 考題管理
![](https://i.imgur.com/VKAEiEy.png)
![](https://i.imgur.com/0fruz1C.png)
![](https://i.imgur.com/lvhv9PC.png)
![](https://i.imgur.com/TJ5lkm7.png)

#### 考試管理
![](https://i.imgur.com/CGoeXOo.png)
![](https://i.imgur.com/ggvwDUl.png)
![](https://i.imgur.com/SD2rSMR.png)


## 專案啟動方式：

### 1. 線上DEMO
> 【系統網址】：https://abby-com-tech-test.herokuapp.com/
> 【系統Swaggar】：https://abby-test-heroku.herokuapp.com/swagger-ui/index.html


### 2. 全部本機執行

2.1事先安裝：
+ 本機安裝JDK8
+ 本機安裝Maven
+ 安裝MySQL，設定密碼為abby0718，建立資料庫並預設Schema為exam，將backend\src\main\java\abby\exam\sqlScript下的all.sql匯入
+ 本機安裝Node

2.2啟動後端：
+ 2.2.1 IDE打開後等Maven專案Loading下載好，可以直接啟動。
+ 2.2.2 下指令 `mvn install -DskipTests` 打包成jar檔，於生成的target資料夾內，下指令`java –jar exam-0.0.1-SNAPSHOT.jar`
    
2.3啟動前端：
+ 2.3.1 cd到frontend資料夾下，下指令`npm install`讓子彈飛一陣子
+ 2.3.2 安裝完成後下指令`npm run serve`
+ 2.3.3 打開 http://localhost:8000 查看

