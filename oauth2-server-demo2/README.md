keytool -genkey -alias ceshi -keyalg RSA -keysize 2048 -keystore mykeystore -validity 400

将使用RSA算法生成2048位的公钥/私钥对及整数，密钥长度为2048位，证书有效期为400天。使用的密钥库为mykeystore文件，别名为ceshi

keytool -genkeypair -alias zjy-oauth2-jwt -keyalg RSA -keypass zjy-pass -keystore zjy-oauth2-demo-jwt.jks -storepass zjy-pass

生成秘钥对，别名 zjy-oauth2-jwt，使用 RSA 算法，你的秘钥 key password: zjy-pass，存储密码：zjy-pass