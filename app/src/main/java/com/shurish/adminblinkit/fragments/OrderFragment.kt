package com.shurish.adminblinkit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shurish.adminblinkit.R
import com.shurish.adminblinkit.adapter.AdapterOrders
import com.shurish.adminblinkit.databinding.FragmentOrderBinding
import com.shurish.adminblinkit.models.OrderedItem
import com.shurish.adminblinkit.viewModels.AdminViewModel
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {

    private lateinit var binding : FragmentOrderBinding
    private val viewModel : AdminViewModel by viewModels()
    private lateinit var adapterOrders: AdapterOrders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)


        getAllOrders()
        return binding.root
    }

    private fun getAllOrders() {
        binding.shimmerViewContainer.visibility= View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList->
                if (orderList.isNotEmpty()){
                    val orderedList = ArrayList<OrderedItem>()
                    for (orders in orderList){
                        val title = StringBuilder()

                        var totalPrice=0
                        for (products in orders.orderList!!){
                            val price = products.productPrice?.substring(1)?.toInt()
                            val itemCount = products.productCount!!
                            totalPrice +=(price?.times(itemCount)!!)

                            title.append("${products.productCategory}, ")
                        }

                        val orderedItems = OrderedItem(orders.orderId, orders.orderDate, orders.orderStatus, title.toString(), totalPrice, orders.userAddress)
                        orderedList.add(orderedItems)
                    }

                    adapterOrders = AdapterOrders(requireContext(), ::onOrderItemViewClicked)
                    binding.rvOrders.adapter= adapterOrders
                    adapterOrders.differ.submitList(orderedList)
                    binding.shimmerViewContainer.visibility= View.GONE

                }

            }
        }

    }

    private fun onOrderItemViewClicked(orderedItem: OrderedItem){
        val bundle = Bundle()
        bundle.putInt("status", orderedItem.itemStatus!!)
        bundle.putString("orderId", orderedItem.orderId)
        bundle.putString("userAddress", orderedItem.userAddress)

        findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment, bundle)


    }




}