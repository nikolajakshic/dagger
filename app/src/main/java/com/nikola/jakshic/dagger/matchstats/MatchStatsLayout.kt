package com.nikola.jakshic.dagger.matchstats

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class MatchStatsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var expanded = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        getChildAt(0).setOnClickListener {
            if (expanded) collapse() else expand()
        }
    }

    fun collapse() {
        getChildAt(1).visibility = View.GONE
        expanded = false
    }

    fun expand() {
        getChildAt(1).visibility = View.VISIBLE
        expanded = true
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val state = State(superState)
        state.expanded = if (expanded) 1 else 0
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is State) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        expanded = state.expanded != 0
        if (expanded) getChildAt(1).visibility = View.VISIBLE else View.GONE
    }

    private class State : BaseSavedState {
        var expanded = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(state: Parcel) : super(state) {
            expanded = state.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(expanded)
        }

        companion object CREATOR : Parcelable.Creator<State> {
            override fun createFromParcel(parcel: Parcel): State {
                return State(parcel)
            }

            override fun newArray(size: Int): Array<State?> {
                return arrayOfNulls(size)
            }
        }
    }
}
