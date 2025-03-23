package com.example.adminwaveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.adapter.DeliveryAdapter
import com.example.adminwaveoffood.adapter.PendingOrderAdapter
import com.example.adminwaveoffood.databinding.ActivityPendingOrderBinding
import com.example.adminwaveoffood.databinding.PendingOrdersItemBinding

class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPendingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener{
            finish()
        }
        val orderedCustomerName = arrayListOf(
            "Saket Kumar",
            "Jessica Jogamaya",
            "Vipin Kumar",
        )
        val orderedQuantity = arrayListOf(
            "8",
            "6",
            "10"
        )
        val orderedFoodImage = arrayListOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3)
        val adapter = PendingOrderAdapter(orderedCustomerName,orderedQuantity,orderedFoodImage,this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}