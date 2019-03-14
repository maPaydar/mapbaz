package ai.bale.mapbaz.neshan.entity

class Step {
    var name: String = ""
    var instruction: String = ""
    var distance: Distance = Distance()
    var duration: Duration = Duration()
    var polyline: OverviewPolyline = OverviewPolyline()
    var maneuver: String = ""
    var start_location: Array<Int> = arrayOf()
    var rotaryName: String = ""

    constructor() {
    }
}
