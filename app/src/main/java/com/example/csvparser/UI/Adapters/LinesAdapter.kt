package com.example.csvparser.UI.Adapters

/**
 * Adapter class to present data in RecyclerView
 * ViewHolder implements recycling of items
 * creating subitems for each data column (!) and reuse them if their number is correct
 */
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.csvparser.Data.Models.CsvDataType
import com.example.csvparser.Data.Models.CsvLine
import com.example.csvparser.R
import com.example.csvparser.Util.SimpleViewHolder
import com.example.csvparser.Util.SimpleRecyclerViewAdapter


class LinesAdapter : SimpleRecyclerViewAdapter<CsvLine>(viewHolderProvider = ::CsvLineViewHolder) {
    override var data: List<CsvLine> = emptyList()
}


private class CsvLineViewHolder(parent: ViewGroup) : SimpleViewHolder<CsvLine>(
    R.layout.item_line,
    parent
) {

    @SuppressLint("SimpleDateFormat")
    override fun bind(item: CsvLine) {

        val fieldsWrapper = itemView.findViewById<LinearLayout>(R.id.fields_area)

        if (!item.isCorrect()) {
            fieldsWrapper.setBackgroundColor(Color.parseColor("#FFFFDDDD"))
        }

        // if fieldsWrapper is NEW(not recycled) or contains wrong number of fields
        // then Create subitems for each CSV column
        // if number of items is the same - reuse them!

        // remove field views if their number for this item is wrong
        while(fieldsWrapper.childCount > item.values.size) {
            fieldsWrapper.removeView(fieldsWrapper.getChildAt(0))
        }
        // add field views if they NEW or their number is
        while(fieldsWrapper.childCount < item.values.size) {
            val fieldView = LayoutInflater.from(itemView.context).inflate(R.layout.item_field, itemView as ViewGroup, false)
            fieldsWrapper.addView(fieldView)
        }

        var idx = 0
        item.values.forEach { value ->
            // now use created or recycled fields:
            val fieldView = fieldsWrapper.getChildAt(idx)
            val tvFieldName = fieldView.findViewById<TextView>(R.id.field_name)
            val tvFieldValue = fieldView.findViewById<TextView>(R.id.field_value)
            val ivImage = fieldView.findViewById<ImageView>(R.id.image)

            // make data text color for each type
            when(item.types.get(idx)) {
                CsvDataType.DATE ->
                    tvFieldValue.setTextColor(Color.RED)
                CsvDataType.INT,
                CsvDataType.FLOAT ->
                    tvFieldValue.setTextColor(Color.MAGENTA)
                CsvDataType.LINK -> {
                    tvFieldValue.setTextColor(Color.BLUE)
                    // try to display link as a picture, nothing if cannot
                    Glide
                        .with(itemView.context)
                        .load(value)
                        .placeholder(android.R.drawable.ic_dialog_info)
                        .into(ivImage);
                    ivImage.visibility = View.VISIBLE
                }
                CsvDataType.GENERAL -> {}
            }

            tvFieldName.text = if(idx < item.titles.size) {
                tvFieldName.setTextColor(Color.DKGRAY)
                item.titles.get(idx)
            } else {    // number of titles does not match number of columns
                tvFieldName.setTextColor(Color.RED)
                "ERR:"
            }
            tvFieldValue.text = value
            idx++
        }

    }

}