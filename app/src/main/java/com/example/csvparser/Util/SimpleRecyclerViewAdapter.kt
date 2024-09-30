package com.example.csvparser.Util

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class SimpleRecyclerViewAdapter<T>(open var data: List<T> = emptyList(),
                                        private val idProvider: IdProvider<T>? = null,
                                        private val viewHolderProvider: SimpleViewHolderProvider<T>
) : RecyclerView.Adapter<SimpleViewHolder<T>>() {

    constructor(data: List<T>,
                idProvider: IdProvider<T>? = null,
                @LayoutRes layoutRID: Int,
                viewBinder: ViewBinder<T>
    ) : this(data, idProvider, { parent -> SimpleViewHolder(layoutRID, parent, viewBinder) })

    init {
        @Suppress("LeakingThis")
        setHasStableIds(idProvider != null)
    }

    open fun getItemAt(pos: Int): T = data[pos]

    override fun getItemId(position: Int): Long = idProvider?.invoke(position, getItemAt(position)) ?: super.getItemId(position)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): SimpleViewHolder<T> = viewHolderProvider(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SimpleViewHolder<T>, position: Int) = bindViewHolderInternal(holder, position)

    override fun onBindViewHolder(holder: SimpleViewHolder<T>, position: Int, payloads: MutableList<Any>) = bindViewHolderInternal(holder, position, payloads)

    protected fun bindViewHolderInternal(holder: SimpleViewHolder<T>, position: Int, payloads: MutableList<Any>? = null) {
        holder.bindHolder(getItemAt(position), payloads)
    }
}