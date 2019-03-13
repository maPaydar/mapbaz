package ai.bale.mapbaz.neshan.entity

class Location(var latitude: Double, var longitude: Double) {

    override fun toString(): String {
        return "$latitude,$longitude"
    }
}
