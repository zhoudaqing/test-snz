wtf

- 需要部署两个系统 console 和 showcase
- 只需要跑 console 的话 showcase 是不需要的，但是需要使用 console 上的功能就需要 showcase 了
- showcase 的分支是 new_container
- showcase 使用的前端工程是 celebi ，分支是 pampas_test
- 本地 2181 起个 zk （console 和 showcase 两个系统的 dubbo 注册中心）
- 本地 6379 起个 redis （showcase 装修存储）

部署 console

- 使用 pampas 工程下的 `console-release.sh` 打包
- 在启动 jetty 的用户的环境变量中新增一个 `PAMPAS_CLS_ZK='localhost:2181'`

部署 showcase

- `mvn clean package -Dmaven.test.skip` 就好

jetty & nginx

- 需要区分 console 和 showcase ，rootPath 或者两个 jetty 实例
- nginx 需要转发 www.showcase.com 到 jetty 的 showcase 中
- 如果装修了非 `/` 的子站，也要 nginx 转发过去