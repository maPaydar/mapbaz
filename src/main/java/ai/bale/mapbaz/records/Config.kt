package ai.bale.mapbaz.records

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "configs")
class Config {

    @Id
    @GeneratedValue
    var id: Int = 0
    var key: String = ""
    var value: String = ""
    var type: String = ""

    constructor() {
    }

    constructor(key: String, value: String, type: String) {
        this.key = key
        this.value = value
        this.type = type
    }

    constructor(id: Int, key: String, value: String, type: String) {
        this.id = id
        this.key = key
        this.value = value
        this.type = type
    }
}