package com.example.csvparser.Util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

typealias ViewBinder<T> = SimpleViewHolder<T>.(data: T) -> Unit

typealias IdProvider<T> = (position: Int, data: T) -> Long

typealias SimpleViewHolderProvider<T> = (parent: ViewGroup) -> SimpleViewHolder<T>


open class SimpleViewHolder<T>(itemView: View, private val binder: ViewBinder<T>? = null) :
    RecyclerView.ViewHolder(itemView),
    LayoutContainer
{
    companion object {
        private fun inflate(@LayoutRes layoutRid: Int, parent: ViewGroup) =
            LayoutInflater.from(parent.context).inflate(layoutRid, parent, false)
    }

    var itemData: T? = null
        private set

    override val containerView: View = itemView

    constructor(@LayoutRes layoutRid: Int, parent: ViewGroup, binder: ViewBinder<T>? = null) : this(
        inflate(layoutRid, parent),
        binder
    )

    open fun bindHolder(data: T, payload: MutableList<Any>?) {
        itemData = data
        if (payload == null) bind(data) else bind(data, payload)
    }

    open fun bind(item: T) {
        binder?.invoke(this, item)
    }

    open fun bind(item: T, payload: MutableList<Any>) {
        bind(item)
    }
}
