package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.Config
import ai.bale.mapbaz.records.Route
import java.lang.Exception

class ConfigRepository : Repository<Int, Config> {

    val session = HibernateUtil.sessionFactory.openSession()

    override fun create(config: Config) {
        val tr = session.beginTransaction()
        session.persist(config)
        tr.commit()
    }

    override fun read(id: Int): Config? {
        val tr = session.beginTransaction()
        val config = session.load(Config::class.java, id)
        tr.commit()
        return config
    }

    override fun update(config: Config) {
        val tr = session.beginTransaction()
        session.update(config)
        tr.commit()
    }

    override fun delete(config: Config) {
        val tr = session.beginTransaction()
        session.delete(config)
        tr.commit()
    }

    fun getConfigByKey(key: String) : Config? {
        val tr = session.beginTransaction()
        try {
            val query = session.createSQLQuery("select * from configs where configs.key = ?")
                    .addEntity(Config::class.java)
                    .setString(1, key)
            tr.commit()
            val list = query.list();
            if (list.isEmpty())
                return null
            return query.list()[0] as Config?
        } catch (e: Exception) {
            println(e)
            tr.commit()
        }
        return null
    }
}
