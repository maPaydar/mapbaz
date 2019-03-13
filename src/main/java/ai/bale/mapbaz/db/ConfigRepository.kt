package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.Config

class ConfigRepository : Repository<Int, Config> {

    val session = HibernateUtil.sessionFactory.currentSession

    override fun create(config: Config) {
        session.transaction.begin()
        session.persist(config)
        session.transaction.commit()
    }

    override fun read(id: Int): Config? {
        session.transaction.begin()
        val config = session.load(Config::class.java, id)
        session.transaction.commit()
        return config
    }

    override fun update(config: Config) {
        session.transaction.begin()
        session.update(config)
        session.transaction.commit()
    }

    override fun delete(config: Config) {
        session.transaction.begin()
        session.delete(config)
        session.transaction.commit()
    }
}
