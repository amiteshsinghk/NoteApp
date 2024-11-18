package com.example.mydatabase.domain.note

import android.os.Parcel
import android.os.Parcelable
import com.example.mydatabase.presentation.*
import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long,
    val created: LocalDateTime
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        LocalDateTime.parse(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        if (id != null) {
            parcel.writeLong(id)
        }
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeLong(colorHex)
        parcel.writeString(created.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}

