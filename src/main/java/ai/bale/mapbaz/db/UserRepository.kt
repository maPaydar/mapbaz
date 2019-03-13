package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.User

class UserRepository : Repository<Int, User> {

    val session = HibernateUtil.sessionFactory.currentSession

    override fun create(user: User) {
        session.transaction.begin()
        session.persist(user)
        session.transaction.commit()
    }

    override fun read(id: Int): User? {
        session.transaction.begin()
        val user = session.load(User::class.java, id)
        session.transaction.commit()
        return user
    }

    override fun update(user: User) {
        session.transaction.begin()
        session.update(user)
        session.transaction.commit()
    }

    override fun delete(user: User) {
        session.transaction.begin()
        session.delete(user)
        session.transaction.commit()
    }
}
