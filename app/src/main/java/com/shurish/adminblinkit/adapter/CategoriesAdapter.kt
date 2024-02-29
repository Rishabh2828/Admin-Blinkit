package com.shurish.adminblinkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shurish.adminblinkit.databinding.ItemViewProductCategoryBinding
import com.shurish.adminblinkit.models.Category

class CategoriesAdapter(

    private val categoryArrayList: ArrayList<Category>,
   val onCategoryClicked: (Category) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>(){
    class CategoryViewHolder (val binding : ItemViewProductCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemViewProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryArrayList[position]
        holder.binding.apply {
            iVCategoryImage.setImageResource(category.image)
            tvCategoryTitle.text = category.title
        }

        holder.itemView.setOnClickListener{

            onCategoryClicked(category)
        }
    }


}