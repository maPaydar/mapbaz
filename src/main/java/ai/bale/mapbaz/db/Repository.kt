package ai.bale.mapbaz.db

interface Repository<Key, Record> {

    fun create(t: Record)
    fun read(id: Key): Record?
    fun update(t: Record)
    fun delete(t: Record)
}
