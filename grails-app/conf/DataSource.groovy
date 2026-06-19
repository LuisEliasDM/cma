dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dbCreate = 'update'
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    //    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

// environment specific settings
environments {
    development {
        dataSource {
            pooled = true
            url = "jdbc:mysql://10.0.44.55:3306/titulo_eCMA"
            username = "sEducacion"
            password = "xbdcvgh13tVeHPmSFZIIfhv4bGdQ8RaA"
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=false
                validationQuery="SELECT 1"
                jdbcInterceptors="ConnectionState"
            }
        }
    }
    test {
        dataSource {
            pooled = true
            url = "jdbc:mysql://10.0.44.55:4306/titulo_eCMA"
            username = "sEducacion"
            password = "38T8mwqe4Juk8UQeaDUr0DLM4VuNHmvK"
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=false
                validationQuery="SELECT 1"
                jdbcInterceptors="ConnectionState"
            }
        }
    }
    production {
        dataSource {
            pooled = true
            url = "jdbc:mysql://10.0.44.56:3306/titulo_eCMA"
            username = "sEducacion_prod"
            password = "jMDEJl9pxP6EQeHukw7hA5tK9Rlu13RH"
            properties {
                maxActive = 50
                maxIdle = 25
                minIdle = 5
                initialSize = 5
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=false
                validationQuery="SELECT 1"
                jdbcInterceptors="ConnectionState"
            }
        }
    }
}
