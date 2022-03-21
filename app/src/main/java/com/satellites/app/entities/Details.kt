package com.satellites.app.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
class Details(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "details_id")
    var details_id: Int? = null,
    @ColumnInfo(name = "cost_per_launch")
    var cost_per_launch: Int? = null,
    @ColumnInfo(name = "first_flight")
    var first_flight: String? = null,
    @ColumnInfo(name = "height")
    var height: Int? = null,
    @ColumnInfo(name = "mass")
    var mass: Int? = null,
    @ColumnInfo(name = "id")
    var id: Int? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(details_id)
        parcel.writeValue(cost_per_launch)
        parcel.writeString(first_flight)
        parcel.writeValue(height)
        parcel.writeValue(mass)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Details> {
        override fun createFromParcel(parcel: Parcel): Details {
            return Details(parcel)
        }

        override fun newArray(size: Int): Array<Details?> {
            return arrayOfNulls(size)
        }
    }
}