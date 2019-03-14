package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.Config
import ai.bale.mapbaz.records.Route
import ai.bale.mapbaz.records.User
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import java.lang.System.setProperties
import org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO
import org.hibernate.cfg.AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS
import org.hibernate.cfg.AvailableSettings.SHOW_SQL
import org.hibernate.cfg.AvailableSettings.DIALECT
import org.hibernate.cfg.AvailableSettings.DRIVER
import org.hibernate.cfg.Environment
import java.util.*


object HibernateUtil {

    var sessionFactory = buildSessionFactory()
    private fun buildSessionFactory(): SessionFactory {
        try {
            val settings = Properties()
            settings.put(Environment.DRIVER, "org.postgresql.Driver")
            settings.put(Environment.URL, "jdbc:postgresql://192.168.3.95/mapbaz")
            settings.put(Environment.USER, "amin")
            settings.put(Environment.PASS, "123456789")
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect")
            settings.put(Environment.SHOW_SQL, "true")
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
            settings.put(Environment.AUTOCOMMIT, true)
            settings.put(Environment.HBM2DDL_AUTO, "update")

            val configuration = Configuration()
            configuration.setProperties(settings)
            val serviceRegistry = StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build()
            val sources = MetadataSources(serviceRegistry)
                    .addAnnotatedClass(User::class.java)
                    .addAnnotatedClass(Route::class.java)
                    .addAnnotatedClass(Config::class.java)
            val metadata = sources.metadataBuilder.build()
            return metadata.getSessionFactoryBuilder().build();
        } catch (ex: Throwable) {
            System.err.println("Initial SessionFactory creation failed.$ex")
            throw ExceptionInInitializerError(ex)
        }
    }

    fun shutdown() {
        sessionFactory.close()
    }
}
