package com.shurish.adminblinkit.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.denzcoskun.imageslider.models.SlideModel
import com.shurish.adminblinkit.FilteringProducts
import com.shurish.adminblinkit.databinding.ItemViewProductBinding
import com.shurish.adminblinkit.models.Product


class AdapterProduct(
    val onEditButtonClicked: (Product) -> Unit) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() , Filterable{
    class ProductViewHolder(val binding : ItemViewProductBinding) :ViewHolder(binding.root){

    }


    val diffUtil = object :DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return  ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
      return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = differ.currentList[position]



        holder.binding.apply {

            val imageList = ArrayList<SlideModel>()

            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!){
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }

            ivImageSlider.setImageList(imageList)
            tvProductTitle.text= product.productTitle
            val quantity = product.productQuantity.toString()+" "+product.productUnit
            tvProductQuantity.text= quantity
            tvProductPrice.text= "₹"+product.productPrice

            tvAdd.setOnClickListener {
                onEditButtonClicked(product)
            }

        }


    }


    private val filter : FilteringProducts?= null
    var originalList = ArrayList<Product>()
    override fun getFilter(): Filter {
        if (filter==null) return  FilteringProducts(this, originalList)
        return  filter
    }


}