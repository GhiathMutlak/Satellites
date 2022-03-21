package com.satellites.app.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satellites")
data class Satellite(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "active")
    var active: Boolean = false,
    @ColumnInfo(name = "name")
    var name: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeByte(if (active) 1 else 0)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Satellite> {
        override fun createFromParcel(parcel: Parcel): Satellite {
            return Satellite(parcel)
        }

        override fun newArray(size: Int): Array<Satellite?> {
            return arrayOfNulls(size)
        }
    }

}