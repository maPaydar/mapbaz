package ai.bale.mapbaz.records

import javax.persistence.*

@Entity
@Table(name = "route")
class Route : java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var route_id: Int = 0
    var name: String? = ""
    var origin: String? = ""
    var destination: String? = ""
    var time: String? = ""
    var scheduleTime: Int? = 0
//    @ManyToOne(fetch = FetchType.LAZY)
    var userId: Long? = 0

    constructor() {
    }

    constructor(origin: String?, name: String?, destination: String?, time: String?, scheduleTime: Int?, userId: Long?) {
        this.name = name
        this.origin = origin
        this.destination = destination
        this.time = time
        this.scheduleTime = scheduleTime
        this.userId = userId
    }
}