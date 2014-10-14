# MySQL 双主互备

结合F5, 配置Mysql的双主互备, 比如mysql装在10.1.137.81和10.1.1.137.82上面, 通过F5配置了一个虚拟ip 10.1.137.80. 则配置步骤如下:

1. 对于10.1.137.81,修改/etc/my.cnf.d/server.cnf的[mysqld] 位置, 添加如下:

        [mysqld]
        skip-name-resolve
        server-id=1
        log-bin=mysql-bin
        binlog-do-db=rrs
        binlog-ignore-db=mysql, test
        log-slave-updates
        sync_binlog=1
        auto_increment_offset=1
        auto_increment_increment=2
        replicate-do-db=rrs
        replicate-ignore-db=mysql, test

     对于10.1.137.82,修改/etc/my.cnf.d/server.cnf的[mysqld] 位置, 添加如下:

         [mysqld]
         skip-name-resolve
         server-id=2
         log-bin=mysql-bin
         binlog-do-db=rrs
         binlog-ignore-db=mysql, test
         log-slave-updates
         sync_binlog=1
         auto_increment_offset=2
         auto_increment_increment=2
         replicate-do-db=rrs
         replicate-ignore-db=mysql, test

      可以看到, 两台机器设置了不同的server-id, 以及不同的auto_increment_offset,可以防止主键冲突

2.  添加用于两台机器数据同步的账号:

    * 在10.1.137.81上执行如下命令:

          MariaDB [(none)]> CREATE USER 'repl'@'10.1.137.82' IDENTIFIED BY 'p@ssword'
          MariaDB [(none)]> GRANT REPLICATION SLAVE ON *.* TO 'repl'@'10.1.137.82';
          MariaDB [(none)]> FLUSH PRIVILEGES;

    * 在10.1.137.82上执行如下命令:

          MariaDB [(none)]> CREATE USER 'repl'@'10.1.137.81' IDENTIFIED BY 'p@ssword'
          MariaDB [(none)]> GRANT REPLICATION SLAVE ON *.* TO 'repl'@'10.1.137.81';
          MariaDB [(none)]> FLUSH PRIVILEGES;

3. 在两台机器上都要设置对方为自己的master:

   * 在两台机器上分别执行如下命令:

         MariaDB [(none)]> show master status;  #记住输出中bin-log的名字和position

   * 在10.1.137.81上执行如下命令:

         MariaDB [(none)]> stop slave;
         MariaDB [(none)]> change master to master_host='10.1.137.82', master_user=’repl’, master_password='p@ssword',master_log_file='MySQL-bin.00000x',master_log_pos=xxx;
         MariaDB [(none)]> start slave;

   * 在10.1.137.82上执行如下命令:

         MariaDB [(none)]> stop slave;
         MariaDB [(none)]> change master to master_host='10.1.137.81', master_user=’repl’, master_password='p@ssword',master_log_file='MySQL-bin.00000x',master_log_pos=xxx;
         MariaDB [(none)]> start slave;

       其中, master_log_file='MySQL-bin.00000x',master_log_pos=xxx 需要被替换成show master status的输出.

4. 测试设置是否成功, 在两台机器上分别执行如下命令:

        MariaDB [(none)]> show slave status \G

        Slave_IO_Running: Yes

        Slave_SQL_Running: Yes   \\如果此2项都为yes，master-master配置即成功

5. 应用的数据库连接池只要配置10.1.137.80即可实现mysql的双主互备的高可用集群, 利用F5的虚拟ip漂移机制

# Jetty 线上配置

`JAVA_OPTIONS=" -server -Xms4096m -Xmx4096m -XX:PermSize=512M -XX:+PrintGC -XX:+PrintGCDetails -Xloggc:/var/log/jetty/gc.log"`
