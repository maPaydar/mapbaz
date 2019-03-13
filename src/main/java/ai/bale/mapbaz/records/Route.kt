package ai.bale.mapbaz.records

import javax.persistence.*

@Entity
@Table(name = "route")
class Route : java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var route_id: Int = 0
    var origin: String? = ""
    var destination: String? = ""
    var time: Long? = 0
    var scheduleTime: Int? = 0
//    @ManyToOne(fetch = FetchType.LAZY)
    var userId: Int? = 0

    constructor() {
    }

    constructor(origin: String?, destination: String?, time: Long?, scheduleTime: Int?, userId: Int?) {
        this.origin = origin
        this.destination = destination
        this.time = time
        this.scheduleTime = scheduleTime
        this.userId = userId
    }
}