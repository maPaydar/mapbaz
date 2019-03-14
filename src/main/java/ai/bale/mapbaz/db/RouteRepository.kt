package ai.bale.mapbaz.db

import ai.bale.mapbaz.records.Route
import java.lang.Exception

class RouteRepository : Repository<Int, Route> {

    val session = HibernateUtil.sessionFactory.openSession()

    override fun create(route: Route) {
        val tr = session.beginTransaction()
        session.persist(route)
        tr.commit()
    }

    override fun read(id: Int): Route? {
        val tr = session.beginTransaction()
        val route = session.load(Route::class.java, id)
        tr.commit()
        return route
    }

    override fun update(route: Route) {
        val tr = session.beginTransaction()
        session.update(route)
        tr.commit()
    }

    override fun delete(route: Route) {
        val tr = session.beginTransaction()
        session.delete(route)
        tr.commit()
    }

    fun getRouteByName(name: String) : Route? {
        val tr = session.beginTransaction()
        try {
            val query = session.createSQLQuery("select * from route where route.name = ?")
                    .addEntity(Route::class.java)
                    .setString(1, name)
            tr.commit()
            return query.list()[0] as Route?
        } catch (e: Exception) {
            println(e)
            tr.commit()
        }
        return null
    }

    fun getRoutesByUser(userId: Long) : MutableList<Route> {
        val tr = session.beginTransaction()
        try {
            val query = session.createSQLQuery("select * from route where route.userId = ?")
                    .addEntity(Route::class.java)
                    .setLong(1, userId)
            tr.commit()
            return query.list() as MutableList<Route>
        } catch (e: Exception) {
            println(e)
            tr.commit()
        }
        return mutableListOf()
    }

    fun getRoutesByTime(time: String) :  MutableList<Route> {
        val tr = session.beginTransaction()
        try {
            val query = session.createSQLQuery("select * from route where route.time = ?")
                    .addEntity(Route::class.java)
                    .setString(1, time)
            tr.commit()
            return query.list() as MutableList<Route>
        } catch (e: Exception) {
            println(e)
            tr.commit()
        }
        return mutableListOf()
    }
}