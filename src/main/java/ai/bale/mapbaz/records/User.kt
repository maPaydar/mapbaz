package ai.bale.mapbaz.records

import javax.persistence.*

@Entity
@Table(name = "users")
class User : java.io.Serializable {

    @Id
    var user_id: Int? = 0
    var paymentDate: Long = 0
    var name: String = ""
//    @OneToMany(mappedBy = "user")
//    var routes: Set<Route> = setOf()

    constructor() {
    }

    constructor(id: Int) {
        this.user_id = id
    }

    constructor(id: Int?, paymentDate: Long, name: String) {
        this.user_id = id
        this.paymentDate = paymentDate
        this.name = name
    }
}
