
#### quartz集成mysql自定义DataSource和密码加密

```
org.quartz.dataSource.myDS.connectionProvider.class = service.datasource.quartz.MyPoolingConnectionProvider
```

```
package com.quartz.datasource;

import org.apache.ibatis.io.Resources;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.PoolingConnectionProvider;
import org.quartz.utils.PropertiesParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.common.security.SecurityUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 *  入口见：StdSchedulerFactory.instantiate，  见 PROP_CONNECTION_PROVIDER_CLASS 参数。
 *
 * @auther zhihui.kzh
 * @create 14/9/1719:15
 */
public class MyPoolingConnectionProvider extends PoolingConnectionProvider {
    private static final Logger logger = LoggerFactory.getLogger(MyPoolingConnectionProvider.class);

    public MyPoolingConnectionProvider(Properties config) throws SchedulerException, SQLException {
        super(config);
    }

    public MyPoolingConnectionProvider() throws SQLException, SchedulerException, IOException {
        super(getProperties());
    }

    public static Properties getProperties() throws IOException {
        Properties oriProp = Resources.getResourceAsProperties("quartz.properties");

        PropertiesParser cfg = new PropertiesParser(oriProp);

        String[] dsNames = cfg.getPropertyGroups(StdSchedulerFactory.PROP_DATASOURCE_PREFIX);

        if (dsNames.length != 1) {
            throw new RuntimeException("can not parse org.quartz.dataSource.");
        }

        logger.info("org.quartz.dataSource.dsName=" + dsNames[0]);

        PropertiesParser pp = new PropertiesParser(cfg.getPropertyGroup(
                StdSchedulerFactory.PROP_DATASOURCE_PREFIX + "." + dsNames[0], true));

        pp.getUnderlyingProperties().remove(StdSchedulerFactory.PROP_CONNECTION_PROVIDER_CLASS);

        Properties properties = pp.getUnderlyingProperties();

        String oriPassword = properties.getProperty("password");
        String password = SecurityUtil.decryptionTrimWords(oriPassword);
        properties.setProperty("password", password);

        return properties;
    }
}


```