package ai.bale.mapbaz.records

import javax.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "users")
class User {

    @Id
    var id: Int? = 0
    var paymentDate: Long = 0
    var name: String = ""

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "route_id")
    var routes: Set<Route>? = setOf<Route>()

    constructor() {
    }

    constructor(id: Int?, paymentDate: Long, name: String, routes: Set<Route>?) {
        this.id = id
        this.paymentDate = paymentDate
        this.name = name
        this.routes = routes
    }
}
