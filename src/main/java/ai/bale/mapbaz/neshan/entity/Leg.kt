package ai.bale.mapbaz.neshan.entity


class Leg {
    var summary: String = ""
    var distance: Distance = Distance()
    var duration: Duration = Duration()
    var steps: Array<Step> = arrayOf()

    constructor() {
    }
}
