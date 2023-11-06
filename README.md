# Android · 匿名类 IM（即时聊天）
## 相关链接
- 项目设计与开发文档：[网盘链接](https://pan.baidu.com/s/1yHCvR4Tl3_CsBxsxX16r6Q?pwd=0608)

- 客户端：[Gitee链接](https://github.com/nor1take/android-im-chat)

- 服务端 SSM：[Gitee链接](https://github.com/nor1take/android-im-chat-server)

- 服务端 SpringBoot：[Gitee链接](https://github.com/nor1take/android-im-chat-server-springboot)

## 整体设计
### 1 服务器的搭建
我们通过编写web服务端，再通过花生壳实现内网穿透，以实现我们本次的安卓服务器。其中涉及的开源协议和项目有：

- 涉及web应用的部署的Apache开源协议、开源项目Tomcat；
- web持久层使用到的开源关系型数据库管理系统MySQL；
- 和C/S请求应答相关的android处理网络请求的开源网络框架项目OKhttp以及HTTP协议；
- 和IM功能相关的TCP协议。

#### 1.1 使用Maven工具 + MyBatis技术框架 + MySQL数据库
Maven工具提供了一套标准化的项目结构、构建流程和依赖管理机制，极大地方便了对于web应用的管理和配置；

MyBatis技术框架极大地简化了对于MySQL数据库操作和访问。

#### 1.2 客户端使用OKhttp向服务端发起请求、使用TCP协议与服务端进行通信
android处理网络请求的开源网络框架项目是OKhttp。OKhttp封装了一个高性能的http请求库。所以，我们安卓端对服务器的所有请求都是通过OKhttp实现。

Java对基于TCP协议的网络提供了良好的封装，为客户端提供了Socket类，为服务端提供了ServerSocket类。我们IM功能的实现就是基于此。

#### 1.3 使用内网穿透工具——花生壳
在这一步之前，服务端还只能用于本地局域网测试，而我们的目的是让所有人只有联网就能使用我们的软件。我们解决用到内网穿透技术解决这个问题。

内网穿透基本原理：让在内网的节点主动访问一个拥有公网IP地址的服务器，并由中间服务器搭桥，打通经过该服务器从其他主机到NAT之后节点的隧道。

简单来说，通过内网穿透，我们的服务器可以有效地被公网访问。

我们使用内网穿透工具——花生壳实现内网穿透。

我们做了2个映射：
- HTTPS映射是用于部署到tomcat上的web应用，将本机的IP映射到一个公网IP；
- TCP映射是用于IM功能，将本机的IP地址和端口号映射到新的IP地址和端口号。
 
### 2 即时通讯（IM）功能的实现
使用TCP实现即时通讯（IM）功能。
#### 2.1 服务端
因为两个Client无法直接建立连接，整体思路是使用一个Server中转Client的消息。因为每个Client是单独与Server建立连接，所以在实现Client到Client的通信时，Client发出的消息要表明：是发给谁的、以及是针对哪个帖子发的，由Server去补充是谁发的，再中转出去。这个过程涉及到几个技术细节：

- Server在与Client建立连接的时候需要单独开辟一个线程维持连接；
- Server需要为Client添加标识。这里我们使用了哈希表HashMap<String, Socket>，中转消息的过程其实是对HashMap的遍历过程，当匹配到对应的String（Client的标识）才将信息进行写出。
#### 2.2 Android端
1. 与TCP服务端的连接

    显然，一个Client只能开辟一个Socket去接收Server的消息，所以需要一个Application类，因为该类“只被执行一次，且能起到全局变量的作用”，所以能够跨界面的去使用客户端的socket去接收消息。
    此外，消息列表需要一个线程实时地监听Server发来的消息，而聊天详情界面只需要在进入时才开启接收消息的线程，离开界面时则关闭线程以节约资源。

2. 消息实时通知

    消息实时通知是IM功能的辅助功能但是是关乎用户体验的重要功能。用户需要在收到消息的第一时间能感受手机振动，有铃声，且状态栏有消息提醒。为此我们写了一个通知工具类Notice_Util，并同时还考虑到了不同Android版本的兼容问题。

