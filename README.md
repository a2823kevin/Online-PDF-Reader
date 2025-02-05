# Online PDF Reader
功能簡易的線上PDF閱讀器，轉換使用者上傳的PDF檔案為網頁提供線上存取。

![reader](https://github.com/a2823kevin/Online-PDF-Reader/raw/main/docs/pdf_reader.png)

## 功能
### 新增PDF

![upload](https://github.com/a2823kevin/Online-PDF-Reader/raw/main/docs/pdf_upload.gif)

### 切換登入帳號

![change_user](https://github.com/a2823kevin/Online-PDF-Reader/raw/main/docs/change_user.gif)

### 下載/刪除PDF
![download/delete](https://github.com/a2823kevin/Online-PDF-Reader/raw/main/docs/download_and_delete.gif)

## 依賴項目
- JDK>=17
- Apache Maven 3.x
- Apache Tomcat 9
- Node.js 18
- SQLite 3
- [pdf2htmlEX-Server](https://github.com/a2823kevin/pdf2htmlEX-Server) (用以轉換PDF)

## 組態/安裝
### 設定.env
將```PDF2HTML_SERVER_IP```及```PDF2HTML_SERVER_PORT```改為正在運行的[pdf2htmlEX-Server](https://github.com/a2823kevin/pdf2htmlEX-Server) IP位址及埠號

### 建置前端
執行```build_frontend.bat```

### 建置後端
1. 生成war
```
mvn --no-transfer-progress clean install
```
2. 將編譯出的```target/OnlinePDFReader-0.1.war```部署至Tomcat Server

## 使用方法
在pdf2htmlEX Server及Tomcat Server運行期間，使用瀏覽器存取```http://<tomcat_server_ip>:<tomcat_server_port>/OnlinePDFReader```
e.g. ```localhost:8080/OnlinePDFReader```

## Credits
此程式依賴之[pdf2htmlEX-Server](https://github.com/a2823kevin/pdf2htmlEX-Server)使用了*pdf2htmlEX*(https://github.com/pdf2htmlEX/pdf2htmlEX)作為PDF檔案轉換工具。