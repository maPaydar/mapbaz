package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.Route

class RouteRepository : Repository<Int, Route> {

    val session = HibernateUtil.sessionFactory.currentSession

    override fun create(route: Route) {
        session.transaction.begin()
        session.persist(route)
        session.transaction.commit()
    }

    override fun read(id: Int): Route? {
        session.transaction.begin()
        val route = session.load(Route::class.java, id)
        session.transaction.commit()
        return route
    }

    override fun update(route: Route) {
        session.transaction.begin()
        session.update(route)
        session.transaction.commit()
    }

    override fun delete(route: Route) {
        session.transaction.begin()
        session.delete(route)
        session.transaction.commit()
    }
}