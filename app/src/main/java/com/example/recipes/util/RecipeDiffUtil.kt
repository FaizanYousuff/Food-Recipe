package com.example.recipes.util

import androidx.recyclerview.widget.DiffUtil
import com.example.recipes.model.Result

class RecipeDiffUtil<T>(
    val oldList: List<T>,
    val newList: List<T>
)  : DiffUtil.Callback(){

    /**
     * This Returns size of an Old list
     */
    override fun getOldListSize(): Int {
        return oldList.size
    }

    /**
     * This Returns size of an new list
     */
    override fun getNewListSize(): Int {
        return newList.size
    }

    /**
     * This Item is called by DiffUtil to decide whether 2 objects
     * represent same item in old and new list
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition] == newList[newItemPosition]
    }
}