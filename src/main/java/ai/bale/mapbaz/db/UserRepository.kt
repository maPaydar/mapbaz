package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.User

class UserRepository : Repository<Int, User> {

    val session = HibernateUtil.sessionFactory.openSession()

    override fun create(user: User) {
        var tr = session.beginTransaction()
        session.persist(user)
        tr.commit()
    }

    override fun read(id: Int): User? {
        var tr = session.beginTransaction()
        val user = session.load(User::class.java, id)
        tr.commit()
        return user
    }

    override fun update(user: User) {
        var tr = session.beginTransaction()
        session.update(user)
        tr.commit()
    }

    override fun delete(user: User) {
        var tr = session.beginTransaction()
        session.delete(user)
        tr.commit()
    }
}
