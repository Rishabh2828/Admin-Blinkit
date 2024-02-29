package com.shurish.adminblinkit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shurish.adminblinkit.R
import com.shurish.adminblinkit.Utils
import com.shurish.adminblinkit.adapter.AdapterCartProducts
import com.shurish.adminblinkit.databinding.FragmentOrderDetailBinding
import com.shurish.adminblinkit.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class OrderDetailFragment : Fragment() {

    private val viewModel : AdminViewModel by viewModels()
    private  lateinit var binding : FragmentOrderDetailBinding
    private lateinit var adapterCartProducts: AdapterCartProducts
    private   var status = 0
    private   var currentstatus = 0
    private   var orderId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)

        getValue()
        settingStatus(status)
        onBackButtonClicked()
        lifecycleScope.launch {
            getOrderedProducts()
        }

        onChangeStatusButtonClicked()
        return binding.root
    }

    private fun onChangeStatusButtonClicked() {
        binding.btnChangeStatus.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(),it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener{menu ->
                when(menu.itemId){
                    R.id.menuReceived->{
                        currentstatus = 1

                        if (currentstatus>status){
                            status = 1
                            settingStatus(1)
                            viewModel.updateOrderStatus(orderId, 1)

                            lifecycleScope.launch {
                                viewModel.sendNotification(orderId, "Received", "Your order is received")
                            }
                        }
                        else{
                            Utils.showToast(requireContext(), "Order is already received")
                        }

                        true
                    }

                    R.id.menuDispached->{
                        currentstatus = 2

                        if (currentstatus>status){
                            status = 2
                            settingStatus(2)
                            viewModel.updateOrderStatus(orderId, 2)


                            lifecycleScope.launch {
                                viewModel.sendNotification(orderId, "Dispatched", "Your order is Dispatched")
                            }

                        }

                        else{
                            Utils.showToast(requireContext(), "Order is already dispatched")
                        }


                        true
                    }

                    R.id.menuDelivered->{
                        currentstatus = 3

                        if (currentstatus>status){
                            status = 3
                            settingStatus(3)
                            viewModel.updateOrderStatus(orderId, 3)


                            lifecycleScope.launch {
                                viewModel.sendNotification(orderId, "Delivered", "Your order is delivered")
                            }
                        }


                        true
                    }


                    else -> {false}
                }

            }

        }
    }

    suspend fun getOrderedProducts() {

        viewModel.getOrderedProducts(orderId).collect{cartList ->
            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)

        }
    }

    private fun settingStatus(status : Int){
        val statusToViews = mapOf(
            0 to listOf(binding.iv1),
            1 to listOf(binding.iv1, binding.iv2, binding.view1),
            2 to listOf(binding.iv1, binding.iv2, binding.view1, binding.iv3, binding.view2),
            3 to listOf(binding.iv1, binding.iv2, binding.view1, binding.iv3, binding.view2, binding.iv4,binding.view3)
        )

        val viewsToInt = statusToViews.getOrDefault(status, emptyList())

        for (view in viewsToInt){
            view.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.blue)
        }


    }

    private fun getValue() {
        val bundle = arguments
        status = bundle?.getInt("status")!!
        orderId = bundle.getString("orderId").toString()
        binding.tvUserAddress.text = bundle.getString("userAddress").toString()
    }

    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_orderFragment)
        }
    }


}