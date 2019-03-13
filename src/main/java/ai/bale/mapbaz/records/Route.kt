package ai.bale.mapbaz.records

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "route")
class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int? = 0
//    @OneToMany(
//        mappedBy = "users",
//        cascade = [CascadeType.ALL]
//    )
    var userId: Int? = 0
    var origin: String? = ""
    var destination: String? = ""
    var time: Long? = 0
    var scheduleTime: Int? = 0

    constructor() {
    }

    constructor(id: Int?, userId: Int?, origin: String?, destination: String?, time: Long?, scheduleTime: Int?) {
        this.id = id
        this.userId = userId
        this.origin = origin
        this.destination = destination
        this.time = time
        this.scheduleTime = scheduleTime
    }
}