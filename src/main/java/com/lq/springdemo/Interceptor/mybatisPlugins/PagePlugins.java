package com.lq.springdemo.Interceptor.mybatisPlugins;

import com.lq.springdemo.entity.common.Page;
import com.lq.springdemo.utils.MybatisUtils;
import com.lq.springdemo.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.*;
import java.util.Properties;

@Slf4j
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class} // 需要与对应版本一致
        )
})
public class PagePlugins implements Interceptor {
        @Override
        public Object intercept(Invocation invocation) throws Throwable {
                // 获取非代理对象
                StatementHandler target = MybatisUtils.getNoProxyTarget(invocation.getTarget());
                BoundSql boundSql = target.getBoundSql();
                // 拿到sql 转为小写，去掉前后空格
                String sql = boundSql.getSql().toLowerCase().trim();
                // 判断是否需要添加分页
                if (!sql.startsWith("select")) {
                        return invocation.proceed();
                }
                // 获取分页参数
                Page page = PageUtils.getPage();
                if (page == null) {
                        return invocation.proceed();
                }
                // 处理分页
                log.info("[需要分页的SQL: {}", sql.replace("\n",""));
                // 构建一个查询分页总条数的sql;
                Integer count = count(target, invocation, sql);
                log.info("[SQL的总条数为: " + count);
                // 处理pageNo
                if (page.getPageNo() == null || page.getPageNo() < 1)
                        page.setPageNo(1);
                // 处理pageSize
                if (page.getPageSize() == null || page.getPageSize() < 1)
                        page.setPageSize(10);
                // 设置分页对象
                page.setPageCount(count);
                page.setPageTotal(page.getPageCount() % page.getPageSize() == 0 ? page.getPageCount()/ page.getPageSize() : page.getPageCount()/ page.getPageSize() + 1);
                if (page.getPageNo() > page.getPageTotal())
                        page.setPageNo(page.getPageTotal());
                log.info("[处理过的Page为: " + page);
                sql += " limit " + (page.getPageNo() * page.getPageSize() - 1) + "," + page.getPageSize();
                log.info("[分页处理过的SQL: {}", sql.replace("\n",""));
                // 通过反射设置BoundSql的sql
                // MyBatis提供了工具,该工具通过反射实现
                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
                metaObject.setValue("sql", sql);
                return invocation.proceed();
        }

        /**
         * 获取sql的总条数
         * @param sql
         * @return
         */
        private Integer count(StatementHandler statementHandler, Invocation invocation, String sql) throws SQLException {

                // 判断是否存在排序的内容
                int orderByIndex = -1;
                if (sql.lastIndexOf("order by") != -1) {
                        sql = sql.substring(0, orderByIndex);
                }

                // 获取查询总条数sql
                int fromIndex = sql.indexOf("from");
                String countSQL = "select count(*) " + sql.substring(fromIndex);
                log.info("[查询总条数的SQL: " + countSQL);

                // 执行sql
                // 获得方法的参数
                Connection connection = (Connection) invocation.getArgs()[0];
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                try {
                        // sql 处理器
                        ps = connection.prepareStatement(countSQL,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                        ps = connection.prepareStatement(countSQL);
                        // 处理参数
                        statementHandler.parameterize(ps);
                        // 执行sql
                        resultSet = ps.executeQuery();
                        // 获取结果
                        if (resultSet.first()) {
                                return resultSet.getInt(1);
                        }
                } catch (SQLException  sqlException) {
                        log.info("[查询总条数的SQL出现异常！！！]");
                        throw sqlException;
                } finally {
                        if (resultSet != null) {
                                resultSet.close();
                        }
                        if (ps != null) {
                                ps.close();
                        }
                }
                return -1;
        }

        @Override
        public Object plugin(Object target) {
                return Interceptor.super.plugin(target);
        }

        @Override
        public void setProperties(Properties properties) {
                Interceptor.super.setProperties(properties);
        }
}
