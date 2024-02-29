package com.shurish.adminblinkit.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shurish.adminblinkit.AuthMainActivity
import com.shurish.adminblinkit.Constants
import com.shurish.adminblinkit.R
import com.shurish.adminblinkit.Utils
import com.shurish.adminblinkit.adapter.AdapterProduct
import com.shurish.adminblinkit.adapter.CategoriesAdapter
import com.shurish.adminblinkit.databinding.EditProductLayoutBinding
import com.shurish.adminblinkit.databinding.FragmentHomeBinding
import com.shurish.adminblinkit.models.Category
import com.shurish.adminblinkit.models.Product
import com.shurish.adminblinkit.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    val viewModel  : AdminViewModel by viewModels()

    private lateinit var binding : FragmentHomeBinding
    private  lateinit var adapterProduct: AdapterProduct


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = FragmentHomeBinding.inflate(layoutInflater)

        setStatusBarColor()

        setCategories()
        onLogout()

        searchProducts()
        getAllTheProducts("All")


        return binding.root
    }

    private fun onLogout() {
        binding.tbHomeFragment.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menuLogout ->{
                    logoutUser()
                    true
                }
                else -> {false}
            }
        }
        }



    private fun logoutUser() {





            val builder = AlertDialog.Builder(requireContext())
            val alertDialog = builder.create()
            builder.setTitle("Log Out")
                .setMessage("Your message goes here")
                .setPositiveButton("Yes"){_,_->
                    viewModel.logOutUser()
                    startActivity(Intent(requireContext(), AuthMainActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("No"){_,_->
                    alertDialog.dismiss()

                }
                .show()
                .setCancelable(false)




    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query = s.toString().trim()
                adapterProduct.filter?.filter(query)

            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun getAllTheProducts(category: Any?) {

        binding.shimmerViewContainer.visibility= View.VISIBLE

        lifecycleScope.launch {
            viewModel.fetchAllTheProducts(category).collect{

                if (it.isEmpty()){
                    binding.rvProducts.visibility= View.GONE
                    binding.tvText.visibility= View.VISIBLE
                }else{
                    binding.rvProducts.visibility= View.VISIBLE
                    binding.tvText.visibility= View.GONE
                }

                 adapterProduct = AdapterProduct(::onEditButtonClicked)
                binding.rvProducts.adapter= adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList= it as ArrayList<Product>
                binding.shimmerViewContainer.visibility= View.GONE

            }
        }

    }

    private fun setCategories() {

        val categoryList = ArrayList<Category>()

        for (i in 0 until  Constants.allProductCategoryIcon.size){
            categoryList.add(Category(Constants.allProductCategory[i], Constants.allProductCategoryIcon[i]))
        }

        binding.rvCategories.adapter = CategoriesAdapter(categoryList,::onCategoryClicked)


    }

    fun onCategoryClicked(category: Category){
        getAllTheProducts(category.title)

    }

    fun onEditButtonClicked(product: Product){



        val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            etProductTitle.setText(product.productTitle)
            etProductQuantity.setText(product.productQuantity.toString())
            etProductUnit.setText(product.productUnit)
            etProductPrice.setText(product.productPrice.toString())
            etProductStock.setText(product.productStock.toString())
            etProductCategory.setText(product.productCategory)
            etProductType.setText(product.productType)
        }

        val  alertDialog=AlertDialog.Builder(requireContext())
            .setView(editProduct.root)
            .create()
        alertDialog.show()

        editProduct.btnEdit.setOnClickListener {
            editProduct.etProductTitle.isEnabled=true
            editProduct.etProductCategory.isEnabled=true
            editProduct.etProductQuantity.isEnabled=true
            editProduct.etProductUnit.isEnabled=true
            editProduct.etProductPrice.isEnabled=true
            editProduct.etProductStock.isEnabled=true
            editProduct.etProductType.isEnabled=true
        }

        setAutoCompleteTextViews(editProduct)


        editProduct.btnSave.setOnClickListener {

            lifecycleScope.launch {

                product.productTitle=editProduct.etProductTitle.text.toString()
                product.productCategory=editProduct.etProductCategory.text.toString()
                product.productQuantity=editProduct.etProductQuantity.text.toString().toInt()
                product.productUnit=editProduct.etProductUnit.text.toString()
                product.productPrice=editProduct.etProductPrice.text.toString().toInt()
                product.productStock=editProduct.etProductStock.text.toString().toInt()
                product.productType=editProduct.etProductType.text.toString()
                viewModel.savingUpdatedProducts(product)
            }



            Utils.showToast(requireContext(), "Saved Changes")
            alertDialog.dismiss()
        }


    }

    private fun setAutoCompleteTextViews(editProduct: EditProductLayoutBinding) {

        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProduct)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        editProduct.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }

    private fun setStatusBarColor(){

        activity?.window?.apply {
            val statusBarColors= ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT> Build.VERSION_CODES.M){
                decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}