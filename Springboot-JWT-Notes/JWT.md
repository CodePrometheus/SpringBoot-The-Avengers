# 							JWT & Springboot

[TOC]







## 概述





### 简介

-----

![image-20200813164756041](JWT.assets/image-20200813164756041.png)



> JSON Web令牌（JWT）json web token是一个开放标准（[RFC 7519](https://tools.ietf.org/html/rfc7519)），它定义了一种紧凑且自包含的方式，用于在各方之间安全地将信息作为JSON对象传输。由于此信息是经过数字签名的，因此可以被验证和信任。可以使用秘密（使用**HMAC**算法）或使用**RSA**或**ECDSA**的公钥/私钥对对JWT进行**签名**。
>
> 尽管可以对JWT进行加密以提供双方之间的保密性，但我们将重点关注已*签名的*令牌。签名的令牌可以验证其中包含的声明的*完整性*，而加密的令牌则将这些声明*隐藏*在其他方的面前。当使用公钥/私钥对对令牌进行签名时，签名还证明只有持有私钥的一方才是对其进行签名的一方。



### 作用

----

- **授权**：这是使用JWT的最常见方案。一旦用户登录，每个后续请求将包括JWT，从而允许用户访问该令牌允许的路由，服务和资源。单点登录是当今广泛使用JWT的一项功能，因为它的开销很小并且可以在不同的域中轻松使用。
- **信息交换**：JSON Web令牌是在各方之间安全地传输信息的一种好方法。因为可以对JWT进行签名（例如，使用公钥/私钥对），所以您可以确保发件人是他们所说的人。此外，由于签名是使用标头和有效负载计算的，因此您还可以验证内容是否遭到篡改。



### 回顾对比

---

在互联网中，我们每天都会使用到各种各样的APP和网站，在使用过程中通常还会遇到需要注册登录的情况，输入你的用户名和密码才能正常使用，也就是说成为这个应用的合法身份才可以访问应用的资源，这个过程就是认证。

当一个用于认证通过以后，为了避免用户的每次操作都进行认证，这个时候就需要保持一个用户认证状态的会话，常见的认证会话机制有很多，例如`session`认证，`token`认证等。



#### 基于session的认证方式

用户认证成功后，在服务端生成用户相关的数据保存在当前会话`session`中，发给客户端的 `SesssionId`会存放到 `cookie` 中，这样用户客户端请求时带上 `SesssionId` 就可以验证服务器端是否存在 `session` 数 据，以此完成用户的合法校验，当用户退出系统或`session`过期销毁时,客户端的`SesssionId`也就无效了。

![Session.jpg](JWT.assets/7sZWaPpxLBtOd4X.png)

基于`session`的认证方式由`Servlet`规范定制，服务端要存储`session`信息需要占用内存资源，客户端需要支持`cookie`;

Cookie虽然在一定程度上解决了“保持状态”的需求，但是由于Cookie本身最大支持4096字节，以及Cookie本身保存在客户端，可能被拦截或窃取，因此就需要有一种新的东西，它能支持更多的字节，并且他保存在服务器，有较高的安全性。这就是Session。

问题来了，基于HTTP协议的无状态特征，服务器根本就不知道访问者是“谁”。那么上述的Cookie就起到桥接的作用。

我们可以给每个客户端的Cookie分配一个唯一的id，这样用户在访问时，通过Cookie，服务器就知道来的人是“谁”。然后我们再根据不同的Cookie的id，在服务器上保存一段时间的私密资料，如“账号密码”等等。

总结而言：Cookie弥补了HTTP无状态的不足，让服务器知道来的人是“谁”；但是Cookie以文本的形式保存在本地，自身安全性较差；所以我们就通过Cookie识别不同的用户，对应的在Session里保存私密的信息以及超过4096字节的文本。



> 通常用户一次请求要转发多次，若用session存储在服务器里给服务器增加负担，且因为session是基于cookie进行用户识别的，若cookie被捕获，易受到CSRF（跨站伪造请求攻击），另外sessionId是一个特征值，表达的信息不够丰富，不容易扩展，若后端应用是多节点部署，就需要实现session共享机制，不便于集群应用



#### 基于token的认证方式

用户认证成功后，服务端生成一个token发给客户端，客户端可以放到 cookie 或 localStorage等存储中，每次请求时带上 token，服务端收到token通过验证后即可确认用户身份。

![token.jpg](JWT.assets/Pfp978ATHeEWJZ5.png)

基于`token`的方式则一般不需要服务端存储`token`，并且不限制客户端的存储方式。



#### 基于JWT的认证方式

![image-20200813173009362](JWT.assets/image-20200813173009362.png)

它将用户信息加密到`token`里，服务器不保存任何用户信息。服务器通过使用保存的密钥验证`token`的正确性，只要正确即通过验证

**认证流程：**

前端通过Web表单将自己的用户名和密码发送到后端的接口，这一过程一般是HTTP POST请求

后端核对用户名和密码成功后，将用户的id等其他信息作为JWT Payload（负载），将其与头部分别进行Base64编码拼接后签名，形成JWT(Token)，形成的JWT就是一个形同xxxxx.yyyyy.zzzzz的字符串

后端将JWT字符串作为登录成功的返回结果返回给前端，前端可以将返回的结果保存在localStorage或session Storage上，退出登录时前端删除保存的JWT即可，前端在每次请求时将JWT放入HTTP Header中的Authorization位。（解决XSS和XSRF问题）HEADER，后端检查是否存在，如存在验证JWT的有效性。

> `Authorization`标头中检查有效的JWT ，如果存在，则将允许用户访问受保护的资源。如果JWT包含必要的数据，则可以减少查询数据库中某些操作的需求，尽管这种情况并非总是如此。

![JSON Web令牌如何工作](JWT.assets/client-credentials-grant.png)

> 1. 应用程序或客户端向授权服务器请求授权。这是通过不同的授权流程之一执行的。例如，典型的符合OpenID Connect的应用程序将`/oauth/authorize`使用授权代码流通过端点。
> 2. 授予授权后，授权服务器会将访问令牌返回给应用程序。
> 3. 应用程序使用访问令牌来访问受保护的资源（例如API）。



1. 简洁: 可以通过`URL`、`POST`参数或者在`HTTP header`发送，因为数据量小，传输速度也很快；
2. 自包含：负载中可以包含用户所需要的信息，避免了多次查询数据库；
3. 因为`Token`是以`JSON`加密的形式保存在客户端的，所以`JWT`是跨语言的，原则上任何`web`形式都支持；
4. 不需要在服务端保存会话信息，特别适用于分布式微服务





### 结构

----

JSON Web令牌以紧凑的形式由三部分组成，这些部分由点（`.`）分隔，分别是：

- 标头
- 有效载荷
- 签名



####  header

`Jwt`的头部承载两部分信息：标头*通常*由两部分组成：令牌的类型（即JWT）和所使用的签名算法，例如HMAC SHA256或RSA。

例如：

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

然后，此JSON被**Base64Url**编码以形成JWT的第一部分。

1. 声明类型，这里是`Jwt`
2. 声明加密的算法 通常直接使用 `HMAC SHA256`

`Jwt`里验证和签名使用的算法列表如下：

|  JWS  | 算法名称 |
| :---: | :------: |
| HS256 | HMAC256  |
| HS384 | HMAC384  |
| HS512 | HMAC512  |
| RS256 |  RSA256  |
| RS384 |  RSA384  |
| RS512 |  RSA512  |
| ES256 | ECDSA256 |
| ES384 | ECDSA384 |
| ES512 | ECDSA512 |

#### playload

载荷就是存放有效信息的地方。基本上填`2`种类型数据。

有效负载，其中包含声明。声明是有关实体（通常是用户）和其他数据的声明。

1. 标准中注册的声明的数据；
2. 自定义数据。

由这2部分内部做`base64`加密。

- 标准中注册的声明 (建议但不强制使用)

```java
iss: jwt签发者
sub: jwt所面向的用户
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
```

- 自定义数据:存放我们想放在`token`中存放的`key-value`值



```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```

然后，对有效负载进行**Base64Url**编码，以形成JSON Web令牌的第二部分。

> 请注意，对于已签名的令牌，此信息尽管可以防止篡改，但任何人都可以读取。除非将其加密，否则请勿将机密信息放入JWT的有效负载或报头元素中。



#### signature

前两部分都是使用Base64进行编码的，即前端可以知道里面的信息，Signature需要使用编码后的header和payload以及我们提供的一个密钥，然后使用header中指定的签名算法（HS256）进行签名，**其作用是保证JWT没有被篡改过**

最后这一步签名的过程，实际上是对头部以及负载内容进行签名，防止内容被篡改

要创建签名部分，必须获取编码的标头，编码的有效载荷，机密，标头中指定的算法，并对其进行签名。

签名用于验证消息在此过程中没有更改，并且对于使用私钥进行签名的令牌，它还可以验证JWT的发送者是它所说的真实身份。

例如，如果要使用HMAC SHA256算法，则将通过以下方式创建签名：

```json
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```

`Jwt`的第三部分是一个签证信息，这个签证信息由三部分组成
`base64`加密后的`header`和`base64`加密后的`payload`连接组成的字符串，然后通过`header`中声明的加密方式进行加盐`secret`组合加密，然后就构成了`Jwt`的第三部分。

---

输出是三个由点分隔的Base64-URL字符串，可以在HTML和HTTP环境中轻松传递这些字符串，与基于XML的标准（例如SAML）相比，它更紧凑。

下面显示了一个JWT，它已对先前的标头和有效负载进行了编码，并用一个秘密进行了签名。

![编码的JWT](JWT.assets/encoded-jwt3.png)



> 注意，Base64是一种编码，是可逆的，所以在JWT中，不应该在负载里面放入任何敏感的数据，

![image-20200813184203102](JWT.assets/image-20200813184203102.png)







## Springboot实现

```java
		<dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.10.3</version>
        </dependency>
```

```java
@SpringBootTest
class JwtApplicationTests {

    @Test
    void contextLoads() {

        HashMap<String, Object> map = new HashMap<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND,20);

        String token = JWT.create()
                .withHeader(map)//header
                .withClaim("userId", 21)//payload
                .withClaim("username", "xiaohei")//设置自定义用户名
                .withExpiresAt(instance.getTime())//令牌过期时间
                .sign(Algorithm.HMAC256("!@#$%￥"));//签名
        System.out.println(token);
    }
```

```java
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTczMTY0NjcsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2hlaSJ9.jR-r7rvSEMFHRhjMIFtJ0Lrm6748hCnlenMuft0_WCM
```

```java
    @Test
    public void test(){
//        创建验证对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!@#$%￥")).build();
        DecodedJWT verify = jwtVerifier.verify("");
//        反向取得信息
        System.out.println(verify.getClaim("userId").asInt());
        System.out.println(verify.getClaim("username").asString());
    }
```

```java
//实现封装
public class JWTUtils {

    public static final String SIGN = "!@#$%￥";

    //    生成token
    public static String getToken(Map<String, String> map) {

//        进行封装
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 30);

//        创建JWT builder
        JWTCreator.Builder builder = JWT.create();

//        payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

//        sign
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));

        return token;
    }

    //    验证token合法性
    public static DecodedJWT verify(String token) {
//        对下面进行一个封装
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!@#$%￥")).build();
//        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTczMTgxNDcsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2hlaSJ9.04u5SaEgbw7lTxBiVbJRduY0b4aKCTXPJAi7xC8xW2c\n");
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
```

?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true



```java
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/login")
    public Map<String, Object> login(User user) {
        log.info("用户名：[{}] ", user.getName());
        log.info("密码：[{}] ", user.getPassword());
        Map<String, Object> map = new HashMap<>();
        try {
            User login = userService.login(user);
//            在此生成JWT令牌
            Map<String, String> payload = new HashMap<>();
            payload.put("id", login.getId());
            payload.put("name", login.getName());
            String token = JWTUtils.getToken(payload);
            map.put("token", token);//响应token，存储在客户端，每次请求时携带
            map.put("state", true);
            map.put("msg", "成功登录");
        } catch (Exception e) {
            map.put("state", false);
            map.put("msg", "认证失败");
        }
        return map;
    }

    @PostMapping("user/test")
    public Map<String, Object> teat(String token) {
        Map<String, Object> map = new HashMap<>();
        log.info("当前token：[{}]", token);
//        验证token
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            map.put("state", true);
            map.put("msg", "请求成功");
            return map;
//            可对异常做细腻度处理
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
        }
        map.put("state", false);
        return map;
    }

}
```

使用上诉方式可以看出，每次都要传递一个token数据，每个方法都要验证token代码冗余

优化  使用拦截器



### 优化

----

在分布式里可以使用网关，在单体应用下可以用拦截器  **Interceptor**



```java
    @PostMapping("user/test")
    public Map<String, Object> teat(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
//        处理业务逻辑
//        可以通过request把头里的token拿出来
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        System.out.println(verify);
        log.info("用户id:[{}]",verify.getClaim("userId").asString());
        log.info("用户name:[{}]",verify.getClaim("username").asString());
        map.put("state", true);
        map.put("msg", "请求成功");
        return map;
    }
```

````java
@Configuration
//配置拦截器
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/user/test")         //其他接口token验证
                .excludePathPatterns("/user/login");//所有用户都放行
    }
}
````



```java
//拦截器  将token藏进header里
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        获取请求头中的令牌
        String token = request.getHeader("token");

        Map<String, Object> map = new HashMap<>();
//        验证token
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            return true;//放行请求
//            可对异常做细腻度处理
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
        }
        map.put("state", false);//设置状态
//        将map转为JSON响应给前台
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
```

