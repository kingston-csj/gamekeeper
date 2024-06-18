# 使用官方MySQL镜像作为基础镜像
FROM mysql:latest
 
# 将SQL脚本复制到容器中的某个位置
COPY admin.sql /docker-entrypoint-initdb.d/

ENV MYSQL_DATABASE=admin
ENV MYSQL_PASSWORD=123456
ENV MYSQL_ROOT_PASSWORD=123456


# 创建一个数据卷挂载点
VOLUME /var/lib/mysql
 
# 暴露MySQL的默认端口
EXPOSE 3306

# 启动MySQL服务
CMD ["mysql"]
